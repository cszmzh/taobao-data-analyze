package com.banana.visual.enums;

import lombok.Getter;

@Getter
public enum ChartViewStatusEnum {

    SELF_ONLY("仅自己可见", 0L),

    DEP_ONLY("部门内可见", 1L),

    PUBLIC("公开", 2L);

    private String desc;

    private Long value;

    ChartViewStatusEnum(String desc, Long value) {
        this.desc = desc;
        this.value = value;
    }
}

