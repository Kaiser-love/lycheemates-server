package com.wdy.cheemate.controller;

import com.alibaba.fastjson.JSONObject;
import com.wdy.cheemate.annotation.Pass;
import com.wdy.cheemate.common.constant.Constant;
import com.wdy.cheemate.common.constant.WxConstant;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestBean;
import com.wdy.cheemate.common.response.ResponseHelper;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.common.util.api.WxApi;
import com.wdy.cheemate.dto.WxUser;
import com.wdy.cheemate.entity.input.UserPerfectInputVo;
import com.wdy.cheemate.entity.*;
import com.wdy.cheemate.service.*;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-19 13:14
 */
@RestController
@Api(tags = "用户接口", description = "用户API")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class UserController {
    private IUserService userService;
    private IUserToFocusService userToFocusService;
    private IUserFriendService userFriendService;
    private IUserFriendAddMessageService userFriendAddMessageService;
    private ZimgServiceUtil zimgServiceUtil;
    private WxApi wxApi;

    @PostMapping("/users/ranking/{type}")
    @ApiOperation(value = "获取用户排行榜列表", notes = "type:0好友1全服务器")
    public ResponseEntity<ResultBean> getUserRankings(@RequestBody PageRequestBean pageRequestBean, @PathVariable Integer type) {
        return userService.getUserRankings(pageRequestBean, type);
    }

    @PostMapping("/users")
    @ApiOperation(value = "获取用户列表")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        JSONObject dataJson = new JSONObject();
        JSONObject sonDateJson = new JSONObject();
        sonDateJson.put("value", "Java程序设计");
        dataJson.put("thing1", sonDateJson);
        JSONObject sonDateJson1 = new JSONObject();
        sonDateJson1.put("value", "2019-10-05");
        dataJson.put("time5", sonDateJson1);
        wxApi.sendTemplate(WxConstant.MESSAGE.URL,"oYzeI5H74UNgUql7AZ7tty5NyXLE", "49JnAR7Gb9qw_39genKrSR7ImiK31kBkFHH0WgtNPPY", dataJson);
        return userService.listByPage(pageRequestBean);
    }

    @ApiOperation(value = "获取当前用户信息")
    @GetMapping("/user/currentUser")
    public ResponseEntity<ResultBean> getUser() {
        User user = SecureUtil.getDataBaseUser();
        return ResponseHelper.OK(userService.fillUser(user));
    }

    @PostMapping("/user/registry")
    @ApiOperation(value = "微信用户注册")
    @ApiIgnore
    @Pass
    public ResponseEntity<ResultBean> registry(@RequestBody WxUser wxUser) {
        return userService.userRegistry(wxUser);
    }

    @PostMapping("/user/focusList/{type}")
    @ApiOperation(value = "获取用户关注列表或粉丝列表", notes = "type:0用户关注列表1粉丝列表")
    public ResponseEntity<ResultBean> getFocusList(@RequestBody PageRequestBean pageRequestBean, @PathVariable Integer type) {
        return userToFocusService.listByPage(pageRequestBean, type);
    }

    @PostMapping("/user/friendList")
    @ApiOperation(value = "获取用户好友列表")
    public ResponseEntity<ResultBean> getFriendList(@RequestBody PageRequestBean pageRequestBean) {
        return userFriendService.listByPage(pageRequestBean);
    }

    @PutMapping("/user/focus/{type}")
    @ApiOperation(value = "关注或取消关注用户", notes = "type:0取消关注1关注")
    public ResponseEntity<ResultBean> putFocus(@RequestBody RequestBean requestBean, @PathVariable Integer type) {
        return userToFocusService.putOrRemoveFocus(requestBean, type);
    }

    @PutMapping("/user/addFriend/{type}")
    @ApiOperation(value = "删除好友、发送添加好友请求或接收好友邀请", notes = "type:0删除好友1发送添加好友请求2接受添加好友请求,content:添加好友内容(type为1时需要),userAddFriendMessageId:消息主键(接受好友添加时需要)")
    public ResponseEntity<ResultBean> addFriend(@RequestBody RequestBean requestBean, @PathVariable Integer type, @RequestParam(required = false) String content, @RequestParam(required = false) Long userAddFriendMessageId) {
        return userFriendService.putOrRemoveFriend(requestBean, type, content, userAddFriendMessageId);
    }

    @PostMapping("/user/addFriendMessages")
    @ApiOperation(value = "获取用户添加好友申请列表", notes = "type:0发出的1接收到的")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean, @PathVariable Integer type) {
        return userFriendAddMessageService.listByPage(pageRequestBean, type);
    }

    @PostMapping("/user/perfectInfo")
    @ApiOperation(value = "完善当前用户信息", notes = "传空则置空 不传则不变 传则修改 birthday样例为2019-07-14 20:41:12 account,password慎传")
    public ResponseEntity<ResultBean> requestHeadByPhone(@RequestBody UserPerfectInputVo userPerfectInputVo) {
        User dataBaseUser = SecureUtil.getDataBaseUser();
        BeanUtils.copyProperties(userPerfectInputVo, dataBaseUser);
        // 未认证
        if (Constant.NumberType.BAD_PROPERTY.equals(dataBaseUser.getStatus()))
            dataBaseUser.setStatus(Constant.NumberType.BAD_PROPERTY);
        if (!StringUtils.isEmpty(dataBaseUser.getSchoolCardUrl())) {
            dataBaseUser.setSchoolCardUrl(String.format(zimgServiceUtil.getZimgTemp(), dataBaseUser.getSchoolCardUrl()));
        }
        boolean b = userService.updateById(dataBaseUser);
        return ResponseHelper.BooleanResultBean(dataBaseUser, "修改失败", b);
    }

    @PostMapping("/user/schoolVerify")
    @ApiOperation(value = "学生身份认证接口")
    public ResponseEntity<ResultBean> schoolVerify(@RequestParam(required = false) String grade, @RequestParam(required = false) String location, @RequestParam(required = false) String major, @RequestParam("schoolCard") MultipartFile schoolCard) {
        String url = String.format(zimgServiceUtil.getZimgTemp(), zimgServiceUtil.sendPost(schoolCard));
        User dataBaseUser = SecureUtil.getDataBaseUser();
        dataBaseUser.setSchoolCardUrl(url);
        if (!StringUtils.isEmpty(grade))
            dataBaseUser.setGrade(grade);
        if (!StringUtils.isEmpty(location))
            dataBaseUser.setLocation(location);
        if (!StringUtils.isEmpty(major))
            dataBaseUser.setMajor(major);
        boolean b = dataBaseUser.updateById();
        return ResponseHelper.BooleanResultBean(dataBaseUser, "上传认证信息失败", b);
    }

}