package com.banana.visual.repository;

import com.banana.visual.entity.mongo.LoginInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginInfoRepository extends MongoRepository<LoginInfo, String> {

}
