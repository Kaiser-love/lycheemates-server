package com.wdy.cheemate.common.constant;

/**
 * @author: dongyang_wu
 * @create: 2019-10-19 14:11
 * @description:
 */
public interface StateConstant {
    interface Invitation {
        // 等待推送
        Byte WAIT_PUBLISH = -1;
        // 等待接受
        Byte WAIT_ACCEPT = 0;
        // 用户取消
        Byte USER_CANCEL = 1;
        // 超时取消
        Byte TIME_OVERCANCEL = 2;
        // 等待开始
        Byte WAIT_BEGIN = 3;
        // 正在进行
        Byte IS_RUNNING = 4;
        // 完成
        Byte END_INVITATION = 5;
    }
}