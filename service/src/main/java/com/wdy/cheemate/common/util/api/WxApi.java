package com.wdy.cheemate.common.util.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdy.cheemate.common.constant.RedisConstant;
import com.wdy.cheemate.common.util.SecureUtil;
import com.wdy.cheemate.common.util.http.HttpHelper;
import com.wdy.cheemate.redis.RedisUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 08:54
 * @description:
 */
@Component("WxApi")
public class WxApi {
    private final RedisUtil redisUtil;
    private final HttpHelper httpHelper;
    private final String APP_ID = "wx7637a2b4e72d71da";
    private final String APP_SECRET = "695065d05d26073b6d37ba4b9003869e";

    public WxApi(HttpHelper httpHelper, RedisUtil redisUtil) {
        this.httpHelper = httpHelper;
        this.redisUtil = redisUtil;
    }

    private String getAccessToken() {
        String cacheKey = String.format(RedisConstant.ACCESSTOKEN_PREFIX, SecureUtil.getDataBaseUserId());
        String accessToken = (String) redisUtil.get(cacheKey);
        if (!StringUtils.isEmpty(accessToken)) {
            return accessToken;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?appid=" + APP_ID + "&secret=" + APP_SECRET + "&grant_type=client_credential";
        ResponseEntity responseEntity = httpHelper.get(url, null, null);
        JSONObject apiResult = JSON.parseObject(responseEntity.getBody().toString());
        System.out.println(responseEntity.getBody().toString());
        redisUtil.set(cacheKey, apiResult.get("access_token"), (Integer) apiResult.get("expires_in"));
        return (String) apiResult.get("access_token");
    }

    public ResponseEntity sendTemplateMessage(Map<String, Object> bodyMapArgs) {
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        ResponseEntity responseEntity = httpHelper.post(url, null, bodyMapArgs);
//        System.out.println(responseEntity);
//        JSONObject data = JSON.parseObject(apiResult).getJSONObject("data");
        return responseEntity;
    }

    /**
     * 发送微信消息模板
     */
    public String sendTemplate(String url, String toUser, String templateId, JSONObject dataJson) {
        String accessToken = getAccessToken();
        url = url + accessToken;
        // 装配post请求参数
        JSONObject json = new JSONObject();
        json.put("touser", toUser);
        json.put("template_id", templateId);
        json.put("data", dataJson);
        json.put("page", "index");
        String resultStr = "发送失败";
        try {
            String apiResult = httpHelper.postForObject(url, json.toJSONString());
            Integer errcode = JSON.parseObject(apiResult).getInteger("errcode");
            System.out.println(apiResult);
            if (errcode == 0) {
                // 发送成功
                if (errcode.equals("40037")) {
                    resultStr = "template_id不正确";
                }
                if (errcode.equals("41028")) {
                    resultStr = "form_id不正确，或者过期";
                }
                if (errcode.equals("41029")) {
                    resultStr = "form_id已被使用";
                }
                if (errcode.equals("41030")) {
                    resultStr = "page不正确";
                }
                if (errcode.equals("45009")) {
                    resultStr = "接口调用超过限额（目前默认每个帐号日调用限额为100万）";
                }
                resultStr = "ok";
                return resultStr;
            }
        } catch (Exception e) {
        }
        return resultStr;
    }
}