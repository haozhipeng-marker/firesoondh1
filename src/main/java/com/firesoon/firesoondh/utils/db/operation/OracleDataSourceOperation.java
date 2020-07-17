package com.firesoon.firesoondh.utils.db.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.firesoon.firesoondh.model.dtotype.db.DbCommentsDTO;
import com.firesoon.firesoondh.model.dtotype.db.OracleColumnDTO;
import com.firesoon.firesoondh.utils.ConvertUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * @description: oracle数据源操作
 * @author: Yz
 * @date: 2020/6/11
 */
public class OracleDataSourceOperation implements DataSourceOperation {
    @Override
    public List<DbCommentsDTO> getTables(JdbcTemplate jdbcTemplate) {

        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT\n" +
                        "\tt.table_name name,\n" +
                        "\tc.COMMENTS comments \n" +
                        "FROM\n" +
                        "\tuser_tables t,\n" +
                        "\tuser_tab_comments c \n" +
                        "WHERE\n" +
                        "\tt.TABLE_NAME = c.TABLE_NAME \n" +
                        "ORDER BY\n" +
                        "\tt.table_name");

        return ConvertUtil.convert(list, DbCommentsDTO.class);
    }

    @Override
    public List<OracleColumnDTO> getColumns(JdbcTemplate jdbcTemplate, String tableName) {

        List<Map<String, Object>> list = jdbcTemplate.queryForList("" +
                "SELECT\n" +
                "\tutc.column_name name,\n" +
                "\tutc.data_type type,\n" +
                "\tutc.data_length length,\n" +
                "\tutc.data_precision,\n" +
                "\tutc.data_Scale,\n" +
                "\tDECODE(utc.nullable, 'N',0,1)  isNull,\n" +
                "\tutc.data_default defaultValue,\n" +
                "\tucc.comments,\n" +
                "\tDECODE(uc.constraint_name, null,0,1)  isPrimaryKey\n" +
                "FROM\n" +
                "\tuser_tab_columns utc\n" +
                "\tLEFT JOIN user_cons_columns  uscc on uscc.position = 1 and utc.table_name = uscc.table_name and utc.column_name = uscc.column_name\n" +
                "\tLEFT JOIN user_constraints uc ON uc.CONSTRAINT_TYPE = 'P' \n" +
                "\tAND uc.CONSTRAINT_NAME = uscc.CONSTRAINT_NAME \n" +
                "\tAND uc.TABLE_NAME = uscc.TABLE_NAME\n" +
                "\tLEFT JOIN user_col_comments ucc ON utc.table_name = ucc.table_name \n" +
                "\tAND utc.column_name = ucc.column_name \n" +
                "WHERE\n" +
                "\tutc.table_name = ?  order by column_id ", tableName);

        return ConvertUtil.convert(list, OracleColumnDTO.class);


    }

    @Override
    public String getPrimaryKey(JdbcTemplate jdbcTemplate, String tableName) {
        List<String> data = jdbcTemplate.queryForList("" +
                "\n" +
                "SELECT uscc.column_name from user_cons_columns  uscc " +
                "LEFT JOIN  user_constraints uc ON uc.CONSTRAINT_TYPE = 'P' \n" +
                "\tAND uc.CONSTRAINT_NAME = uscc.CONSTRAINT_NAME \n" +
                "\tAND uc.TABLE_NAME = uscc.TABLE_NAME  WHERE\n" +
                "\tuscc.table_name = ? and uscc.position = 1" +
                "", String.class, tableName);
        if (!CollectionUtils.isEmpty(data)) {
            return data.get(0);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public String getCreateTableSql(JdbcTemplate jdbcTemplate, String tableName) {
        String s = jdbcTemplate.execute("declare\n" +
                "a Clob;\n" +
                "BEGIN\n" +
                "\t\tDBMS_METADATA.set_transform_param ( DBMS_METADATA.session_transform, 'STORAGE', FALSE );\n" +
                "\t\t DBMS_METADATA.set_transform_param(DBMS_METADATA.session_transform, 'TABLESPACE', FALSE);\n" +
                "\t\t  DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'REF_CONSTRAINTS',FALSE);\n" +
                "\t\t  DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM,'SEGMENT_ATTRIBUTES',FALSE);\n" +
                "\t\tSELECT \tdbms_metadata.get_ddl ( 'TABLE', :tableName )   into a from dual;\n" +
                " :cc:=a;\n" +
                "END;", (CallableStatementCallback<String>) callableStatement -> {
            callableStatement.setString(1, tableName);
            callableStatement.registerOutParameter(2, Types.CLOB);

            callableStatement.execute();
            return callableStatement.getString(2);
        });
        if (StringUtils.isNoneBlank(s)) {
            s = s.replaceAll("([\n\t\r\"])", "");

            s = s.replaceFirst(((DruidDataSource) jdbcTemplate.getDataSource())
                    .getUsername().toUpperCase().concat("."), "");
        }
        return s;
    }


}
