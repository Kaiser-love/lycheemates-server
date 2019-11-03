package com.wdy.cheemate.controller;


import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.UserLearnInvitation;
import com.wdy.cheemate.service.IUserLearnInvitationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@RestController
@Api(tags = "用户学习邀请接口", description = "用户学习邀请API")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@ApiIgnore
public class UserLearnInvitationController {
    private IUserLearnInvitationService userLearnInvitationService;

    @PostMapping("/userLearnInvitations/{type}")
    @ApiOperation(value = "获取用户学习邀请列表", notes = "type:0发出的1接收到的,可以设置state状态")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean, @PathVariable Integer type) {
        return userLearnInvitationService.listByPage(pageRequestBean, type);
    }

    @PutMapping("/userLearnInvitation")
    @ApiOperation(value = "向指定用户发出学习邀请(添加或修改)")
    public ResponseEntity<ResultBean> putUserLearnInvitation(@RequestBody UserLearnInvitation userLearnInvitation) {
        return userLearnInvitationService.putUserLearnInvitation(userLearnInvitation);
    }

    @PutMapping("/userLearnInvitation/sign/{id}")
    @ApiOperation(value = "对指定的学习邀请进行签到开始")
    public ResponseEntity<ResultBean> putUserLearnInvitationSign(@PathVariable Long id) {
        return userLearnInvitationService.putUserLearnInvitationSign(id);
    }
}

