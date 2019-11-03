package com.wdy.cheemate.controller;


import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.SingleChatMessage;
import com.wdy.cheemate.service.ISingleChatMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@RestController
@Api(tags = "私聊接口", description = "私聊API")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class SingleChatMessageController {
    private ISingleChatMessageService singleChatMessageService;

    @PutMapping("/singleChatMessage")
    @ApiOperation(value = "向指定用户发送消息")
    public ResponseEntity<ResultBean> putSingleChatMessage(@RequestBody SingleChatMessage singleChatMessage) {
        return singleChatMessageService.putSingleChatMessage(singleChatMessage);
    }

    @PutMapping("/singleChatMessages/{receiveId}")
    @ApiOperation(value = "获取和指定用户的聊天记录")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean, @PathVariable Long receiveId) {
        return singleChatMessageService.listByPage(pageRequestBean, receiveId);
    }

}

