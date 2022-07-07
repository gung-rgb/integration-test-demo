import requests
import yaml


class MockClient:
    def __init__(self, server_url):
        self.server_url = server_url

    def add_mocks(self, mocks=None, file=None):
        if file:
            with open(file, 'r') as f:
                mocks_data = f.read()
        else:
            assert mocks
            if type(mocks) != str:
                mocks_data = yaml.dump(mocks)
            else:
                mocks_data = mocks
        resp = requests.post(self.server_url + '/mocks', data=mocks_data,
                             headers={"Content-Type": "application/x-yaml"})
        self._validate_response(resp)

    @staticmethod
    def _validate_response(resp):
        if resp.status_code != 200:
            raise Exception(f"bad resp: {resp.status_code}, {resp.text}")

    def reset(self):
        resp = requests.post(self.server_url + '/reset')
        self._validate_response(resp)


if __name__ == '__main__':
    client = MockClient('http://localhost:8081')
    client.add_mocks(file='mocks.yaml')
    client.reset()
