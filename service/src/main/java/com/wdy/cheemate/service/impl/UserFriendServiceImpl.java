package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.request.*;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.entity.*;
import com.wdy.cheemate.entity.output.*;
import com.wdy.cheemate.mapper.*;
import com.wdy.cheemate.service.IUserFriendService;
import com.wdy.cheemate.websocket.WsSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:27
 * @description:
 */
@Service
@AllArgsConstructor
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend> implements IUserFriendService {
    private UserFriendMapper userFriendMapper;
    private UserFriendAddMessageMapper userFriendAddMessageMapper;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean) {
        PageListAndConditions pageListAndConditions = getUserFriend(pageRequestBean);
        List<UserFriend> list = pageListAndConditions.getList();
        List<UserFriendOutputVo> resultList = list.stream().map(this::fillUserFriend).collect(Collectors.toList());
        Integer pageAllSize = userFriendMapper.getPageAllSize(pageListAndConditions.getConditionString());
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @Override
    public PageListAndConditions getUserFriend(PageRequestBean pageRequestBean) {
        String conditionString = RequestUtil.getDefaultPageBeanByUserId(pageRequestBean);
        List<UserFriend> list = userFriendMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        return PageListAndConditions.builder().list(list).conditionString(conditionString).build();
    }

    @Override
    public ResponseEntity<ResultBean> putOrRemoveFriend(RequestBean requestBean, Integer type, String content, Long userAddFriendMessageId) {
        if (type.equals(Constant.NumberType.ZERO_PROPERTY))
            requestBean.getItems().forEach(requestItem -> userFriendMapper.deleteByMap(SqlUtil.map(requestItem.getQuery(), requestItem.getQueryString()).build()));
        else if (type.equals(Constant.NumberType.ONE_PROPERTY)) {
            requestBean.getItems().forEach(requestItem -> {
                UserFriendAddMessage userFriendAddMessage = UserFriendAddMessage.builder().addTime(TimeUtil.currentTimeStamp()).sendUserId(SecureUtil.getDataBaseUserId()).receiveUserId(Long.valueOf(requestItem.getQueryString())).content(content).state(StateConstant.Invitation.WAIT_PUBLISH).build();
                WebSocketMessage webSocketMessage = WebSocketMessage.builder().type(WebSocketMessageConstant.USER_FRIEND_ADD_MESSAGE).jsonString(JSON.toJSONString(userFriendAddMessage)).build();
                // 推送消息
                Boolean publishFlag = WsSessionManager.sendMessage(userFriendAddMessage.getReceiveUserId(), JSON.toJSONString(webSocketMessage));
                if (publishFlag) {
                    userFriendAddMessage.setState(StateConstant.Invitation.WAIT_ACCEPT);
                }
                userFriendAddMessage.insertOrUpdate();
            });
        } else if (type.equals(Constant.NumberType.TWO_PROPERTY)) {
            UserFriendAddMessage userFriendAddMessage = userFriendAddMessageMapper.selectById(userAddFriendMessageId);
            userFriendAddMessage.setState(StateConstant.Invitation.END_INVITATION);
            userFriendAddMessage.insertOrUpdate();
            requestBean.getItems().forEach(requestItem -> UserFriend.builder().addTime(TimeUtil.currentTimeStamp()).userId(SecureUtil.getDataBaseUserId()).friendUserId(Long.valueOf(requestItem.getQueryString())).build().insert());
        }
        return ResponseHelper.OK();
    }

    @Override
    public UserFriendOutputVo fillUserFriend(UserFriend userFriend) {
        UserFriendOutputVo vo = BeanUtil.copy(userFriend, UserFriendOutputVo.class);
        vo.setUser(userMapper.selectById(userFriend.getUserId()));
        vo.setFriendUser(userMapper.selectById(userFriend.getFriendUserId()));
        return vo;
    }
}