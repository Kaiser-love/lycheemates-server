package com.wdy.cheemate.common.util;

import com.google.common.collect.ImmutableMap;
import com.wdy.cheemate.common.constant.StringConstant;
import com.wdy.cheemate.common.constant.TableConstant;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestBean;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description: 数据库工具类
 * @author: dongyang_wu
 * @create: 2019-07-30 16:48
 */
public class SqlUtil {
    public static ImmutableMap.Builder<String, Object> map(String query, String queryString) {
        return ImmutableMap.<String, Object>builder().put(query, queryString);
    }

    public static ImmutableMap.Builder<String, String> mapWithString(String query, String queryString) {
        return ImmutableMap.<String, String>builder().put(query, queryString);
    }

    public static ImmutableMap.Builder<Long, String> map(Long query, String queryString) {
        return ImmutableMap.<Long, String>builder().put(query, queryString);
    }

    public static ImmutableMap.Builder<String, Object> map(String query, Object queryString) {
        return ImmutableMap.<String, Object>builder().put(query, queryString);
    }

    public static Map<String, Object> map(RequestBean requestBean) {
        HashMap<String, Object> resultMap = new HashMap<>();
        requestBean.getItems().forEach(item ->
                resultMap.put(item.getQuery(), item.getQueryString())
        );
        return resultMap;
    }

    public static String getConditionString(List<PageRequestBean.PageRequestItem> conditions, String conditionConnection) {
        if (CollectionUtil.isNotEmpty(conditions)) {
            String[] connections = conditionConnection.split(" ");
            StringBuffer sql = new StringBuffer();
            int i;
            for (i = 0; i < conditions.size() - 1; i++) {
                addCondition(sql, conditions.get(i), connections[i]);
            }
            addCondition(sql, conditions.get(i), null);
            return sql.toString();
        }
        return null;
    }

    public static StringBuffer addCondition(StringBuffer sql, PageRequestBean.PageRequestItem condition, String connection) {
        if (StringConstant.LIKE.equalsIgnoreCase(condition.getConnection())) {
            condition.setQueryString(StringUtil.addPercentageSign(condition.getQueryString()));
        } else if (StringConstant.GREATER_STR.equalsIgnoreCase(condition.getConnection())) {
            condition.setConnection(StringConstant.GREATER);
        } else if (StringConstant.LESS_STR.equalsIgnoreCase(condition.getConnection())) {
            condition.setConnection(StringConstant.LESS);
        } else if (StringConstant.NOT_EQUAL_STR.equalsIgnoreCase(condition.getConnection())) {
            condition.setConnection(StringConstant.NOT_EQUAL);
        }
        String conditionConnection = condition.getConnection();
        sql.append(condition.getQuery())
                .append(" ").append(conditionConnection).append(" ");
        if (conditionConnection.equalsIgnoreCase(StringConstant.IN)) {
            sql.append("(").append(condition.getQueryString()).append(")");
        } else {
            sql.append("\'").append(condition.getQueryString()).append("\'");
        }
        if (!StringUtils.isEmpty(connection)) {
            sql.append(" ").append(connection).append(" ");
        }
        return sql;
    }

    /**
     * 增加条件
     *
     * @return
     */
    public static String addConditon(String condition, String key, String connection, String value) {
        if (StringUtils.isEmpty(condition)) {
            return key + " " + connection + " " + value;
        } else {
            return condition + " AND " + key + " " + connection + " " + value;
        }
    }

    /**
     * 在TeamIds找含有用户所属的TeamId的
     */
    public static String addTeamIdsConditions(String conditions, String userTeamIds) {
        StringBuffer sql = new StringBuffer();
        if (StringUtils.isEmpty(conditions)) {
            sql.append(" ").append("FIND_IN_SET(");
        } else {
            sql.append(" ").append(conditions).append(" and ").append("FIND_IN_SET(");
        }
        if (StringUtils.isEmpty(userTeamIds)) {
            throw new RuntimeException("user not hava userTeamIds");
        }
        List<String> teamIdList = Arrays.asList(userTeamIds.split(","));
        String teamIdOrStr = String.join(" OR ", teamIdList);
        sql.append(teamIdOrStr).append(",").append(TableConstant.USER.TEAM_IDS).append(") ");
        return sql.toString();
    }

    public static String addLimitCondition(String condition, Integer page, Integer count) {
        return condition + " LIMIT " + page * count + "," + count;
    }

    public static Boolean isUpdateOperation(Long id) {
        return id != null && id > 0;
    }
}