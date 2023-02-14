package com.banana.visual.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "index_info")
public class IndexInfo {
    @MongoId
    private String title;
    private String content;

    public IndexInfo() {

    }

    public IndexInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
