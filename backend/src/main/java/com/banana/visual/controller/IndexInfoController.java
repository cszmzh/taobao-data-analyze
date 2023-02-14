package com.banana.visual.controller;

import com.banana.visual.VO.ResultVO;
import com.banana.visual.entity.mongo.IndexInfo;
import com.banana.visual.service.IndexInfoService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统信息控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/16
 */
@RestController
@RequestMapping("/info")
public class IndexInfoController {

    @Autowired
    private IndexInfoService indexInfoService;

    @GetMapping("/getIndexInfoList")
    public ResultVO getIndexInfoList() {
        List<IndexInfo> indexInfoList = indexInfoService.getIndexInfoList();
        ResultVO resultVO = new ResultVO(HttpStatus.SC_OK, "success", indexInfoList);
        return resultVO;
    }
}
