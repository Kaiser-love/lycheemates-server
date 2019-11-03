package com.wdy.cheemate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-23 22:40
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendAddMessage extends Model<UserFriendAddMessage> implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long sendUserId;
    private Long receiveUserId;
    private Date addTime;
    private String content;
    private Byte state;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}