package com.wdy.cheemate.common.util;


import com.wdy.cheemate.service.*;
import lombok.Data;
import lombok.Getter;

/**
 * @description: 枚举单例
 * @author: dongyang_wu
 * @create: 2019-08-01 14:12
 */
public enum SingletonEnum {
    INSTANCE;
    @Getter
    private ISingleChatMessageService singleChatMessageService;
    @Getter
    private IUserFriendAddMessageService userFriendAddMessageService;
    @Getter
    private IUserChallengeInvitationService userChallengeInvitationService;

    // 同时每个枚举实例都是static final类型的，也就表明只能被实例化一次
    SingletonEnum() {
        singleChatMessageService = SpringUtil.getBean(ISingleChatMessageService.class);
        userFriendAddMessageService = SpringUtil.getBean(IUserFriendAddMessageService.class);
        userChallengeInvitationService = SpringUtil.getBean(IUserChallengeInvitationService.class);
    }

}
