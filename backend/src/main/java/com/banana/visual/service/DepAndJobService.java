package com.banana.visual.service;

import com.alibaba.fastjson.JSONObject;
import com.banana.visual.entity.mysql.Department;
import com.banana.visual.entity.mysql.Job;

import java.util.List;

public interface DepAndJobService {
    List<JSONObject> getDepAndJobList();

    List<Department> getDepartment();

    Department getDepById(Long depId);

    boolean saveDepartment(Department department);

    boolean deleteDepartment(Long depId);

    boolean addJob(Job job);

    boolean deleteJob(Long jid);

    boolean updateJob(Job job);

    boolean updateJidAndJob(Job job, Long newJid);
}
