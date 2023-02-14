package com.banana.visual.VO;

import hashids.formatter.HashidsFormat;
import lombok.Data;

@Data
public class ChartAdviceVO {
    // ChartAdvice
    private String objectId;
    private Long uid;
    @HashidsFormat
    private Long cid;
    private String content;
    private Long createTimestamp;

    // User
    private String avatar;
    private String username;

    // Department
    private String depName;
}
