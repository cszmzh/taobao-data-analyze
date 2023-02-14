package com.banana.visual.VO;

import com.banana.visual.entity.mongo.LoginInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserInfoVO {
    private Long uid;
    private String username;
    private String realName;
    private Long jid;
    private String avatar;
    private String phone;
    private String email;
    private String signature;
    private String permission;
    private Date updateTime;
    private Date createTime;

    private List<LoginInfo> loginInfoList;
    private String passwordLevel;
    private String depName;
    private String jobName;
    private Long depId;
}
