package com.banana.visual.VO;

import lombok.Data;

@Data
public class LoginInfoVO {
    private Long uid;
    private String username;
    private String avatar;
    private String permission;
    private String token;
}
