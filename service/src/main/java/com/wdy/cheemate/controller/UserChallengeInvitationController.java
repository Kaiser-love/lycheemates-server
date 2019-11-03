package com.wdy.cheemate.controller;


import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.UserChallengeInvitation;
import com.wdy.cheemate.service.IUserChallengeInvitationService;
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
@Api(tags = "用户督局挑战邀请接口", description = "用户督局API")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class UserChallengeInvitationController {
    private IUserChallengeInvitationService userChallengeInvitationService;

    @PostMapping("/userChallengeInvitations/{type}")
    @ApiOperation(value = "获取用户督局挑战邀请列表", notes = "type:0发出的1接收到的")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean, @PathVariable Integer type) {
        return userChallengeInvitationService.listByPage(pageRequestBean, type);
    }

    @PutMapping("/userChallengeInvitation")
    @ApiOperation(value = "向指定用户发出督局邀请(添加或修改)")
    public ResponseEntity<ResultBean> putUserChallengeInvitation(@RequestBody UserChallengeInvitation userChallengeInvitation) {
        return userChallengeInvitationService.putUserChallengeInvitation(userChallengeInvitation);
    }

    @PutMapping("/userChallengeInvitation/sign/{id}/{type}")
    @ApiOperation(value = "对指定的督局进行签到开始或完成", notes = "0开始1中途结束2完成")
    public ResponseEntity<ResultBean> putUserChallengeInvitationSign(@PathVariable Long id, @PathVariable Integer type) {
        return userChallengeInvitationService.putUserChallengeInvitationSign(id, type);
    }
}

