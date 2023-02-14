package com.banana.visual.entity.mysql;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Size(min = 2, max = 15)
    private String username;
    @Size(min = 2, max = 20)
    private String realName;
    private Long jid;
    @Size(min = 6, max = 16)
    private String password;
    private String avatar;
    @Pattern(regexp = "^[1][3,4,5,7,8,9][0-9]{9}$")
    private String phone;
    @Email(regexp = "^([a-zA-Z0-9]+[_|_|\\-|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$")
    private String email;
    @Size(max = 100)
    private String signature;
    private String permission;
    private Date updateTime;
    private Date createTime;
}
