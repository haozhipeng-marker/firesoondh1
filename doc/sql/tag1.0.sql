create table FSDH.DATA_ACCESS
(
    ID  VARCHAR2(36) NOT NULL PRIMARY KEY,
    DATA_SOURCE_ID NUMBER(10) ,
    DATA_SOURCE_TYPE VARCHAR2(10) ,
    DATA_SOURCE_NAME VARCHAR2(100) ,
    ACCESS_NAME VARCHAR2(100) ,
    ACCESS_DESC VARCHAR2(255) ,
    CREATOR_ID NUMBER(10) ,
    CREATE_TIME DATE,
    UPDATE_TIME DATE
);
comment on table FSDH.DATA_ACCESS
is '数据接入';
-- Add comments to the columns
comment on column FSDH.DATA_ACCESS.ID
is '主键id';
comment on column FSDH.DATA_ACCESS.DATA_SOURCE_ID
is '数据源id';
comment on column FSDH.DATA_ACCESS.DATA_SOURCE_TYPE
is '数据源类型';
comment on column FSDH.DATA_ACCESS.DATA_SOURCE_NAME
is '数据源名称';
comment on column FSDH.DATA_ACCESS.ACCESS_NAME
is '接入名称';
comment on column FSDH.DATA_ACCESS.ACCESS_DESC
is '接入备注';
comment on column FSDH.DATA_ACCESS.CREATOR_ID
is '创建者';
comment on column FSDH.DATA_ACCESS.CREATE_TIME
is '创建时间';
comment on column FSDH.DATA_ACCESS.UPDATE_TIME
is '更新时间';


create table FSDH.DATA_ACCESS_ASSOCIATE
(
    DATA_ACCESS_ID  VARCHAR2(36) NOT NULL PRIMARY KEY ,
    PROJECT_ID NUMBER(10)
);
comment on table FSDH.DATA_ACCESS_ASSOCIATE
is '数据接入关联信息表';
-- Add comments to the columns
comment on column FSDH.DATA_ACCESS_ASSOCIATE.DATA_ACCESS_ID
is '数据人接入id';
comment on column FSDH.DATA_ACCESS_ASSOCIATE.PROJECT_ID
is 'ds项目id';


create table FSDH.DATA_ACCESS_CONF
(
    ID VARCHAR2(36) NOT NULL PRIMARY KEY,
    DATA_ACCESS_ID VARCHAR2(36),
    ORIGIN_TABLE VARCHAR2(50) ,
    TARGET_TABLE VARCHAR2(50) ,
    IS_FULL NUMBER(1),
    ADD_TYPE NUMBER(2),
    PRIMARY_KEY VARCHAR2(50),
    PRE_SQL CLOB,
    FILTER_SQL CLOB,
    FETCH_SIZE NUMBER(10),
    CREATE_TIME DATE,
    UPDATE_TIME DATE
);
comment on table FSDH.DATA_ACCESS_CONF
is '数据接入配置';
-- Add comments to the columns
comment on column FSDH.DATA_ACCESS_CONF.ID
is '主键id';
comment on column FSDH.DATA_ACCESS_CONF.DATA_ACCESS_ID
is '数据接入id';
comment on column FSDH.DATA_ACCESS_CONF.ORIGIN_TABLE
is '源表';
comment on column FSDH.DATA_ACCESS_CONF.TARGET_TABLE
is '目标表';
comment on column FSDH.DATA_ACCESS_CONF.IS_FULL
is '是否全量 1是 0不是';
comment on column FSDH.DATA_ACCESS_CONF.ADD_TYPE
is '增量类型 1merge 2insert';
comment on column FSDH.DATA_ACCESS_CONF.PRIMARY_KEY
is '主键(增量类型为merge时必须)';
comment on column FSDH.DATA_ACCESS_CONF.PRE_SQL
is '前置处理SQL';
comment on column FSDH.DATA_ACCESS_CONF.FILTER_SQL
is '数据过滤SQL';
comment on column FSDH.DATA_ACCESS_CONF.FETCH_SIZE
is '每个batch获取数据大小';
comment on column FSDH.DATA_ACCESS_CONF.CREATE_TIME
is '创建时间';
comment on column FSDH.DATA_ACCESS_CONF.UPDATE_TIME
is '更新时间';


create table FSDH.DATA_ACCESS_CONF_ASSOCIATE
(
    DATA_ACCESS_CONF_ID  VARCHAR2(36) NOT NULL PRIMARY KEY ,
    PROCESS_ID NUMBER(10),
    RESOURCES_ID NUMBER(10)
);
comment on table FSDH.DATA_ACCESS_CONF_ASSOCIATE
is '数据接入配置关联表';
-- Add comments to the columns
comment on column FSDH.DATA_ACCESS_CONF_ASSOCIATE.DATA_ACCESS_CONF_ID
is '数据接入配置id';
comment on column FSDH.DATA_ACCESS_CONF_ASSOCIATE.PROCESS_ID
is 'ds工作流id';
comment on column FSDH.DATA_ACCESS_CONF_ASSOCIATE.RESOURCES_ID
is 'ds资源id';

