package com.banana.visual.controller;

import com.banana.visual.VO.ChartAdviceVO;
import com.banana.visual.VO.ChartVO;
import com.banana.visual.VO.ResultVO;
import com.banana.visual.context.UserContext;
import com.banana.visual.entity.mongo.ChartAdvice;
import com.banana.visual.entity.mysql.Chart;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.service.ChartService;
import com.banana.visual.service.UserService;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图表业务控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/2
 */
@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private ChartService chartService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllChartList")
    public ResultVO getAllChartList() {
        List<ChartVO> chartVOList = chartService.getAllChartList();
        ResultVO resultVO = new ResultVO(0, "success", chartVOList);
        return resultVO;
    }

    @GetMapping("/getPublicChartList")
    public ResultVO getPublicChartList() {
        List<ChartVO> chartVOList = chartService.getPublicChartList();
        ResultVO resultVO = new ResultVO(0, "success", chartVOList);
        return resultVO;
    }

    @GetMapping("/getDepChartList")
    public ResultVO getDepChartList() {
        List<ChartVO> chartVOList = chartService.getDepChartListByDepId(
                userService.getUserBaseInfoByUid(UserContext.get().getUid()).getDepId());
        ResultVO resultVO = new ResultVO(0, "success", chartVOList);
        return resultVO;
    }

    @GetMapping("/getSelfChartList")
    public ResultVO getSelfChartList() {
        List<ChartVO> chartVOList = chartService.getChartListByUid(UserContext.get().getUid());
        ResultVO resultVO = new ResultVO(0, "success", chartVOList);
        return resultVO;
    }

    @GetMapping("/getRecChartList")
    public ResultVO getRecChartList() {
        List<ChartVO> chartVOList = chartService.getPublicAndDepChartByRandom(UserContext.get().getUid());
        ResultVO resultVO = new ResultVO(0, "success", chartVOList);
        return resultVO;
    }

    @PostMapping("/saveChartInfo")
    public ResultVO saveChartInfo(@RequestBody ChartVO chartVO) {
        // 接收图表信息
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartVO, chart);
        chart.setCreator(UserContext.get().getUid());

        chartService.saveChartInfo(chart);
        ResultVO resultVO = new ResultVO(HttpStatus.SC_OK, "success", null);
        return resultVO;
    }

    @PostMapping("/deleteChart")
    public ResultVO deleteChart(@RequestBody ChartVO chartVO) {
        Long cid = chartVO.getCid();
        boolean res = chartService.deleteChart(cid);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success", null);
        }
        return new ResultVO(ResultEnum.DATA_DELETION_EXCEPTION);
    }

    @PostMapping("/saveChartAdvice")
    public ResultVO saveChartAdvice(@RequestBody ChartAdviceVO chartAdviceVO) {
        chartAdviceVO.setCreateTimestamp(System.currentTimeMillis()); // 设置时间戳
        ChartAdvice chartAdvice = new ChartAdvice();
        BeanUtils.copyProperties(chartAdviceVO, chartAdvice);
        boolean res = chartService.saveChartAdvice(chartAdvice);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION);
    }

    @PostMapping("/deleteChartAdvice")
    public ResultVO deleteChartAdvice(@RequestBody ChartAdviceVO chartAdviceVO) {
        String objectId = chartAdviceVO.getObjectId();
        boolean res = chartService.deleteChartAdvice(objectId);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_DELETION_EXCEPTION);
    }

    @PostMapping("/getChartAdviceList")
    public ResultVO getChartAdviceList(@RequestBody ChartAdviceVO chartAdviceVO) {
        List<ChartAdviceVO> chartAdviceVOList = chartService.getChartAdviceList(chartAdviceVO.getCid());
        ResultVO resultVO = new ResultVO(0, "success", chartAdviceVOList);
        return resultVO;
    }
}
