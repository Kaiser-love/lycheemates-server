package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 18:23
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChallengeInvitationOutputVo {
    private Long id;

    private User sendUser;

    private User receiveUser;

    private Integer lycheeNumber;

    private Integer challengeTime;

    private Date sendTime;
    private Date beginTime;
    private Date endTime;
    private String content;

    private Byte state;
    private Date sendSignTime;
    private Date receiveSignTime;

    private Date sendFinishTime;
    private Date receiveFinishTime;
}