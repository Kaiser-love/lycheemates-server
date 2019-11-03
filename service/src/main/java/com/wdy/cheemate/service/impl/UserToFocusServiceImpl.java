package com.wdy.cheemate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.request.*;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.entity.*;
import com.wdy.cheemate.entity.output.UserToFocusOutputVo;
import com.wdy.cheemate.mapper.*;
import com.wdy.cheemate.service.IUserToFocusService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-11 19:39
 */
@Service("UserToFocusService")
@AllArgsConstructor
public class UserToFocusServiceImpl extends ServiceImpl<UserToFocusMapper, UserToFocus> implements IUserToFocusService {
    private UserToFocusMapper userToFocusMapper;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type) {
        PageListAndConditions pageListAndConditions = getUserFocusOrFans(pageRequestBean, type);
        List<UserToFocus> list = pageListAndConditions.getList();
        List<UserToFocusOutputVo> resultList = list.stream().map(this::fillUserToFocus).collect(Collectors.toList());
        Integer pageAllSize = userToFocusMapper.getPageAllSize(pageListAndConditions.getConditionString());
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @Override
    public ResponseEntity<ResultBean> putOrRemoveFocus(RequestBean requestBean, Integer type) {
        if (type.equals(Constant.NumberType.ZERO_PROPERTY))
            requestBean.getItems().forEach(requestItem -> userToFocusMapper.deleteByMap(SqlUtil.map(requestItem.getQuery(), requestItem.getQueryString()).build()));
        else if (type.equals(Constant.NumberType.ONE_PROPERTY))
            requestBean.getItems().forEach(requestItem -> UserToFocus.builder().focusTime(TimeUtil.currentTimeStamp()).userId(SecureUtil.getDataBaseUserId()).focusUserId(Long.valueOf(requestItem.getQueryString())).build().insert());
        return ResponseHelper.OK();
    }

    @Override
    public PageListAndConditions getUserFocusOrFans(PageRequestBean pageRequestBean, Integer type) {
        String key = TableConstant.USER_TO_FOCUS.USER_ID;
        if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            key = TableConstant.USER_TO_FOCUS.FOCUS_USER_ID;
        }
        String conditionString = RequestUtil.getDefaultPageBeanByKey(pageRequestBean, key);
        List<UserToFocus> list = userToFocusMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        return PageListAndConditions.builder().list(list).conditionString(conditionString).build();
    }

    @Override
    public UserToFocusOutputVo fillUserToFocus(UserToFocus userToFocus) {
        UserToFocusOutputVo vo = BeanUtil.copy(userToFocus, UserToFocusOutputVo.class);
        vo.setUser(userMapper.selectById(userToFocus.getUserId()));
        vo.setFocusUser(userMapper.selectById(userToFocus.getFocusUserId()));
        return vo;
    }

}