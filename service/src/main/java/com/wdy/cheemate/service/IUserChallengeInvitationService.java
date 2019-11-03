package com.wdy.cheemate.service;

import com.wdy.cheemate.common.request.PageRequestBean;
import com.wdy.cheemate.common.response.ResultBean;
import com.wdy.cheemate.entity.UserChallengeInvitation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wdy.cheemate.entity.output.UserChallengeInvitationOutputVo;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
public interface IUserChallengeInvitationService extends IService<UserChallengeInvitation> {

    ResponseEntity<ResultBean> putUserChallengeInvitation(UserChallengeInvitation userChallengeInvitation);

    ResponseEntity<ResultBean> putUserChallengeInvitationSign(Long id, Integer type);

    UserChallengeInvitationOutputVo sendMessage(UserChallengeInvitation userChallengeInvitation);

    UserChallengeInvitationOutputVo fillUserChallengeInvitation(UserChallengeInvitation userChallengeInvitation);

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean, Integer type);

}
