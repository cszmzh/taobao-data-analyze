package com.banana.visual.service.impl;

import com.banana.visual.entity.mongo.IndexInfo;
import com.banana.visual.repository.IndexInfoRepository;
import com.banana.visual.service.IndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexInfoServiceImpl implements IndexInfoService {

    @Autowired
    private IndexInfoRepository indexInfoRepository;

    @Override
    public List<IndexInfo> getIndexInfoList() {
        List<IndexInfo> indexInfoList = indexInfoRepository.findAll();
        return indexInfoList;
    }
}
