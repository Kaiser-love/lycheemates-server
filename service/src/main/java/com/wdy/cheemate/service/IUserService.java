package com.wdy.cheemate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.dto.WxUser;
import com.wdy.cheemate.entity.User;
import com.wdy.cheemate.entity.output.UserOutputVo;
import org.springframework.http.ResponseEntity;

public interface IUserService extends IService<User> {

    ResponseEntity<ResultBean> userRegistry(WxUser wxUser);

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean);

    PageListAndConditions getUser(PageRequestBean pageRequestBean);

    ResponseEntity<ResultBean> getUserRankings(PageRequestBean pageRequestBean, Integer type);

    ResponseEntity<ResultBean> getConferenceCollection(String userId, String query, String connection, String queryString, Integer page, Integer count, Integer mode);

    UserOutputVo fillUser(User user);
}
