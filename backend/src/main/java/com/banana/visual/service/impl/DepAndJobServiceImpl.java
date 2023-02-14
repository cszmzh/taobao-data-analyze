package com.banana.visual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.banana.visual.entity.mysql.Department;
import com.banana.visual.entity.mysql.Job;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.exception.SelfDefineException;
import com.banana.visual.repository.DepartmentRepository;
import com.banana.visual.repository.JobRepository;
import com.banana.visual.service.DepAndJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepAndJobServiceImpl implements DepAndJobService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<JSONObject> getDepAndJobList() {
        List<Department> departmentList = departmentRepository.findAll();
        List<JSONObject> objectList = new ArrayList<>();

        for (Department department : departmentList) {
            JSONObject depObject = new JSONObject();
            depObject.put("value", department.getDepName());
            depObject.put("label", department.getDepName());
            depObject.put("depId", department.getDepId());

            List<Job> jobList = jobRepository.findAllByDepId(department.getDepId());
            List<JSONObject> childrenList = new ArrayList<>();

            for (Job job : jobList) {
                JSONObject jobObject = new JSONObject();
                jobObject.put("value", job.getJobName());
                jobObject.put("label", job.getJobName());
                jobObject.put("jid", job.getJid());
                jobObject.put("jobDes", job.getJobDes());
                jobObject.put("baseSalary", job.getBaseSalary());
                jobObject.put("updateTime", job.getUpdateTime());
                jobObject.put("createTime", job.getCreateTime());
                childrenList.add(jobObject);
            }

            depObject.put("children", childrenList);

            objectList.add(depObject);
        }
        return objectList;
    }

    @Override
    public List<Department> getDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepById(Long depId) {
        return departmentRepository.findById(depId).orElse(null);
    }

    @Override
    public boolean saveDepartment(Department department) {
        departmentRepository.saveAndFlush(department);
        return true;
    }

    @Override
    public boolean deleteDepartment(Long depId) {
        departmentRepository.deleteById(depId);
        return true;
    }

    @Override
    public boolean addJob(Job job) {
        if (jobRepository.findById(job.getJid()).orElse(null) != null) {
            throw new SelfDefineException(ResultEnum.JID_IS_EXIST);
        }
        jobRepository.saveAndFlush(job);
        return true;
    }

    @Override
    public boolean deleteJob(Long jid) {
        jobRepository.deleteById(jid);
        return true;
    }

    @Override
    public boolean updateJob(Job job) {
        Job oldJob = jobRepository.findById(job.getJid()).orElse(null);
        job.setCreateTime(oldJob.getCreateTime());
        jobRepository.saveAndFlush(job);
        return true;
    }

    @Override
    @Transactional
    public boolean updateJidAndJob(Job job, Long newJid) {
        updateJob(job);
        jobRepository.updateJid(newJid, job.getJid());
        return true;
    }
}
