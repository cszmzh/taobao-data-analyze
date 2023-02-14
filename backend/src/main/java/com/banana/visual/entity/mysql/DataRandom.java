package com.banana.visual.entity.mysql;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class DataRandom {

    @Id
    private Long userId;
    private Long itemId;
    private Long categoryId;
    private String behaviorType;
    private Long timestamp;
    private String buyDate;
    private String buyHour;
}
