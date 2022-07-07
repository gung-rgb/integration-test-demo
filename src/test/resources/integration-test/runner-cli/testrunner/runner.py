import os
import pathlib
import subprocess
import time

import yaml
from yaml import UnsafeLoader

from api_mock import MockClient
from db.dbtestfixtures import import_from_dict, rollback_from_dict


def _pick_port_to_use():
    import socket
    sock = socket.socket()
    sock.bind(('', 0))
    return sock.getsockname()[1]


class TestRunner:
    def __init__(self, property_file):
        with open(property_file, 'r') as f:
            self.properties = {} if not property_file else yaml.load(f, Loader=UnsafeLoader)
        self.mock_client: MockClient = None

    def before_all(self):
        self.init_env()

    def init_env(self):
        if self.properties.get('create_mock_api_server'):
            self._create_smocker()
        if self.properties.get("create_mysql"):
            self._create_mysql()
        if self.properties.get("create_redis"):
            self._create_redis()

    def destroy_env(self):
        prefix = self.properties.get("container_prefix_name")
        code, output = subprocess.getstatusoutput(
            f" docker rm -f {prefix}_mysql {prefix}_redis  {prefix}_postgres  {prefix}_smocker ", )
        print(f"docker rm , ret code: {code}, output: {output}")

    def after_all(self):
        self.destroy_env()

    def _get_prepare_conf(self, test_file):
        prepare_file = self._get_prepare_declare_file(test_file)
        if prepare_file:
            with open(prepare_file, 'r') as f:
                return yaml.load(f, Loader=UnsafeLoader)
        return None

    def before_each(self, test_file):
        prepare_config = self._get_prepare_conf(test_file)
        if prepare_config:
            if prepare_config.get('data') and prepare_config.get('data').get('rds'):
                rds_conf = prepare_config.get('data').get('rds')
                driver = rds_conf.get('driver', 'mysql')
                port = self._deduce_mysql_port() if driver.upper() == "MYSQL" else None
                import_table_datas = rds_conf.get('tables', {})
                import_from_dict(import_table_datas, driver, 'localhost', port, 'root', 'test', '')
            if prepare_config.get('mock_api'):
                port = self._deduce_use_port('mock_api_server_admin_port', 'INTEGRATION_TEST_SMOCKER_ADMIN_PORT')
                if not self.mock_client:
                    self.mock_client = MockClient(f'http://localhost:{port}')
                self.mock_client.add_mocks(prepare_config['mock_api'])

    @staticmethod
    def _get_prepare_declare_file(test_file):
        p = pathlib.Path(test_file)
        prepare_file = p.parent.parent.joinpath(f'prepare_test/{p.name}')
        if prepare_file.is_file():
            return str(prepare_file)
        else:
            return None

    def after_each(self, test_file):
        if self.mock_client:
            self.mock_client.reset()
        prepare_config = self._get_prepare_conf(test_file)
        if prepare_config:
            if prepare_config.get('data') and prepare_config.get('data').get('rds'):
                rds_conf = prepare_config.get('data').get('rds')
                driver = rds_conf.get('driver', 'mysql')
                port = self._deduce_mysql_port() if driver.upper() == "MYSQL" else None
                import_table_datas = rds_conf.get('tables', {})
                rollback_from_dict(import_table_datas, driver, 'localhost', port, 'root', 'test', '')

    def do_run_test(self, test_file):
        subprocess.check_call(f"hrp run {test_file}", shell=True)

    def run_test(self, test_file):
        self.before_each(test_file)
        self.do_run_test(test_file)
        self.after_each(test_file)

    def just_run(self, test_file):
        self.run_test(test_file)

    def _deduce_use_port(self, conf_prop, env_var):
        conf_prop_val = self.properties.get(conf_prop)
        if conf_prop_val:
            return int(conf_prop_val)
        return int(os.environ[env_var])

    def _deduce_mysql_port(self):
        return self._deduce_use_port('mysql_port', "INTEGRATION_TEST_MYSQL_PORT")

    def _create_mysql(self):
        port = self.properties.get("mysql_port", _pick_port_to_use())
        os.environ["INTEGRATION_TEST_MYSQL_PORT"] = str(port)
        container_name = f'{self.properties.get("container_prefix_name")}_mysql'
        subprocess.check_call(f"""
        docker run  -d --name {container_name}  \
        -e MYSQL_ALLOW_EMPTY_PASSWORD=true -e MYSQL_DATABASE=test \
        -p {port}:3306 mysql
        """, shell=True)
        self._wait_success(f'echo select 1 | mysql 2>/dev/null', container_name)
        time.sleep(5)

    @staticmethod
    def _wait_success(cmd, container_name):
        full_cmd = f'docker exec {container_name}  bash -c ' \
                   f'\'exit_code=1 ; while [ ! 0 -eq $exit_code ]; do {cmd}  ' \
                   f'; exit_code=$?; sleep 0.1 ; done\''
        print(full_cmd)
        subprocess.check_call(full_cmd, timeout=60, shell=True)

    def _create_redis(self):
        port = self.properties.get("redis_port", _pick_port_to_use())
        os.environ["INTEGRATION_TEST_REDIS_PORT"] = str(port)
        subprocess.check_call(f"""
        docker run  -d --name  {self.properties.get("container_prefix_name")}_redis \
        -p {port}:6379   redis
        """, shell=True)

    def _create_smocker(self):
        admin_port = self.properties.get("mock_api_server_admin_port", _pick_port_to_use())
        mock_port = self.properties.get("mock_api_server_port", _pick_port_to_use())
        os.environ["INTEGRATION_TEST_SMOCKER_ADMIN_PORT"] = str(admin_port)
        os.environ["INTEGRATION_TEST_SMOCKER_MOCK_PORT"] = str(mock_port)
        subprocess.check_call(f"""
        docker run  -d --name  {self.properties.get("container_prefix_name")}_smocker \
        -p {admin_port}:8081 -p {mock_port}:8080   thiht/smocker
        """, shell=True)


if __name__ == '__main__':
    runner = TestRunner('../demo/.integration.yaml')
    try:
        runner.before_all()
        runner.run_test('../demo/testcases/test.yaml')
    finally:
        runner.after_all()
