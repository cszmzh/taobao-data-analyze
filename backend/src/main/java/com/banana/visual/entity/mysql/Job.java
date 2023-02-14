package com.banana.visual.entity.mysql;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Job {

    @Id
    @Range(min = 1, max = 99999999)
    private Long jid;
    @Size(min = 2, max = 30)
    private String jobName;
    @Size(min = 1, max = 1000)
    private String jobDes;
    @Size(min = 1, max = 10)
    private String baseSalary;
    private Long depId;
    private Date updateTime;
    private Date createTime;
}
