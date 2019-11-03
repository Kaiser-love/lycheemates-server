package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestUtil;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.entity.User;
import com.wdy.cheemate.entity.UserChallengeInvitation;
import com.wdy.cheemate.entity.output.*;
import com.wdy.cheemate.mapper.UserChallengeInvitationMapper;
import com.wdy.cheemate.mapper.UserMapper;
import com.wdy.cheemate.service.IUserChallengeInvitationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wdy.cheemate.websocket.WsSessionManager;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@Service
@AllArgsConstructor
public class UserChallengeInvitationServiceImpl extends ServiceImpl<UserChallengeInvitationMapper, UserChallengeInvitation> implements IUserChallengeInvitationService {
    private UserChallengeInvitationMapper userChallengeInvitationMapper;
    private UserMapper userMapper;

    @Override
    public ResponseEntity<ResultBean> putUserChallengeInvitation(UserChallengeInvitation userChallengeInvitation) {
        if (!SqlUtil.isUpdateOperation(userChallengeInvitation.getId())) {
            userChallengeInvitation.setSendTime(TimeUtil.currentTimeStamp());
            userChallengeInvitation.setSendUserId(SecureUtil.getDataBaseUserId());
            userChallengeInvitation.setState(StateConstant.Invitation.WAIT_PUBLISH);
            return ResponseHelper.OK(sendMessage(userChallengeInvitation));
        }
        userChallengeInvitation.insertOrUpdate();
        return ResponseHelper.OK(userChallengeInvitation);
    }


    @Override
    public UserChallengeInvitationOutputVo sendMessage(UserChallengeInvitation userChallengeInvitation) {
        Boolean flag = WsSessionManager.sendMessage(userChallengeInvitation.getReceiveUserId(), JSON.toJSONString(userChallengeInvitation));
        if (flag) {
            userChallengeInvitation.setState(StateConstant.Invitation.WAIT_ACCEPT);
            userChallengeInvitation.insertOrUpdate();
        }
        return fillUserChallengeInvitation(userChallengeInvitation);
    }

    @Override
    public UserChallengeInvitationOutputVo fillUserChallengeInvitation(UserChallengeInvitation userChallengeInvitation) {
        UserChallengeInvitationOutputVo vo = BeanUtil.copy(userChallengeInvitation, UserChallengeInvitationOutputVo.class);
        vo.setSendUser(userMapper.selectById(userChallengeInvitation.getSendUserId()));
        vo.setReceiveUser(userMapper.selectById(userChallengeInvitation.getReceiveUserId()));
        return vo;
    }

    @Override
    public ResponseEntity<ResultBean> putUserChallengeInvitationSign(Long id, Integer type) {
        UserChallengeInvitation userChallengeInvitation = userChallengeInvitationMapper.selectById(id);
        // 0开始
        if (type.equals(Constant.NumberType.ZERO_PROPERTY)) {
            if (SecureUtil.getDataBaseUserId().equals(userChallengeInvitation.getSendUserId())) {
                userChallengeInvitation.setSendSignTime(TimeUtil.currentTimeStamp());
            } else if (SecureUtil.getDataBaseUserId().equals(userChallengeInvitation.getReceiveUserId())) {
                userChallengeInvitation.setReceiveSignTime(TimeUtil.currentTimeStamp());
            }
            // 判断是否发送人和接收人同时在线且满足开始条件
//            if ((userChallengeInvitation.getSendSignTime() != null && userChallengeInvitation.getReceiveSignTime() != null) && WebSocketServer.isAliveUser(userChallengeInvitation.getSendUserId()) && WebSocketServer.isAliveUser(userChallengeInvitation.getReceiveUserId())) {
//                String jsonString = WebSocketMessage.builder().type(WebSocketMessageConstant.USER_CHALLENGE_INVITATION_BEGIN).jsonString(JSON.toJSONString(userChallengeInvitation)).build().getJsonString();
//                // 发送开始消息
//                Boolean sendMessageFlag = WebSocketServer.sendMessage(userChallengeInvitation.getSendUserId(), jsonString);
//                Boolean receiveMessageFlag = WebSocketServer.sendMessage(userChallengeInvitation.getReceiveUserId(), jsonString);
//                if (sendMessageFlag && receiveMessageFlag)
//                    userChallengeInvitation.setState(StateConstant.Invitation.IS_RUNNING);
//            }
            userChallengeInvitation.setState(StateConstant.Invitation.IS_RUNNING);
            userChallengeInvitation.insertOrUpdate();
        }
        // 1中途结束
        else if (type.equals(Constant.NumberType.ONE_PROPERTY)) {
            User sendUser = SecureUtil.getDataBaseUser();
            User receiveUser = userMapper.selectById(userChallengeInvitation.getReceiveUserId());
            if (sendUser.getId().equals(userChallengeInvitation.getSendUserId())) {
                sendUser.setLycheeNumber(sendUser.getLycheeNumber() - userChallengeInvitation.getLycheeNumber());
                receiveUser.setLycheeNumber(receiveUser.getLycheeNumber() + userChallengeInvitation.getLycheeNumber());
                userChallengeInvitation.setSendFinishTime(TimeUtil.currentTimeStamp());
            } else if (sendUser.getId().equals(userChallengeInvitation.getReceiveUserId())) {
                sendUser.setLycheeNumber(sendUser.getLycheeNumber() + userChallengeInvitation.getLycheeNumber());
                receiveUser.setLycheeNumber(receiveUser.getLycheeNumber() - userChallengeInvitation.getLycheeNumber());
                userChallengeInvitation.setReceiveFinishTime(TimeUtil.currentTimeStamp());
            }
            userChallengeInvitation.setState(StateConstant.Invitation.END_INVITATION);
            userChallengeInvitation.insertOrUpdate();
        }
        // 2完成
        else if (type.equals(Constant.NumberType.TWO_PROPERTY)) {
            User sendUser = SecureUtil.getDataBaseUser();
            User receiveUser = userMapper.selectById(userChallengeInvitation.getReceiveUserId());
            if (sendUser.getId().equals(userChallengeInvitation.getSendUserId())) {
                sendUser.setLycheeNumber(sendUser.getLycheeNumber() + userChallengeInvitation.getLycheeNumber());
                userChallengeInvitation.setSendFinishTime(TimeUtil.currentTimeStamp());
            } else if (sendUser.getId().equals(userChallengeInvitation.getReceiveUserId())) {
                receiveUser.setLycheeNumber(receiveUser.getLycheeNumber() + userChallengeInvitation.getLycheeNumber());
                userChallengeInvitation.setReceiveFinishTime(TimeUtil.currentTimeStamp());
            }
            userChallengeInvitation.setState(StateConstant.Invitation.END_INVITATION);
            userChallengeInvitation.insertOrUpdate();
        }
        return ResponseHelper.OK(fillUserChallengeInvitation(userChallengeInvitation));
    }

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type) {
        String conditionString = RequestUtil.getDefaultPageBeanByKey(pageRequestBean, type);
        List<UserChallengeInvitation> resultList = userChallengeInvitationMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        Integer pageAllSize = userChallengeInvitationMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }
}
