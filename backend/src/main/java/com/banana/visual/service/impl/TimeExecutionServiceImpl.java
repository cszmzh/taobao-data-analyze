package com.banana.visual.service.impl;

import com.banana.visual.entity.mongo.IndexInfo;
import com.banana.visual.enums.IndexInfoEnum;
import com.banana.visual.repository.*;
import com.banana.visual.service.TimeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TimeExecutionServiceImpl implements TimeExecutionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndexInfoRepository indexInfoRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataRandomRepository dataRandomRepository;

    @Override
    public void startup() {
        // 初始化系统启动时间
        IndexInfo indexInfo = new IndexInfo(IndexInfoEnum.SYSTEM_STARTUP_TITLE.getTitle(), String.valueOf(System.currentTimeMillis()));
        indexInfoRepository.save(indexInfo);
    }

    @Override
    @Scheduled(cron = "0 55 23 * * ?")
    public void summarizeData() {

        IndexInfo indexInfo = new IndexInfo();

        // 1.用户数统计
        indexInfo.setTitle(IndexInfoEnum.COUNT_USER_TITLE.getTitle());
        indexInfo.setContent(String.valueOf(userRepository.countUserNumber()));
        indexInfoRepository.save(indexInfo);

        // 2.部门数统计
        indexInfo.setTitle(IndexInfoEnum.COUNT_DEPT_TITLE.getTitle());
        indexInfo.setContent(String.valueOf(departmentRepository.countDeptNum()));
        indexInfoRepository.save(indexInfo);

        // 3.图表数统计
        indexInfo.setTitle(IndexInfoEnum.COUNT_CHART_TITLE.getTitle());
        indexInfo.setContent(String.valueOf(chartRepository.countChartNum()));
        indexInfoRepository.save(indexInfo);

        // 4.岗位数统计
        indexInfo.setTitle(IndexInfoEnum.COUNT_JOB_TITLE.getTitle());
        indexInfo.setContent(String.valueOf(jobRepository.countJobNum()));
        indexInfoRepository.save(indexInfo);

        // 5.数据集统计
        indexInfo.setTitle(IndexInfoEnum.COUNT_DATA_TITLE.getTitle());
        indexInfo.setContent(String.valueOf(dataRandomRepository.countDataRandomNum()));
        indexInfoRepository.save(indexInfo);
    }
}
