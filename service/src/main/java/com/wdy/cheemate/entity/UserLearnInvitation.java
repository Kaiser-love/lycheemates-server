package com.wdy.cheemate.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserLearnInvitation extends Model<UserLearnInvitation> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long sendUserId;

    private Long receiveUserId;

    private String content;

    private Date sendTime;

    private Date beginTime;

    private Date endTime;

    private Byte state;

    private Date sendSignTime;
    private Date receiveSignTime;

    private Date sendFinishTime;
    private Date receiveFinishTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
