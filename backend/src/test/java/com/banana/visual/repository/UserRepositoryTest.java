package com.banana.visual.repository;

import com.banana.visual.entity.mongo.IndexInfo;
import com.banana.visual.service.TimeExecutionService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndexInfoRepository indexInfoRepository;

    @Autowired
    private ChartAdviceRepository chartAdviceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TimeExecutionService timeExecutionService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void findByUsername() {
        System.out.println(userRepository.findByUsername("admin"));
    }

    @Test
    void testMongo() {
        IndexInfo indexInfo = new IndexInfo("Tableau", "9.1.41");
        indexInfoRepository.save(indexInfo);
    }

    @Test
    void testMongo2() {
        System.out.println(departmentRepository.getDeptNameByJobId(1L));
    }

    @Test
    void testJava(){
        Integer i = 127;
        Integer j = 127;
        System.out.println(i==j);
        System.out.println(chartAdviceRepository.findAll());
        System.out.println(" --- ");
        ObjectId objectId = new ObjectId("622ca9202613d15473788238");
    }
}