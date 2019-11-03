package com.wdy.cheemate.dto;

import lombok.*;

import java.util.List;

/**
 * @author: wdy
 * @create: 2019-10-25 09:38
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageListAndConditions {
    private List list;
    private String conditionString;
}