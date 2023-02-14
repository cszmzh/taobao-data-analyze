package com.banana.visual.entity.mysql;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
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
@DynamicInsert
@DynamicUpdate
public class Chart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    @Size(min = 2, max = 20)
    private String name;
    @Size(min = 1, max = 200)
    private String chartDes;
    private Long creator;
    @Size(min = 10, max = 3000)
    private String detail;
    @Size(max = 1000)
    private String powerBiKey;
    @Size(max = 3000)
    private String sqlText;
    @Range(min = 0, max = 2)
    private Long viewStatus;
    private Date updateTime;
    private Date createTime;
}
