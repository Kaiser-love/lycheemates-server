package com.wdy.cheemate.entity.output;

import com.wdy.cheemate.entity.User;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 18:25
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostOutputVo
{
    private Long id;
    private User user;

    private String content;
    private Date publishTime;
    private String learningTime;
}