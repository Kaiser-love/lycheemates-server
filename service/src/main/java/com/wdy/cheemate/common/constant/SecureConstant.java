package com.wdy.cheemate.common.constant;
public interface SecureConstant {

    /**
     * 认证请求头
     */
    String BASIC_HEADER_KEY = "LY-AUTH";

    /**
     * 认证请求头前缀
     */
    String BASIC_HEADER_PREFIX = "Basic ";

    /**
     * blade_client表字段
     */
    String CLIENT_FIELDS = "client_id, client_secret, access_token_validity, refresh_token_validity";

    /**
     * blade_client查询语句
     */
    String BASE_STATEMENT = "select " + CLIENT_FIELDS + " from blade_client";

    /**
     * blade_client查询排序
     */
    String DEFAULT_FIND_STATEMENT = BASE_STATEMENT + " order by client_id";

    /**
     * 查询client_id
     */
    String DEFAULT_SELECT_STATEMENT = BASE_STATEMENT + " where client_id = ?";

}
