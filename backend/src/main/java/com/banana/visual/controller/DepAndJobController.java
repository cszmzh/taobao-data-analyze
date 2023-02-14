package com.banana.visual.controller;

import com.alibaba.fastjson.JSONObject;
import com.banana.visual.VO.ResultVO;
import com.banana.visual.entity.mysql.Department;
import com.banana.visual.entity.mysql.Job;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.exception.SelfDefineException;
import com.banana.visual.service.DepAndJobService;
import com.banana.visual.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 部门与岗位业务控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/16
 */
@RestController
@RequestMapping("/depAndJob")
public class DepAndJobController {
    @Autowired
    private DepAndJobService depAndJobService;
    @Autowired
    private UserService userService;

    Department JSON2Department(JSONObject body) {
        Department department = new Department();
        if (StringUtils.isNoneBlank(body.getString("depId"))) {
            department = depAndJobService.getDepById(Long.valueOf(body.getString("depId")));
        }
        department.setDepName(body.getString("depName"));
        department.setDepDes(body.getString("depDes"));
        if (StringUtils.isNoneBlank(body.getString("depManager"))) {
            if (userService.getUserByUid(Long.valueOf(body.getString("depManager"))) == null) {
                throw new SelfDefineException(ResultEnum.UID_NOT_FOUND);
            }
            department.setDepManager(Long.valueOf(body.getString("depManager")));
        }
        return department;
    }

    Job JSON2Job(JSONObject body) {
        Job job = new Job();
        if (StringUtils.isNoneBlank(body.getString("jid"))) {
            job.setJid(Long.valueOf(body.getString("jid")));
        }
        job.setDepId(Long.valueOf(body.getString("depId")));
        job.setJobName(body.getString("jobName"));
        job.setJobDes(body.getString("jobDes"));
        job.setBaseSalary(body.getString("baseSalary"));
        return job;
    }

    @GetMapping("/getList")
    public ResultVO getList() {
        ResultVO resultVO = new ResultVO(HttpStatus.SC_OK, "success",
                depAndJobService.getDepAndJobList());
        return resultVO;
    }

    @GetMapping("/getDepartment")
    public ResultVO getDepartment() {
        ResultVO resultVO = new ResultVO(HttpStatus.SC_OK, "success",
                depAndJobService.getDepartment());
        return resultVO;
    }

    @PostMapping("/addDepartment")
    public ResultVO addDepartment(@RequestBody Department department) {
        Department d = depAndJobService.getDepById(department.getDepId());
        if (d != null) {
            return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION.getCode(), "部门编号已存在，请重新输入", null);
        }
        if (department.getDepManager() != null) {
            if (userService.getUserByUid(department.getDepManager()) == null) {
                throw new SelfDefineException(ResultEnum.UID_NOT_FOUND);
            }
        }
        boolean res = depAndJobService.saveDepartment(department);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION);
    }

    @PostMapping("/updateDepartment")
    public ResultVO updateDepartment(@RequestBody JSONObject body) {
        Department department = JSON2Department(body);
        boolean res = depAndJobService.saveDepartment(department);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION);
    }

    @PostMapping("/deleteDepartment")
    public ResultVO deleteDepartment(@RequestBody JSONObject body) {
        Long depId = Long.valueOf(body.getString("depId"));
        boolean res = depAndJobService.deleteDepartment(depId);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success", null);
        }
        return new ResultVO(ResultEnum.DATA_DELETION_EXCEPTION);
    }

    @PostMapping("/addJob")
    public ResultVO addJob(@RequestBody JSONObject body) {
        Job job = JSON2Job(body);
        boolean res = depAndJobService.addJob(job);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success", null);
        }
        return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION);
    }

    @PostMapping("/deleteJob")
    public ResultVO deleteJob(@RequestBody JSONObject body) {
        Long jid = Long.valueOf(body.getString("jid"));
        boolean res = depAndJobService.deleteJob(jid);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success", null);
        }
        return new ResultVO(ResultEnum.DATA_DELETION_EXCEPTION);
    }

    @PostMapping("/updateJob")
    public ResultVO updateJob(@RequestBody JSONObject body) {
        Job job = JSON2Job(body);
        boolean res;
        if (StringUtils.isNoneBlank(body.getString("newJid"))) {
            Long newJid = Long.valueOf(body.getString("newJid"));
            res = depAndJobService.updateJidAndJob(job, newJid);
        } else {
            res = depAndJobService.updateJob(job);
        }
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success", null);
        }
        return new ResultVO(ResultEnum.DATA_UPDATE_EXCEPTION);
    }
}
