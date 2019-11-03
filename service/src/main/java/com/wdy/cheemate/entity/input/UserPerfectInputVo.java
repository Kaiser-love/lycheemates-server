package com.wdy.cheemate.entity.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wdy.cheemate.common.util.DateUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-12 11:37
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserPerfectInputVo {
    private String account;
    private String password;
    private String avatarUrl;
    private String nickName;
    private String country;
    private String province;
    private String city;
    private String realName;
    private String email;
    private String phone;
    private Date birthday;
    private Byte gender;
    private String job;
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
}