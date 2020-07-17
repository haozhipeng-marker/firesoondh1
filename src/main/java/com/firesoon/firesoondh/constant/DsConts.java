package com.firesoon.firesoondh.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/4
 */
@Component
public class DsConts {

    public static String dsHost;

    public static String PROJECT_LIST_PAG;

    public static String LOGIN;

    public static String GET_USER_INFO;

    public static String CREATE_PROJECT;

    public static String ALL_PROJECT_LIST;
    public static String SELECT_PROJECT_ID;

    public static String DELETE_PROJECT;

    public static String PROCESS_VERIFY_NAME;

    public static String PROCESS_SAVE;

    public static String PROCESS_DELETE;
    public static String PROCESS_SELECT_ID;

    public static String DATASOURCES_DETAILS;
    public static String DATASOURCES_LIST;

    public static String UPLOAD_FILE_DELETE;
    public static String UPLOAD_FILE_VERIFY;
    public static String UPLOAD_CREATE_DIR;
    public static String UPLOAD_DIR_SELECT;
    public static String UPLOAD_CREATE_FILE;
    public static String UPLOAD_SELECT_RESOURCE_BY_NAME;
    public static String UPLOAD_SELECT_RESOURCE_BY_ID;

    @Value("${dsHost}")
    public void setConfPath(String confPath) {
        dsHost = confPath + "/dolphinscheduler";
        LOGIN = dsHost + "/login";
        PROJECT_LIST_PAG = dsHost + "/projects/list-paging";
        GET_USER_INFO = dsHost + "/users/get-user-info";
        CREATE_PROJECT = dsHost + "/projects/create";
        DELETE_PROJECT = dsHost + "/projects/delete";
        ALL_PROJECT_LIST = dsHost + "/projects/queryAllProjectList";
        SELECT_PROJECT_ID = dsHost + "/projects/query-by-id";
        PROCESS_VERIFY_NAME = dsHost + "/projects/%s/process/verify-name";
        PROCESS_SAVE = dsHost + "/projects/%s/process/save";
        PROCESS_DELETE = dsHost + "/projects/%s/process/delete";
        DATASOURCES_DETAILS = dsHost + "/datasources/update-ui";
        DATASOURCES_LIST = dsHost + "/datasources/list-paging";
        UPLOAD_FILE_DELETE = dsHost + "/resources/delete";
        UPLOAD_FILE_VERIFY = dsHost + "/resources/verify-name";
        UPLOAD_CREATE_DIR = dsHost + "/resources/directory/create";
        UPLOAD_DIR_SELECT = dsHost + "/resources/list-paging";
        UPLOAD_CREATE_FILE = dsHost + "/resources/create";
        UPLOAD_SELECT_RESOURCE_BY_NAME = dsHost + "/resources/queryResourceByFullName";
        UPLOAD_SELECT_RESOURCE_BY_ID = dsHost + "/resources/queryResourceById";
        PROCESS_SELECT_ID = dsHost + "/projects/%s/process/select-by-id";
    }

    /**
     * 工作流上线标识
     */
    public static String PROCESS_ONLINE = "ONLINE";
    /**
     * 工作流下线标识
     */
    public static String PROCESS_OFFLINE = "OFFLINE";


    /**
     * 工作流已经存在返回code值
     */
    public static Integer PROCESS_ALREADY_EXIST = 50002;
    /**
     * 工作流正在使用返回code值
     */
    public static Integer PROCESS_IS_USING = 50021;

    /**
     * 项目正在使用返回code值
     */
    public static Integer PROJECT_IS_USING = 10137;

    /**
     * 资源已经存在返回code值
     */
    public static Integer RESOURCE_ALREADY_EXIST = 20005;

}
