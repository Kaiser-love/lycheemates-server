package com.wdy.cheemate.service;

import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.dto.WxUser;
import org.springframework.http.ResponseEntity;

/**
 * @program: AuthService
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-30 22:39
 */
public interface AuthService {

    ResponseEntity<ResultBean> wxLoginByCode(String code);

    ResponseEntity<ResultBean> wxLoginByCodeAndRegistry(String code, WxUser wxUser);

    ResponseEntity<ResultBean> token(String userName, String password);

}
