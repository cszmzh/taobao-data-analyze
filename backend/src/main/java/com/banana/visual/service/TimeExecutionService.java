package com.banana.visual.service;

public interface TimeExecutionService {
    /**
     * 1.启动初始化任务
     */
    void startup();

    /**
     * 2.每日总结数据信息
     */
    void summarizeData();
}
