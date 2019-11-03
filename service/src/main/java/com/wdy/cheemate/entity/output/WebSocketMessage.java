package com.wdy.cheemate.entity.output;

import lombok.*;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 16:41
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {

    private String type;
    private String jsonString;
}