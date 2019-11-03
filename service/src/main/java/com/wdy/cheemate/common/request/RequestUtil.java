package com.wdy.cheemate.common.request;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.util.*;

import java.util.*;

/**
 * @description: 请求工具类
 * @author: dongyang_wu
 * @create: 2019-08-01 12:38
 */
public class RequestUtil {

    public static boolean isInValidParameter(Integer min, Integer max, Integer... list) {
        for (Integer args : list) {
            if (args < min || args > max)
                return true;
        }
        return false;
    }

    public static void setDefaultPageBean(PageRequestBean pageRequestBean) {
        if (pageRequestBean == null) {
            pageRequestBean = new PageRequestBean();
        }
        pageRequestBean.setPage(Optional.ofNullable(pageRequestBean.getPage()).orElse(0));
        pageRequestBean.setCount(Optional.ofNullable(pageRequestBean.getCount()).orElse(5));
        pageRequestBean.setOrderBy(Optional.ofNullable(pageRequestBean.getOrderBy()).orElse(TableConstant.ID));
        pageRequestBean.setSortType(Optional.ofNullable(pageRequestBean.getSortType()).orElse(StringConstant.DESC));
        if (!CollectionUtils.isEmpty(pageRequestBean.getConditions()) && StringUtils.isEmpty(pageRequestBean.getConditionConnection()))
            pageRequestBean.setConditionConnection(StringConstant.AND);
    }

    public static String getDefaultPageBeanByUserId(PageRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        pageRequestBean.setConditionConnection(StringConstant.AND);
        pageRequestBean.setConditions(Collections.singletonList(PageRequestBean.PageRequestItem.builder().query(TableConstant.USER_TO_FOCUS.USER_ID).connection(StringConstant.EQUALS).queryString(SecureUtil.getDataBaseUserId().toString()).build()));
        return SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
    }

    public static String getDefaultPageBeanByKey(PageRequestBean pageRequestBean, String key) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        pageRequestBean.setConditionConnection(StringConstant.AND);
        pageRequestBean.setConditions(Collections.singletonList(PageRequestBean.PageRequestItem.builder().query(key).connection(StringConstant.EQUALS).queryString(SecureUtil.getDataBaseUserId().toString()).build()));
        return SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
    }

    public static String getDefaultPageBeanByKey(PageRequestBean pageRequestBean, Integer type) {
        String key = TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID;
        if (type.equals(Constant.NumberType.ZERO_PROPERTY))
            key = TableConstant.TABLE_COMMON_COLUMN.SEND_USER_ID;
//        RequestUtil.setDefaultPageBean(pageRequestBean);
//        pageRequestBean.setConditionConnection(StringConstant.AND);
//        pageRequestBean.setConditions(Collections.singletonList(PageRequestBean.PageRequestItem.builder().query(key).connection(StringConstant.EQUALS).queryString(SecureUtil.getDataBaseUserId().toString()).build()));
        setDefaultPageBean(pageRequestBean);
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        conditionString = SqlUtil.addConditon(conditionString, key, StringConstant.EQUALS, SecureUtil.getDataBaseUserId().toString());
        return conditionString;
    }

    public static String getOrDefaultPageBeanByReceiveId(PageRequestBean pageRequestBean, Long receiveId) {
        String dataBaseUserId = SecureUtil.getDataBaseUserId().toString();
        RequestUtil.setDefaultPageBean(pageRequestBean);
        pageRequestBean.setConditionConnection(StringConstant.AND + " " + StringConstant.OR + " " + StringConstant.AND);
        pageRequestBean.setConditions(Arrays.asList(
                PageRequestBean.PageRequestItem.builder().query(TableConstant.TABLE_COMMON_COLUMN.SEND_USER_ID).connection(StringConstant.EQUALS).queryString(dataBaseUserId).build(),
                PageRequestBean.PageRequestItem.builder().query(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID).connection(StringConstant.EQUALS).queryString(receiveId.toString()).build(),
                PageRequestBean.PageRequestItem.builder().query(TableConstant.TABLE_COMMON_COLUMN.SEND_USER_ID).connection(StringConstant.EQUALS).queryString(receiveId.toString()).build(),
                PageRequestBean.PageRequestItem.builder().query(TableConstant.TABLE_COMMON_COLUMN.RECEIVE_USER_ID).connection(StringConstant.EQUALS).queryString(dataBaseUserId).build()));
        pageRequestBean.setOrderBy(TableConstant.TABLE_COMMON_COLUMN.SEND_TIME);
        pageRequestBean.setSortType(StringConstant.DESC);
        return SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
    }
}