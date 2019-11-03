package com.wdy.cheemate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wdy
 * @since 2019-05-18
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String account;
    private String password;
    private String nickName;
    private String openid;
    private String avatarUrl;
    private String email;
    private String phone;
    private Date birthday;
    private String realName;
    private String appid;
    private String country;
    private String province;
    private String city;
    private Byte gender;
    private String job;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private Byte status;
    private String location;
    private Integer cetFour;
    private Integer cetSix;
    private double ielts;
    private double ets;
    private double gmat;
    private double gre;
    private String languages;
    private Integer age;
    private Integer lycheeNumber;
    private String tag;
    private String introduction;
    private String schoolCardUrl;
    private String grade;
    private String major;
    private String userMatchIntent;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public User(String account) {
        this.account = account;
    }

}
