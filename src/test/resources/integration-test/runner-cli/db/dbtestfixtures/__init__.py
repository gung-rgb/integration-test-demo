import yaml
from yaml import SafeLoader

from .core import *


def _load(src_dict: dict):
    datas = []
    for db_name, db_config in src_dict.items():
        rows = db_config['rows']
        datas.append(ImportTableData(db_name, rows))
    return datas


def import_from_yaml(src_file, db_driver, db_host, db_port, db_user, db_name, db_password):
    with open(src_file, 'r') as f:
        src_dict = yaml.load(f, Loader=SafeLoader)
    import_from_dict(src_dict, db_driver, db_host, db_port, db_user, db_name, db_password)


def import_from_dict(src_dct, db_driver, db_host, db_port, db_user, db_name, db_password):
    importer = _create_importer(db_driver, db_host, db_name, db_password, db_port, db_user)
    datas = _load(src_dct)
    importer.import_data(datas)


def rollback_from_dict(src_dct, db_driver, db_host, db_port, db_user, db_name, db_password):
    importer = _create_importer(db_driver, db_host, db_name, db_password, db_port, db_user)
    datas = _load(src_dct)
    importer.rollback_data(datas)


def _create_importer(db_driver, db_host, db_name, db_password, db_port, db_user):
    if db_driver.upper() == 'MYSQL':
        cmder = MysqlCmder(host=db_host, user=db_user, database=db_name, password=db_password,
                           port=3306 if db_port < 0 else db_port)
    else:
        # for pg
        cmder = None
        pass
    importer = DataImporter(cmder)
    return importer
