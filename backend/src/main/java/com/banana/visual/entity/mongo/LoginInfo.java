package com.banana.visual.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "login_info")
public class LoginInfo {
    private Long uid;
    private String ip;
    private Long loginTimestamp;
}