package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.wdy.cheemate.common.constant.*;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestUtil;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.entity.*;
import com.wdy.cheemate.mapper.UserLearnInvitationMapper;
import com.wdy.cheemate.service.IUserLearnInvitationService;
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
public class UserLearnInvitationServiceImpl extends ServiceImpl<UserLearnInvitationMapper, UserLearnInvitation> implements IUserLearnInvitationService {
    private UserLearnInvitationMapper userLearnInvitationMapper;

    @Override
    public ResponseEntity<ResultBean> putUserLearnInvitation(UserLearnInvitation userLearnInvitation) {
        if (!SqlUtil.isUpdateOperation(userLearnInvitation.getId())) {
            // 新建
            userLearnInvitation.setSendTime(TimeUtil.currentTimeStamp());
            userLearnInvitation.setSendUserId(SecureUtil.getDataBaseUserId());
            userLearnInvitation.setState(StateConstant.Invitation.WAIT_PUBLISH);
            // 推送邀请信息
            return ResponseHelper.OK(sendMessage(userLearnInvitation));
        }
        userLearnInvitation.insertOrUpdate();
        return ResponseHelper.OK(userLearnInvitation);
    }

    @Override
    public UserLearnInvitation sendMessage(UserLearnInvitation userLearnInvitation) {
        Boolean flag = WsSessionManager.sendMessage(userLearnInvitation.getReceiveUserId(), JSON.toJSONString(userLearnInvitation));
        if (flag) {
            userLearnInvitation.setState(StateConstant.Invitation.WAIT_ACCEPT);
            userLearnInvitation.insertOrUpdate();
        }
        return userLearnInvitation;
    }

    @Override
    public ResponseEntity<ResultBean> putUserLearnInvitationSign(Long id) {
        UserLearnInvitation userLearnInvitation = getBaseMapper().selectById(id);
        if (SecureUtil.getDataBaseUserId().equals(userLearnInvitation.getSendUserId())) {
            userLearnInvitation.setSendSignTime(TimeUtil.currentTimeStamp());
        } else if (SecureUtil.getDataBaseUserId().equals(userLearnInvitation.getReceiveUserId())) {
            userLearnInvitation.setReceiveSignTime(TimeUtil.currentTimeStamp());
        }
        // 判断是否发送人和接收人同时在线且满足开始条件
        if ((userLearnInvitation.getSendSignTime() != null && userLearnInvitation.getReceiveSignTime() != null) && WsSessionManager.isAliveUser(userLearnInvitation.getSendUserId()) && WsSessionManager.isAliveUser(userLearnInvitation.getReceiveUserId())) {
            // 发送开始消息
            Boolean sendMessageFlag = WsSessionManager.sendMessage(userLearnInvitation.getSendUserId(), JSON.toJSONString(userLearnInvitation));
            Boolean receiveMessageFlag = WsSessionManager.sendMessage(userLearnInvitation.getReceiveUserId(), JSON.toJSONString(userLearnInvitation));
            if (sendMessageFlag && receiveMessageFlag) {
                userLearnInvitation.setState(StateConstant.Invitation.IS_RUNNING);
            }
        }
        userLearnInvitation.insertOrUpdate();
        return ResponseHelper.OK(userLearnInvitation);
    }

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type) {
        String conditionString = RequestUtil.getDefaultPageBeanByKey(pageRequestBean, type);
        List<UserLearnInvitation> resultList = userLearnInvitationMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        Integer pageAllSize = userLearnInvitationMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }
}
