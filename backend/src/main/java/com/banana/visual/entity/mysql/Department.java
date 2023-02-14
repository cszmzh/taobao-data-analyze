package com.banana.visual.entity.mysql;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Range(min = 1, max = 99999)
    private Long depId;
    @Size(min = 2, max = 15)
    private String depName;
    @Size(min = 1, max = 1000)
    private String depDes;
    private Long depManager;
    private Date updateTime;
    private Date createTime;
}
