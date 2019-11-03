package com.wdy.cheemate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.request.RequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.dto.PageListAndConditions;
import com.wdy.cheemate.entity.UserFriend;
import com.wdy.cheemate.entity.output.UserFriendOutputVo;
import org.springframework.http.ResponseEntity;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:26
 * @description:
 */
public interface IUserFriendService extends IService<UserFriend> {
    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean);

    PageListAndConditions getUserFriend(PageRequestBean pageRequestBean);

    ResponseEntity<ResultBean> putOrRemoveFriend(RequestBean requestBean, Integer type, String content, Long userAddFriendMessageId);

    UserFriendOutputVo fillUserFriend(UserFriend userFriend);
}