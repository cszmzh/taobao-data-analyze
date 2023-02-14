package com.banana.visual.enums;

import lombok.Getter;

@Getter
public enum IndexInfoEnum {

    SYSTEM_STARTUP_TITLE("系统启动时间戳"),
    COUNT_USER_TITLE("用户数"),
    COUNT_DEPT_TITLE("部门数"),
    COUNT_CHART_TITLE("图表数"),
    COUNT_JOB_TITLE("岗位数"),
    COUNT_DATA_TITLE("数据集");

    private String title;

    IndexInfoEnum(String title) {
        this.title = title;
    }
}
