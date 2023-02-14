1.部门表(department)

```sql
CREATE TABLE `department` (
  `dep_id` int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '部门编号',
  `dep_name` varchar(15) NOT NULL COMMENT '部门名称',
  `dep_des` varchar(1000) NOT NULL COMMENT '部门描述/介绍',
  `dep_manager` int(8) DEFAULT NULL COMMENT '部门管理者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



2.职位表(job)

```sql
CREATE TABLE `job` (
  `jid` int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '职位编号',
  `job_name` varchar(30) NOT NULL COMMENT '职位名称',
  `job_des` varchar(1000) NOT NULL COMMENT '职位描述/介绍',
  `base_salary` varchar(10) NOT NULL COMMENT '基础工资',
  `dep_id` int unsigned NOT NULL COMMENT '所属部门编号',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT `job_ibfk1` FOREIGN KEY (`dep_id`) REFERENCES `department` (`dep_id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```



3.用户表(user)

```sql
CREATE TABLE `user` (
  `uid` int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(20) NOT NULL UNIQUE KEY COMMENT '昵称',
  `real_name` varchar(20) NOT NULL COMMENT '使用者真实姓名',
  `jid` int unsigned NOT NULL COMMENT '职位编号',
  `password` varchar(16) NOT NULL COMMENT '密码',
  `avatar` varchar(1000) DEFAULT 'https://yun.515code.com/static/visual-system/default.png' COMMENT '头像链接',
  `phone` varchar(11) NOT NULL COMMENT '手机号码',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `permission` varchar(10) NOT NULL COMMENT 'staff/manager/admin',
  `signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT `user_ibfk1` FOREIGN KEY (`jid`) REFERENCES `job` (`jid`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
```



4.分析图表(chart)

```sql
CREATE TABLE `chart` (
  `cid` int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '图表编号',
  `name` varchar(20) NOT NULL COMMENT '图表标题',
  `chart_des` varchar(200) NOT NULL COMMENT '图表描述/介绍',
  `creator` int(8) NOT NULL COMMENT '创建者用户编号',
  `detail` varchar(1000) NOT NULL COMMENT '详细说明',
  `power_bi_key` varchar(1000) NOT NULL COMMENT '图表标识符',
  `sql_text` varchar(1000) DEFAULT NULL COMMENT '数据处理语句',
  `view_status` smallint(2) NOT NULL COMMENT '0-仅自己可看，1-部门内可见，2-公开',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT `chart_ibfk1` FOREIGN KEY (`creator`) REFERENCES `user` (`uid`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

