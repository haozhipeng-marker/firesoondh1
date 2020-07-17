package com.firesoon.firesoondh.model.dtotype.data.access;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: spark配置文件dto
 * @author: Yz
 * @date: 2020/6/22
 */
@Data
public class SparkConfDTO {
    List<Unit> units = new ArrayList<>();
    List<String> blueprint = new ArrayList<String>() {{
        add("start");
    }};
    Map<String, List<DbConf>> dbs = new HashMap<>();

    @Data
    public static class Unit {
        String name;
        String actor;
        String db;
        String table;
        Integer batchSize = 1000;
        Integer timeout = 10;
        String mode;
        String targetTablePartition;
        String sourceTablePartition;
        String priKey;
        String tTable;
        String dllPreSql;
        String sqlFile;
        String query;
        Integer fetchSize = 10000;
        Boolean cache = false;
        String view;
    }

    @Data
    public static class DbConf {
        String dbName;
        String url;
        String username;
        String password;
        String driver;
        String type="normal";
    }
}
