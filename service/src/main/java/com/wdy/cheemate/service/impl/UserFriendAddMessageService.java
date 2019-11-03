package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdy.cheemate.common.constant.StateConstant;
import com.wdy.cheemate.common.constant.WebSocketMessageConstant;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.BeanUtil;
import com.wdy.cheemate.entity.UserFriendAddMessage;
import com.wdy.cheemate.entity.output.*;
import com.wdy.cheemate.mapper.UserFriendAddMessageMapper;
import com.wdy.cheemate.mapper.UserMapper;
import com.wdy.cheemate.service.IUserChallengeInvitationService;
import com.wdy.cheemate.service.IUserFriendAddMessageService;
import com.wdy.cheemate.websocket.WsSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:43
 * @description:
 */
@Service
@AllArgsConstructor
public class UserFriendAddMessageService extends ServiceImpl<UserFriendAddMessageMapper, UserFriendAddMessage> implements IUserFriendAddMessageService {
    private IUserChallengeInvitationService userChallengeInvitationService;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type) {
        return userChallengeInvitationService.listByPage(pageRequestBean, type);
    }

    @Override
    public UserFriendAddMessageOutputVo sendMessage(UserFriendAddMessage userFriendAddMessage) {
        WebSocketMessage webSocketMessage = WebSocketMessage.builder().type(WebSocketMessageConstant.USER_FRIEND_ADD_MESSAGE).jsonString(JSON.toJSONString(userFriendAddMessage)).build();
        Boolean flag = WsSessionManager.sendMessage(userFriendAddMessage.getReceiveUserId(), JSON.toJSONString(webSocketMessage));
        if (flag) {
            userFriendAddMessage.setState(StateConstant.Invitation.WAIT_ACCEPT);
            userFriendAddMessage.insertOrUpdate();
        }
        return fillUserFriendAddMessage(userFriendAddMessage);
    }

    @Override
    public UserFriendAddMessageOutputVo fillUserFriendAddMessage(UserFriendAddMessage userFriendAddMessage) {
        UserFriendAddMessageOutputVo vo = BeanUtil.copy(userFriendAddMessage, UserFriendAddMessageOutputVo.class);
        vo.setSendUser(userMapper.selectById(userFriendAddMessage.getSendUserId()));
        vo.setReceiveUser(userMapper.selectById(userFriendAddMessage.getReceiveUserId()));
        return vo;
    }
}