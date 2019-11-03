package com.wdy.cheemate.common.util.http;

import com.wdy.cheemate.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @auther: dongyang_wu
 * @date: 2019/4/1 15:31
 * @description:
 */
@Component("HttpHelper")
public class HttpHelper {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;
    private final String APP_ID = "wx7637a2b4e72d71da";
    private final String APP_SECRET = "695065d05d26073b6d37ba4b9003869e";
    // http://123.206.73.65:8001/upload/
    @Value("${zimg.djangoUrl}")
    private String djangoUrl = "12313";

    public ResponseEntity get(String url, Map headers, Map<String, Object> paramMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity result;
        if (paramMapArgs != null)
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMapArgs);
        else
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return result;
    }

    public ResponseEntity post(String url, Map headers, Map<String, Object> bodyMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.setAll(bodyMapArgs);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
        ResponseEntity result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        return result;
    }

    public String postForObject(String url, String s) {
        //headers
//        HttpHeaders requestHeaders = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
//        requestHeaders.setContentType(type);
//        HttpEntity<String> requestEntity = new HttpEntity<>(s, requestHeaders);
        String apiResult = restTemplate.postForObject(url, s, String.class);
        return apiResult;
    }

    public ResponseEntity postWithResultString(String url, Map headers, Map<String, Object> bodyMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.setAll(bodyMapArgs);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
        ResponseEntity result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return result;
    }

    public ResponseEntity wxLogin(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID + "&secret=" + APP_SECRET + "&grant_type=authorization_code&js_code=" + code;
        ResponseEntity responseEntity = get(url, null, null);
        System.out.println(responseEntity.getBody().toString());
        return responseEntity;
    }

    public String getDjangoUrl() {
        return djangoUrl;
    }
}
