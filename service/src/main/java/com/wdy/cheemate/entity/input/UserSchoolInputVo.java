package com.wdy.cheemate.entity.input;

import lombok.*;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 12:24
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSchoolInputVo {
    private String location;
    private String grade;
    private String major;
}