package com.wdy.cheemate.service;

import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.UserLearnInvitation;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
public interface IUserLearnInvitationService extends IService<UserLearnInvitation> {

    ResponseEntity<ResultBean> putUserLearnInvitation(UserLearnInvitation userLearnInvitation);

    UserLearnInvitation sendMessage(UserLearnInvitation userLearnInvitation);

    ResponseEntity<ResultBean> putUserLearnInvitationSign(Long id);

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type);

}
