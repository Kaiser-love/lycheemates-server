package com.wdy.cheemate.entity;

import java.time.LocalDate;
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
public class UserChallengeInvitation extends Model<UserChallengeInvitation> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long sendUserId;

    private Long receiveUserId;

    private Integer lycheeNumber;

    private Integer challengeTime;

    private Date sendTime;
    private Date beginTime;
    private Date endTime;
    private String content;

    private Byte state;
    private Date sendSignTime;
    private Date receiveSignTime;

    private Date sendFinishTime;
    private Date receiveFinishTime;
//
//    private Byte isBegin;
//
//    private Byte isRunning;
//
//    private Byte isCancelled;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
