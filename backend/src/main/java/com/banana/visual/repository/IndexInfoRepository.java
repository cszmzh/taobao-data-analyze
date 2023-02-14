package com.banana.visual.repository;

import com.banana.visual.entity.mongo.IndexInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends MongoRepository<IndexInfo, String> {

}
