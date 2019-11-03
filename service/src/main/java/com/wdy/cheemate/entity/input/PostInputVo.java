package com.wdy.cheemate.entity.input;

import lombok.*;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 23:17
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostInputVo {
    private Long id;
    private String content;
    private String learningTime;
}