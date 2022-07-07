import click

from db.dbtestfixtures import import_from_yaml


@click.command()
@click.option('--file', type=click.Path(exists=True))
@click.option('--db_host', default='localhost', help='host of db')
@click.option('--db_port', default=-1, help='port of db')
@click.option('--db_name', default='test', help='port of db')
@click.option('--db_user', default='root', help='user of db')
@click.option('--db_password', default='', help='passowrd of db')
@click.option('--db_driver',
              type=click.Choice(['MYSQL', 'PG'], case_sensitive=False), required=True)
def hello(file, db_driver, db_host, db_port, db_user, db_name, db_password):
    import_from_yaml(file, db_driver, db_host, db_port, db_user, db_name, db_password)


if __name__ == '__main__':
    hello()
