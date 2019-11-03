package com.wdy.cheemate.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wdy.cheemate.common.constant.TokenConstant;
import com.wdy.cheemate.common.exception.ApiResultEnum;
import com.wdy.cheemate.common.exception.ApiException;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.common.util.*;
import com.wdy.cheemate.common.util.http.HttpHelper;
import com.wdy.cheemate.context.ContextUtil;
import com.wdy.cheemate.dto.WxUser;
import com.wdy.cheemate.entity.User;
import com.wdy.cheemate.service.AuthService;
import com.wdy.cheemate.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-30 22:39
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private HttpHelper httpHelper;
    @Autowired
    private IUserService userService;

    @Override
    public ResponseEntity<ResultBean> wxLoginByCode(String code) {
        ResponseEntity responseEntity = httpHelper.wxLogin(code);
        JSONObject apiResult = JSON.parseObject(responseEntity.getBody().toString());
        String session_key = (String) apiResult.getOrDefault("session_key", "");
        String openid = (String) apiResult.getOrDefault("openid", "");
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(session_key)) {
            throw new ApiException(ApiResultEnum.WX_USER_OPENID_GET_ERROR);
        }
        User user = User.builder().build().selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (user == null || StringUtils.isEmpty(user.getId())) {
            throw new ApiException(ApiResultEnum.WX_USER_OPENID_NOT_EXIST.getCode(), ApiResultEnum.WX_USER_OPENID_NOT_EXIST.getMessage() + "-" + openid);
        }
        return getAuthInfoFromUser(user);
    }

    @Override
    public ResponseEntity<ResultBean> wxLoginByCodeAndRegistry(String code, WxUser wxUser) {
        ResponseEntity responseEntity = httpHelper.wxLogin(code);
        JSONObject apiResult = JSON.parseObject(responseEntity.getBody().toString());
        String session_key = (String) apiResult.getOrDefault("session_key", "");
        String openid = (String) apiResult.getOrDefault("openid", "");
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(session_key)) {
            throw new ApiException(ApiResultEnum.WX_USER_OPENID_GET_ERROR);
        }
        User user = User.builder().build().selectOne(new QueryWrapper<User>().eq("openid", openid));
        if (user == null || StringUtils.isEmpty(user.getId())) {
            wxUser.setApiResult(apiResult);
            userService.userRegistry(wxUser);
            user = User.builder().build().selectOne(new QueryWrapper<User>().eq("openid", openid));
            if (user == null) {
                throw new ApiException(ApiResultEnum.WX_USER_REGISTRY_ERROR);
            }
        }
        return getAuthInfoFromUser(user);
    }

    @Override
    public ResponseEntity<ResultBean> token(String userName, String password) {
        User user = User.builder().build().selectOne(new QueryWrapper<User>().eq("account", userName).eq("password", DigestUtil.encrypt(password)));
        if (user == null)
            throw new ApiException(ApiResultEnum.ENTITY_NOT_EXIST);
        return getAuthInfoFromUser(user);
    }


    public ResponseEntity<ResultBean> getAuthInfoFromUser(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        //设置jwt参数
        Map<String, String> param = new HashMap<>(16);
        param.put(TokenConstant.USER_ID, Func.toStr(user.getId()));
//        param.put(TokenConstant.ROLE_ID, user.get());
        param.put(TokenConstant.TENANT_CODE, "000000");
        param.put(TokenConstant.ACCOUNT, user.getAccount());
        param.put(TokenConstant.USER_NAME, user.getRealName());
//        param.put(TokenConstant.ROLE_NAME, Func.join(res.getData().getRoles()));
        //拼装accessToken
        String accessToken = SecureUtil.createJWT(param, "audience", "issuser", true);
        responseHeaders.set(ContextUtil.AUTHORIZATION, accessToken);
        responseHeaders.set("Access-Control-Expose-Headers", ContextUtil.AUTHORIZATION);
        return new ResponseEntity<>(ResultBean.success(user), responseHeaders, HttpStatus.OK);
    }
}
