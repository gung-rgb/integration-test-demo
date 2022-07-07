import click

from .runner import *


@click.group()
def cli():
    pass


@click.command
@click.option('--config_file', type=click.Path(exists=True))
def init_env(config_file):
    TestRunner(config_file).init_env()


@click.command()
@click.option('--config_file', type=click.Path(exists=True))
def destroy_env(config_file):
    TestRunner(config_file).destroy_env()


@click.command()
@click.option('--config_file', type=click.Path(exists=True))
@click.argument('test_file', type=click.Path(exists=True))
def just_run(config_file, test_file):
    TestRunner(config_file).just_run(test_file)


cli.add_command(init_env)
cli.add_command(destroy_env)
cli.add_command(just_run)

if __name__ == '__main__':
    cli()
