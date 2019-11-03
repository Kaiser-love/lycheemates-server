package com.wdy.cheemate.entity;

import com.wdy.cheemate.common.request.PageRequestBean;
import lombok.*;

import java.util.List;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:09
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMatchIntent {
    private String conditionConnection;
    private List<PageRequestBean.PageRequestItem> conditions;
}