package com.wdy.cheemate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.UserFriendAddMessage;
import com.wdy.cheemate.entity.output.UserFriendAddMessageOutputVo;
import org.springframework.http.ResponseEntity;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:42
 * @description:
 */
public interface IUserFriendAddMessageService extends IService<UserFriendAddMessage> {

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type);

    UserFriendAddMessageOutputVo sendMessage(UserFriendAddMessage userFriendAddMessage);

    UserFriendAddMessageOutputVo fillUserFriendAddMessage(UserFriendAddMessage userFriendAddMessage);
}