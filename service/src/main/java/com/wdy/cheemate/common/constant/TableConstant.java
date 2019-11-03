package com.wdy.cheemate.common.constant;

/**
 * @description: 数据表常量
 * @author: dongyang_wu
 * @create: 2019-07-31 09:20
 */
public interface TableConstant {
    /**
     * 用于分页显示基本脚本常量开始
     */
    String GET_PAGE_ALL = "<script>" + TableConstant.SELECT_ALL;
    String GET_PAGE_ALL_CONDITION =
            "<if test='conditions!=null '>" +
                    "  where ${conditions} " +
                    "</if>" +
                    "ORDER BY ${orderBy} ${sortType} " +
                    "limit #{page},#{count}" +
                    "</script>";

    String GET_PAGE_ALL_SIZE = "<script>" + TableConstant.SELECT_COUNT;
    String GET_PAGE_ALL_SIZE_CONDITION =
            "<if test='conditions!=null '>" +
                    " where ${conditions}  " +
                    "</if></script>";

    String GET_ALL_CONDITION = TableConstant.LOGIC_EXIST_STR +
            "<if test='conditions!=null '>" +
            " and ( ${conditions} ) " +
            "</if></script>";
    /**
     * 用于分页显示基本脚本常量结束
     */
    String LOGIC_EXIST_STR = TableConstant.WHERE;
    String ID = "id";
    String STATUS = "status";
    String LOGIC = "logic";
    Integer LOGIC_EXIST_CONSTANT = 0;
    Integer LOGIC_NOT_EXIST_CONSTANT = 1;
    String LOGIC_EXIST = TableConstant.LOGIC + " = 0 ";
    String SELECT_COUNT = "SELECT COUNT(*) FROM ";
    String SELECT_ALL = "SELECT * FROM ";
    String SELECT_ID_ALL = "SELECT id FROM ";
    String WHERE = " WHERE ";
    /**
     * 时间常量
     */
    String CREATE_TIME = "create_time";
    /**
     * Team 与或非常量
     */
    String TEAM_CONDITON = " FIND_IN_SET(%s,team_ids) ";

    interface TABLE {
        String TABLE_USER_TO_FOCUS = "user_to_focus";
        String TABLE_POST = "post";
        String SINGLE_CHAT_MESSAGE = "single_chat_message";
        String USER_LEARN_INVITATION = "user_learn_invitation";
        String USER_CHALLENGE_INVITATION = "user_challenge_invitation";
        String TABLE_USER = "user";
        String TABLE_USER_FRIEND = "user_friend";
        String TABLE_TEAM = "team";
        String TABLE_METRICS = "metrics";
        String TABLE_METIS_EXCEPTION = "metis_exception";
        String TABLE_KPI = "kpi";
        String LOG_API = "log_api";
    }

    interface USER_TO_FOCUS {
        String USER_ID = "user_id";
        String FOCUS_USER_ID = "focus_user_id";
    }

    interface USER {
        String MAIL = "mail";
        String TEAM_IDS = "team_ids";
        String ADMIN_USER_ID = "admin_user_id";
    }

    interface USER_CHALLENGE_INVITATION {
    }

    interface SINGLE_CHAT_MESSAGE {
    }

    interface TABLE_COMMON_COLUMN {
        String STATE = "state";
        String SEND_USER_ID = "send_user_id";
        String RECEIVE_USER_ID = "receive_user_id";
        String SEND_TIME = "send_time";

    }

    interface TEAM {
        String NAME = "name";
    }
}