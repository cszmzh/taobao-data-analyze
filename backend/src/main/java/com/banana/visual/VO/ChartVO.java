package com.banana.visual.VO;

import hashids.formatter.HashidsFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChartVO {
    // Chart
    @HashidsFormat
    private Long cid;
    private String name;
    private String chartDes;
    private Long creator;
    private String detail;
    private String powerBiKey;
    private String sqlText;
    private Long viewStatus;
    private Date updateTime;
    private Date createTime;

    // User
    private String avatar;
    private String username;

    // Department
    private Long depId;
    private String depName;
}
