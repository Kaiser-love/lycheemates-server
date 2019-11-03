package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.wdy.cheemate.common.constant.StateConstant;
import com.wdy.cheemate.common.constant.WebSocketMessageConstant;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestUtil;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.entity.SingleChatMessage;
import com.wdy.cheemate.entity.output.WebSocketMessage;
import com.wdy.cheemate.mapper.SingleChatMessageMapper;
import com.wdy.cheemate.service.ISingleChatMessageService;
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
public class SingleChatMessageServiceImpl extends ServiceImpl<SingleChatMessageMapper, SingleChatMessage> implements ISingleChatMessageService {
    private SingleChatMessageMapper singleChatMessageMapper;

    @Override
    public ResponseEntity<ResultBean> putSingleChatMessage(SingleChatMessage singleChatMessage) {
        if (!SqlUtil.isUpdateOperation(singleChatMessage.getId())) {
            // 新建
            singleChatMessage.setSendTime(TimeUtil.currentTimeStamp());
            singleChatMessage.setSendUserId(SecureUtil.getDataBaseUserId());
            singleChatMessage.setState(StateConstant.Invitation.WAIT_PUBLISH);
            // 推送邀请信息
            return ResponseHelper.OK(sendMessage(singleChatMessage));
        }
        singleChatMessage.insertOrUpdate();
        return ResponseHelper.OK(singleChatMessage);

    }

    @Override
    public SingleChatMessage sendMessage(SingleChatMessage singleChatMessage) {
        WebSocketMessage webSocketMessage = WebSocketMessage.builder().type(WebSocketMessageConstant.SINGLE_CHAT_MESSAGE).jsonString(JSON.toJSONString(singleChatMessage)).build();
        Boolean flag = WsSessionManager.sendMessage(singleChatMessage.getReceiveUserId(), JSON.toJSONString(webSocketMessage));
        if (flag) {
            singleChatMessage.setState(StateConstant.Invitation.WAIT_ACCEPT);
            singleChatMessage.insertOrUpdate();
        }
        return singleChatMessage;
    }

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Long receiveId) {
        String conditionString = RequestUtil.getOrDefaultPageBeanByReceiveId(pageRequestBean, receiveId);
        List<SingleChatMessage> resultList = singleChatMessageMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        Integer pageAllSize = singleChatMessageMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }
}
