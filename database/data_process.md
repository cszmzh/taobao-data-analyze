## 数据导入与清洗

创建数据库（注意不要设置主键）

```sql
CREATE TABLE `user_behavior` (
  `User_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents a user.',
  `Item_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents an item.',
  `Category_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents the category which the corresponding item belongs to.',
  `Behavior_type` varchar(50) DEFAULT NULL COMMENT 'A string, enum-type from (''pv'', ''buy'', ''cart'', ''fav'')',
  `Timestamp` int(15) DEFAULT NULL COMMENT 'An integer, the timestamp of the behavior'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `data_random` (
  `User_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents a user.',
  `Item_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents an item.',
  `Category_ID` int(15) DEFAULT NULL COMMENT 'An integer, the serialized ID that represents the category which the corresponding item belongs to.',
  `Behavior_type` varchar(50) DEFAULT NULL COMMENT 'A string, enum-type from (''pv'', ''buy'', ''cart'', ''fav'')',
  `Timestamp` int(15) DEFAULT NULL COMMENT 'An integer, the timestamp of the behavior'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

导入数据

```sql
load data local infile 'C:/Users/banan/Downloads/UserBehavior/UserBehavior.csv' into table taobao.user_behavior fields terminated by ',';
```

随机抽取数据

```sql
INSERT into data_random SELECT * from `user_behavior` LIMIT 1000000;
```

重复值查询

```sql
SELECT user_id
FROM data_random
GROUP BY User_ID,Item_ID,`Timestamp`
HAVING count(User_ID)>1;
```

缺失值处理

```sql
SELECT count(User_ID),count(Item_ID),count(Category_ID),count(Behavior_type),count(`Timestamp`) from data_random;
```

时间处理

```sql
ALTER TABLE data_random ADD COLUMN Buy_date CHAR(10) NULL;
ALTER TABLE data_random ADD COLUMN Buy_hour CHAR(10) NULL;

UPDATE data_random SET
Buy_date = FROM_UNIXTIME(Timestamp,'%Y-%m-%d'),
Buy_hour = FROM_UNIXTIME(Timestamp,'%k:%i:%s');

ALTER TABLE data_random ADD COLUMN Time char(10) null;
UPDATE data_random SET Time = FROM_UNIXTIME(Timestamp,'%k');
```

删除2017年11月25日之前和2017年12月3日之后的日期，并修改时间

```sql
DELETE from data_random
where Buy_date < '2017-11-25' or Buy_date >= '2017-12-04';

UPDATE data_random SET Buy_date = DATE_ADD(Buy_date, INTERVAL 4 YEAR);
```

## 统计每日PV和UV

```sql
SELECT Buy_date,count(User_ID) as pv,count(DISTINCT User_ID) as uv from data_random GROUP BY Buy_date;
```

## 根据小时统计PV和UV，观测用户活动时间

```sql
SELECT HOUR(Buy_hour),count(User_ID) as pv,count(DISTINCT User_ID) as uv from data_random GROUP BY HOUR(Buy_hour);
```

## 人均每天访问次数

遇到问题：https://blog.csdn.net/weixin_39643007/article/details/105791550

```mysql
SELECT round((pv/uv)/8,2) as 每天人均访问页面
from
(SELECT Buy_date,count(User_ID) as pv,count(DISTINCT User_ID) as uv
from data_random) sub
```

## 跳失率

```sql
SELECT concat(round(SUM(IF(t.fav_num=0 AND t.cart_num=0 AND t.buy_num=0,1,0))/COUNT(User_ID)*100,2),'%') AS '跳失率'
FROM(
SELECT User_ID,
SUM(IF(Behavior_type='fav',1,0)) fav_num,
SUM(IF(Behavior_type='cart',1,0)) cart_num,
SUM(IF(Behavior_type='buy',1,0)) buy_num
FROM data_random
GROUP BY User_ID
) t
```

## RFM分析

```sql
# 计算最近交易时间lasttime, 最后一次购买距今几天recency。2021-12-07是指本项目的分析日期，在实际项目中可以使用curdate()来返回当前日期。
create view recency as 
select User_ID, max(Buy_date) as 'lasttime', datediff('2021-12-07', max(Buy_date)) as recency
from data_random where Behavior_type = 'buy' 
group by User_ID order by recency desc;

# 计算购买频率frequency
create view frequency as 
select User_ID, count(Behavior_type) as frequency 
from data_random where Behavior_type = 'buy' 
group by User_ID order by frequency desc;

# 对recency从1-5划分等级R，可以根据业务理解相应修改。本项目选择3、6、9、12、更久。
create view r等级 as 
select *, (case 
when recency<=3 then 5
when recency<=6 then 4
when recency<=9 then 3
when recency<=12 then 2
else 1 end) as R 
from recency;

# 对frequency从1-5划分等级F，本项目选择2、4、6、8、更久。
create view f等级 as 
select *, (case 
when frequency<=2 then 1 
when frequency<=4 then 2
when frequency<=6 then 3
when frequency<=8 then 4
else 5 end) as F 
from frequency;

# 计算R/F/M,本项目无M，省略
create view rfm as 
select User_ID, 
max(Buy_date) as 'lasttime', datediff('2021-12-07', max(Buy_date)) as recency,
count(behavior_type) as frequency,
(case 
when datediff('2021-12-07', max(Buy_date))<=3 then 5
when datediff('2021-12-07', max(Buy_date))<=6 then 4
when datediff('2021-12-07', max(Buy_date))<=9 then 3
when datediff('2021-12-07', max(Buy_date))<=12 then 2
else 1 end) as R ,
(case 
when count(Behavior_type)<=2 then 1 
when count(Behavior_type)<=4 then 2
when count(Behavior_type)<=6 then 3
when count(Behavior_type)<=8 then 4
else 5 end) as F 
from data_random where Behavior_type = 'buy' 
group by User_ID;

# 计算平均值
select avg(F) as F平均值 from rfm; # 1.4768
select avg(R) as R平均值 from rfm; # 2.6031

# 没有M，建立在购买量M大的前提下，对用户做出以下4类分类
create view rfm用户分类 as 
select *, 
(case 
when R>2.6031 and F>1.4768 then '重要价值用户'
when R>2.6031 and F<1.4768 then '重要深耕用户'
when R<2.6031 and F>1.4768 then '重要唤回用户'
when R<2.6031 and F<1.4768 then '重要挽留用户'
end) as 用户分类 
from rfm;

select 用户分类 from rfm用户分类;
```

联合查询

```sql
SELECT a.dep_name
from department a
left join job b on b.dep_id = a.dep_id
left join user c on  c.jid = b.jid 
where c.jid = 1;

SELECT a.cid,a.name,a.chart_des,a.creator,a.detail,a.power_bi_key,a.sql_text,a.view_status,a.update_time,a.create_time
from chart a
left join user b on b.uid = a.creator
left join job c on  c.jid = b.jid
left join department d on d.dep_id = c.dep_id
where d.dep_id = 
(select d.dep_id from department d 
left join job c on c.dep_id=d.dep_id 
left join user b on b.jid=c.jid
 where uid = 10000);
```

## 游览量top10商品类别

```sql
create view pv_top10(cate_id,浏览量,购买量,转化率,商品数量) as
SELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),'%') as 转化率,商品数量
FROM
(SELECT Category_ID,count(*)as 浏览量,
sum(case when Behavior_type='buy' then 1 else 0 end) as 购买量,
count(DISTINCT Item_ID) as 商品数量
from data_random
GROUP BY Category_ID
ORDER BY 浏览量 desc
LIMIT 10) sub
```

## 转化率top10商品类别

```sql
create view trans_top10(Category_ID,浏览量,购买量,转化率,商品数量)as
SELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),'%') as 转化率,商品数量
FROM
(SELECT Category_ID,count(*) as 浏览量,
sum(case when Behavior_type='buy' then 1 else 0 end) as 购买量,
count(DISTINCT Item_ID) as 商品数量
from data_random
GROUP BY Category_ID) sub
ORDER BY 转化率 DESC
LIMIT 10
```

## 购买量top10商品类别

```sql
create view buy_top10(cate_id,浏览量,购买量,转化率,商品数量)as
SELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),'%') as 转化率,商品数量
FROM
(SELECT Category_ID,count(*) as 浏览量,
sum(case when Behavior_type='buy' then 1 else 0 end) as 购买量,
count(DISTINCT item_id) as 商品数量
from data_random
GROUP BY Category_ID
ORDER BY 购买量 desc
LIMIT 10) data_random
```

## 行为习惯分布（按日期）

```sql
SELECT Buy_date,count(Behavior_type) as '用户行为总数',
SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
FROM data_random
GROUP BY Buy_date
ORDER By Buy_date;
```

## 行为习惯分布（按小时）

```sql
SELECT Time,count(Behavior_type) as '用户行为总数',
SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
FROM data_random
GROUP BY Time
ORDER By Time;
```

## 留存率分析

```sql
create view time_inter as 
select a.*, b.firstday, datediff(a.Buy_date, b.firstday) as day_diff from 
(select User_ID, Buy_date from data_random group by User_ID, Buy_date) as a,
(select User_ID, min(Buy_date) as firstday from data_random group by User_ID) as b
where a.User_ID = b.User_ID order by User_ID, Buy_date;

# 搭建留存天数模型retention_day，第一次活跃用户会持续活跃几天
create view retention_day as 
select firstday,
sum(case when day_diff=0 then 1 else 0 end) as day_0,
sum(case when day_diff=1 then 1 else 0 end) as day_1,
sum(case when day_diff=2 then 1 else 0 end) as day_2,
sum(case when day_diff=3 then 1 else 0 end) as day_3,
sum(case when day_diff=4 then 1 else 0 end) as day_4,
sum(case when day_diff=5 then 1 else 0 end) as day_5,
sum(case when day_diff=6 then 1 else 0 end) as day_6,
sum(case when day_diff=7 then 1 else 0 end) as day_7,
sum(case when day_diff=15 then 1 else 0 end) as day_15,
sum(case when day_diff=30 then 1 else 0 end) as day_30
from time_inter
group by firstday
order by firstday;

# 搭建留存率模型retention_rate
create view retention_rate as 
select firstday, day_0,
concat(format(day_1/day_0*100, 2), '%') as day_1,
concat(format(day_2/day_0*100, 2), '%') as day_2,
concat(format(day_3/day_0*100, 2), '%') as day_3,
concat(format(day_4/day_0*100, 2), '%') as day_4,
concat(format(day_5/day_0*100, 2), '%') as day_5,
concat(format(day_6/day_0*100, 2), '%') as day_6,
concat(format(day_7/day_0*100, 2), '%') as day_7,
concat(format(day_15/day_0*100, 2), '%') as day_15,
concat(format(day_30/day_0*100, 2), '%') as day_30
from retention_day;
```

## 【点击-购买】用户留存

```sql
# 点击用户数
SELECT
SUM(case WHEN 点击量>0 THEN 1 else 0 END) as 点击
FROM(
	SELECT User_ID,count(Behavior_type) as '用户行为总数',
	SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
	SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
	SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
	SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
	FROM data_random
	GROUP BY User_ID
	ORDER BY User_ID DESC) a;

SELECT
SUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买
FROM(
	SELECT User_ID,count(Behavior_type) as '用户行为总数',
	SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
	SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
	SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
	SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
	FROM data_random
	GROUP BY User_ID
	ORDER BY User_ID DESC) a
WHERE 收藏=0 and 加购物车=0;
```

## 【点击-收藏-购买】用户留存

```sql
SELECT
SUM(case WHEN 收藏>0 THEN 1 else 0 END) as 收藏,
SUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买
FROM(
	SELECT User_ID,count(Behavior_type) as '用户行为总数',
	SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
	SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
	SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
	SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
	FROM data_random
	GROUP BY User_ID
	ORDER BY User_ID DESC) a
WHERE 收藏!=0 and 加购物车=0;
```

## 【点击-购物车-购买】用户留存

```sql
SELECT
SUM(case WHEN 加购物车>0 THEN 1 else 0 END) as 加购物车,
SUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买
FROM(
	SELECT User_ID,count(Behavior_type) as '用户行为总数',
	SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
	SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
	SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
	SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
	FROM data_random
	GROUP BY User_ID
	ORDER BY User_ID DESC) a
WHERE 收藏=0 and 加购物车!=0;
```

## 【点击-收藏且加购物车-购买】

```sql
SELECT
(SUM(case WHEN 收藏>0 THEN 1 ELSE 0 END)+
SUM(case WHEN 加购物车>0 THEN 1 ELSE 0 END)) as 收藏且加购物车,
SUM(case WHEN 购买>0 THEN 1 ELSE 0 END) as 购买
FROM(
	SELECT User_ID,count(Behavior_type) as '用户行为总数',
	SUM(case WHEN Behavior_type = 'pv' THEN 1 ELSE 0 END) as '点击量',
	SUM(case WHEN Behavior_type = 'fav' THEN 1 ELSE 0 END) as '收藏',
	SUM(case WHEN Behavior_type = 'cart' THEN 1 ELSE 0 END) as '加购物车',
	SUM(case WHEN Behavior_type = 'buy' THEN 1 ELSE 0 END) as '购买'
	FROM data_random
	GROUP BY User_ID
	ORDER BY User_ID DESC) a
WHERE 收藏!=0 and 加购物车!=0;
```

