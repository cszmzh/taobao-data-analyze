package com.banana.visual.entity.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "chart_advice")
public class ChartAdvice {
    @MongoId
    private ObjectId objectId;
    private Long uid;
    private Long cid;
    private String content;
    private Long createTimestamp;
}
