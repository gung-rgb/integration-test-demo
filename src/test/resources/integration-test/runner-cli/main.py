# This is a sample Python script.

# Press ⌃R to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.
import subprocess


def print_hi(name):
    # Use a breakpoint in the code line below to debug your script.
    print(f'Hi, {name}')  # Press ⌘F8 to toggle the breakpoint.


def _wait_success(cmd, container_name):
    full_cmd = f'docker exec {container_name}  bash -c ' \
               f'\'exit_code=1 ; while [ ! 0 -eq $exit_code ]; do {cmd}  ' \
               f'; exit_code=$?; sleep 0.1 ; echo $exit_code; done\''
    print(full_cmd)
    subprocess.check_call(full_cmd, timeout=30, shell=True)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    _wait_success("mysql  --help", "integration_mysql")

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
