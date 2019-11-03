package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 17:55
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToFocusOutputVo {
    private Long id;
    private User user;
    private User focusUser;
    private Date focusTime;
}