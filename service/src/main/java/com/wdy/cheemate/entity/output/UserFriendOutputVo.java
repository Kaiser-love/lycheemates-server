package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 18:16
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendOutputVo {
    private Long id;
    private User friendUser;
    private User user;
    private Date addTime;
}