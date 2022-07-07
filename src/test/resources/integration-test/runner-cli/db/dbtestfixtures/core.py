import datetime
import re
from dataclasses import dataclass

from mysql import connector as mysql_connector
from tenacity import retry, wait_fixed, stop_after_attempt


@dataclass()
class TableColumn:
    name: str
    type: str
    is_primary: bool


@dataclass()
class DBTable:
    name: str
    columns: list[TableColumn]


class DBCmder:
    def __init__(self, **kwargs):
        pass

    @staticmethod
    def gen_create_table_sql(tb: DBTable):
        sql = f"CREATE TABLE IF NOT EXISTS {tb.name} ("
        for i, col in enumerate(tb.columns):
            sql += "\n" + f"{col.name} {col.type}"
            if col.is_primary and col.type:
                sql += " PRIMARY KEY AUTO_INCREMENT"
            if i < len(tb.columns) - 1:
                sql += ","
            else:
                sql += ")"
        return sql

    def create_table(self, tb: DBTable):
        sql = self.gen_create_table_sql(tb)
        self.execute_sql(sql)

    def insert_row(self, tbl: DBTable, row: dict):
        col_names = []
        col_values = []
        for colum in tbl.columns:
            col_names.append(colum.name)
            if colum.type == "INT" or colum.type == "FLOAT" or colum.type == "BOOLEAN":
                col_values.append(str(row.get(colum.name, "NULL")))
            else:
                col_values.append(f"'{row.get(colum.name, 'NULL')}'")
        sql = f""" insert into {tbl.name}({','.join(col_names)})values({','.join(col_values)}) """
        self.execute_sql(sql)

    def delete_by_pk(self, table_name, **pks):
        conditions = ""
        if pks:
            for k, v in pks.items():
                if type(v) == str:
                    v = f"'{v}'"
                if conditions:
                    conditions += " and "
                conditions += f"{k} =  {v}"
        self.execute_sql(f"delete from {table_name} where {conditions}")

    def execute_sql(self, sql):
        pass


class MysqlCmder(DBCmder):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.kwargs = kwargs

    @retry(wait=wait_fixed(1), stop=stop_after_attempt(10))
    def _get_connection(self):
        return mysql_connector.connect(**self.kwargs)

    def execute_sql(self, sql, **kwargs):
        cnx = self._get_connection()
        cursor = cnx.cursor()
        try:
            return cursor.execute(sql)
        finally:
            cursor.close()
            cnx.commit()
            cnx.close()


@dataclass()
class ImportTableData:
    table_name: str
    datas: list[dict]


class DataImporter:
    def __init__(self, db_cmder: DBCmder):
        self.db_cmder = db_cmder

    def import_data(self, datas: list[ImportTableData]):
        for data in datas:
            self.import_single_table(data)
            print(f"import {len(datas)} rows into table: {data.table_name} success")

    def rollback_data(self, datas: list[ImportTableData]):
        for data in datas:
            self.rollback_single_table(data)

    @staticmethod
    def _deduce_table_definition(data: ImportTableData):
        columns_dict = {}
        for row in data.datas:
            for col_name, value in row.items():
                if not columns_dict.get(col_name):
                    if type(value) == str:
                        if re.match(r'\d{4}-\d{2} \d{2}:\d{2}:\d{2}', value):
                            db_type = "DATETIME"
                        else:
                            db_type = "TEXT"
                    elif type(value) == datetime.datetime:
                        db_type = "DATETIME"
                    elif type(value) == int:
                        db_type = "INT"
                    elif type(value) == float:
                        db_type = "DECIMAL"
                    elif type(value) == bool:
                        db_type = "BOOLEAN"
                    else:
                        db_type = None
                    columns_dict[col_name] = db_type
        columns = []
        table_name = data.table_name
        for col_name, db_type in columns_dict.items():
            if db_type is None:
                raise Exception(f"can not deduce db type, col_name: {col_name}, table_name: {table_name}")
            columns.append(TableColumn(col_name, db_type, col_name.lower() == "id"))

        return DBTable(table_name, columns)

    def import_single_table(self, data: ImportTableData):
        table_definition = self._deduce_table_definition(data)
        self.db_cmder.create_table(table_definition)
        for row in data.datas:
            self.db_cmder.insert_row(table_definition, row=row)

    def rollback_single_table(self, data: ImportTableData):
        table_definition = self._deduce_table_definition(data)
        self.db_cmder.create_table(table_definition)
        for row in data.datas:
            self.db_cmder.delete_by_pk(table_definition.name, id=row['id'])
