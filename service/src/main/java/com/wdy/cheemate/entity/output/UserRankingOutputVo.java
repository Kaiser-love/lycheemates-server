package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import com.wdy.cheemate.entity.UserChallengeInvitation;
import lombok.*;

/**
 * @author: wdy
 * @create: 2019-10-25 11:18
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRankingOutputVo implements Comparable<UserRankingOutputVo> {
    private User user;
    private UserChallengeInvitation userChallengeInvitation;
    private String type;
    private String learningTime;
    @Builder.Default
    private Boolean isOwn = false;

    @Override
    public int compareTo(UserRankingOutputVo o) {
        return this.learningTime.compareTo(o.getLearningTime());
    }
}