package com.banana.visual.service;

import com.banana.visual.VO.ChartAdviceVO;
import com.banana.visual.VO.ChartVO;
import com.banana.visual.entity.mongo.ChartAdvice;
import com.banana.visual.entity.mysql.Chart;

import java.util.List;

public interface ChartService {
    List<ChartVO> getAllChartList();

    List<ChartVO> getPublicChartList();

    List<ChartVO> getDepChartListByDepId(Long depId);

    List<ChartVO> getChartListByUid(Long uid);

    List<ChartVO> getPublicAndDepChartByRandom(Long uid);

    void saveChartInfo(Chart chart);

    boolean deleteChart(Long cid);

    boolean saveChartAdvice(ChartAdvice chartAdvice);

    boolean deleteChartAdvice(String adviceId);

    List<ChartAdviceVO> getChartAdviceList(Long cid);
}
