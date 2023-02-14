package com.banana.visual.runner;

import com.banana.visual.service.TimeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 通过实现CommandLineRunner接口，在SpringBoot项目启动后执行
 */
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Autowired
    private TimeExecutionService timeExecutionService;

    @Override
    public void run(String... args) throws Exception {
        timeExecutionService.startup();
        timeExecutionService.summarizeData();
    }
}
