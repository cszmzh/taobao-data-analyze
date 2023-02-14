package com.banana.visual.repository;

import com.banana.visual.entity.mongo.ChartAdvice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartAdviceRepository extends MongoRepository<ChartAdvice, String> {

}
