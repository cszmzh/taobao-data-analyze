package com.banana.visual.service.impl;

import com.banana.visual.VO.ChartAdviceVO;
import com.banana.visual.VO.ChartVO;
import com.banana.visual.context.UserContext;
import com.banana.visual.entity.mongo.ChartAdvice;
import com.banana.visual.entity.mysql.Chart;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.enums.ChartViewStatusEnum;
import com.banana.visual.enums.UserRolesEnum;
import com.banana.visual.repository.*;
import com.banana.visual.service.ChartService;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ChartAdviceRepository chartAdviceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private List<ChartVO> Chart2ChartVO(List<Chart> chartList) {
        List<ChartVO> chartVOList = new ArrayList<>();
        for (Chart chart : chartList) {
            ChartVO chartVO = new ChartVO();
            // 填入图表信息
            BeanUtils.copyProperties(chart, chartVO);
            // 填入用户信息
            User user = userRepository.findById(chart.getCreator()).get();
            chartVO.setUsername(user.getUsername());
            chartVO.setAvatar(user.getAvatar());
            // 填入部门信息
            chartVO.setDepId(jobRepository.findById(user.getJid()).orElse(null).getDepId());
            chartVO.setDepName(departmentRepository.getDeptNameByJobId(user.getJid()));
            // 填入列表
            chartVOList.add(chartVO);
        }
        return chartVOList;
    }

    @Override
    public List<ChartVO> getAllChartList() {
        List<Chart> chartList = chartRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        return Chart2ChartVO(chartList);
    }

    @Override
    public List<ChartVO> getPublicChartList() {
        List<Chart> chartList = chartRepository.findAllByViewStatusOrderByCreateTimeDesc(ChartViewStatusEnum.PUBLIC.getValue());
        return Chart2ChartVO(chartList);
    }

    @Override
    public List<ChartVO> getDepChartListByDepId(Long depId) {
        List<Chart> chartList = chartRepository.findAllByDepId(depId);
        return Chart2ChartVO(chartList);
    }

    @Override
    public List<ChartVO> getChartListByUid(Long uid) {
        List<Chart> chartList = chartRepository.findAllByCreatorOrderByCreateTimeDesc(uid);
        return Chart2ChartVO(chartList);
    }

    @Override
    public List<ChartVO> getPublicAndDepChartByRandom(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        Long depId = jobRepository.findById(user.getJid()).get().getDepId();
        List<Chart> chartList = chartRepository.findPublicAndDepChartByRandom(depId);
        return Chart2ChartVO(chartList);
    }

    @Override
    public void saveChartInfo(Chart chart) {
        if (chart.getCid() != null) {
            Chart oldChart = chartRepository.findById(chart.getCid()).get();
            chart.setCreateTime(oldChart.getCreateTime());
        }
        chartRepository.save(chart);
    }

    @Override
    @Transactional
    public boolean deleteChart(Long cid) {

        Long executeUid = UserContext.get().getUid();
        String permission = UserContext.get().getPermission();

        Chart chart = chartRepository.findById(cid).get();
        chart.getCreator();
        if (UserRolesEnum.HIGH.getPermission().equals(permission)
                || UserRolesEnum.MIDDLE.getPermission().equals(permission)
                || chart.getCreator().equals(executeUid)) {
            // 删除图表
            chartRepository.deleteById(cid);
            // 删除图表对应的评论
            Query query = new Query(Criteria.where("cid").is(cid));
            mongoTemplate.remove(query, ChartAdvice.class);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean saveChartAdvice(ChartAdvice chartAdvice) {
        if (chartAdvice.getObjectId() != null) {
            Query query = new Query(Criteria.where("_id").is(chartAdvice.getObjectId()));
            List<ChartAdvice> chartAdviceList = mongoTemplate.find(query, ChartAdvice.class, "chart_advice");
            chartAdvice.setCreateTimestamp(chartAdviceList.get(0).getCreateTimestamp());
        }
        chartAdviceRepository.save(chartAdvice);
        return true;
    }

    @Override
    public boolean deleteChartAdvice(String objectId) {
        Long executeUid = UserContext.get().getUid();
        String permission = UserContext.get().getPermission();
        Query query = new Query(Criteria.where("_id").is(new ObjectId(objectId)));
        ChartAdvice chartAdvice = mongoTemplate.find(query, ChartAdvice.class, "chart_advice").get(0);
        if (UserRolesEnum.HIGH.getPermission().equals(permission)
                || chartAdvice.getUid().equals(executeUid)) {
            chartAdviceRepository.delete(chartAdvice);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public List<ChartAdviceVO> getChartAdviceList(Long cid) {
        ChartAdvice chartAdvice = new ChartAdvice();
        chartAdvice.setCid(cid);
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("cid", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<ChartAdvice> example = Example.of(chartAdvice, matcher);
        List<ChartAdvice> chartAdviceList = chartAdviceRepository.findAll(example);

        List<ChartAdviceVO> chartAdviceVOList = new ArrayList<>();
        for (ChartAdvice advice : chartAdviceList) {
            ChartAdviceVO adviceVO = new ChartAdviceVO();
            // 填入建议信息
            BeanUtils.copyProperties(advice, adviceVO);
            // 填入用户信息
            User user = userRepository.findById(advice.getUid()).get();
            adviceVO.setUsername(user.getUsername());
            adviceVO.setAvatar(user.getAvatar());
            // 填入部门信息
            adviceVO.setDepName(departmentRepository.getDeptNameByJobId(user.getJid()));
            // 填入列表
            adviceVO.setObjectId(advice.getObjectId().toHexString());
            chartAdviceVOList.add(adviceVO);
        }
        return chartAdviceVOList;
    }
}
