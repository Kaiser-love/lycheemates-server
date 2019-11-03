package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 18:18
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendAddMessageOutputVo {
    private Long id;
    private User sendUser;
    private User receiveUser;
    private Date addTime;
    private String content;
    private Byte state;
}