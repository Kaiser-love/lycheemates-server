package com.wdy.cheemate.entity.output;


import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-10-24 18:04
 * @description:
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputVo {
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
    private Date createTime;
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
    private Integer focusUserNumber;
    private Integer fansUserNumber;
}