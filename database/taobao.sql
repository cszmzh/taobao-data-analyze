/*
 Navicat Premium Data Transfer

 Source Server         : txyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50650 (5.6.50-log)
 Source Host           : 175.178.56.73:3306
 Source Schema         : taobao

 Target Server Type    : MySQL
 Target Server Version : 50650 (5.6.50-log)
 File Encoding         : 65001

 Date: 15/02/2023 01:30:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chart
-- ----------------------------
DROP TABLE IF EXISTS `chart`;
CREATE TABLE `chart` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '图表编号',
  `name` varchar(20) NOT NULL COMMENT '图表标题',
  `chart_des` varchar(200) NOT NULL COMMENT '图表描述/介绍',
  `creator` int(8) NOT NULL COMMENT '创建者用户编号',
  `detail` varchar(3000) NOT NULL COMMENT '详细说明',
  `power_bi_key` varchar(1000) NOT NULL COMMENT '图表标识符',
  `sql_text` varchar(3000) DEFAULT NULL COMMENT '数据处理语句',
  `view_status` smallint(2) NOT NULL COMMENT '0-仅自己可看，1-公开',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cid`) USING BTREE,
  KEY `chart_ibfk1` (`creator`) USING BTREE,
  CONSTRAINT `chart_ibfk1` FOREIGN KEY (`creator`) REFERENCES `user` (`uid`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of chart
-- ----------------------------
BEGIN;
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (1, 'PV和UV统计（根据每天日期）', ' 统计21/11/25至22/12/3每日的PV与UV，进行数据分析与提出优化建议。', 10000, 'PV是网站分析的一个术语，用以衡量网站用户访问的网页的数量。对于广告主，PV值可预期它可以带来多少广告收入。一般来说，PV与来访者的数量成正比，但是PV并不直接决定页面的真实来访者数量，如同一个来访者通过不断的刷新页面，也可以制造出非常高的PV。\n\nUV（unique visitor）即独立访客数，指访问某个站点或点击某个网页的不同IP地址的人数。在同一天内，UV只记录第一次进入网站的具有独立IP的访问者，在同一天内再次访问该网站则不计数。UV提供了一定时间内不同观众数量的统计指标，而没有反应出网站的全面活动。\n\n用户在电商平台会产生不同操作，包括游览、收藏、加入购物车和购买。在数据集中，表示行为的字段为Behavior_type。要统计PV和UV数量，需要利用SQL中的count函数，对用户记录中Behavior_type的值为PV的记录条数进行统计，即可得到PV量。随后，使用count函数和distinct关键字（用来查询用户ID不重复记录的条数）统计用户记录数量即可得到UV量。\n\n上方红色折线图代表的是每日PV量走势，下方蓝色折线图代表的是每日UV量走势。', 'eyJrIjoiYzEzNGUzMjItZjZmNi00NjA3LWJiZGItNWUwODY2NjQ5Mjk4IiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT Buy_date,count(User_ID) as pv,count(DISTINCT User_ID) as uv from data_random GROUP BY Buy_date;', 2, '2022-05-31 18:18:07', '2022-02-08 14:02:04');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (14, 'PV和UV统计（根据小时）', '通过分析PV和UV在一天内不同时段的波动，找出用户活跃时间段。', 10000, '字段说明：Buy_hour（购买小时）/ User_ID（用户标识）\n数据表：data_random\n\n用户在电商平台会产生不同操作，包括游览、收藏、加入购物车和购买。在数据集中，表示行为的字段为Behavior_type。要统计PV和UV数量，需要利用SQL中的count函数，对用户记录中Behavior_type的值为PV的记录条数进行统计，即可得到PV量。随后，使用count函数和distinct关键字（用来查询用户ID不重复记录的条数）统计用户记录数量即可得到UV量。\n\n上方红色折线图代表的是每日PV量走势，下方蓝色折线图代表的是每日UV量走势。', 'eyJrIjoiMjhiMzEyZWYtZjhkZS00Y2IwLTlkNzctN2M5ZDA5ODNlY2M0IiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT HOUR(Buy_hour),count(User_ID) as pv,count(DISTINCT User_ID) as uv from data_random GROUP BY HOUR(Buy_hour);', 2, '2022-05-31 18:04:54', '2022-03-17 17:12:43');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (15, '每天人均访问页面次数', '通过条形图展示。', 10002, '平均访问次数（Visits per Visitor）是指在一定期间内，平均每个用户的访问次数。计算方法是用指定期间内的总访问数除以此期间内的唯一访问者（Unique Visitor）数。', 'eyJrIjoiMmNlZTBhNjgtODMwNS00YzZkLWE4ZTgtMTM1N2EyOWY1MTZlIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT round((pv/uv)/8,2) as 每天人均访问页面 from (SELECT Buy_date,count(User_ID) as pv,count(DISTINCT User_ID) as uv from data_random) sub', 2, '2022-04-14 10:45:45', '2022-03-17 18:00:22');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (16, '跳失率分析', '可以分析得出商品总体情况，用户滞留情况。', 10000, '跳失率是一个被广泛使用的衡量网页吸引力的指标，计算方法是用户从某个界面进入，只访问了当前页面就离开的访问次数占总访问次数的百分比。跳失率越低，说明该页面的内容更受大众欢迎[17]。在本次分析中，访问的页面即商品页面，计算公式为：在某个时间段，跳失率=只有游览行为的用户/总用户数。\n\n在数据集中，只需使用SQL统计没有点击收藏、加入购物车或购买的用户，即Behavior_type字段中，没有出现值为fav、cart和buy的用户数量，然后除以总用户数，得到的跳失率。', 'eyJrIjoiZGIwNDM1MWItNDg0ZC00Zjg1LTljYWYtYTMyOTM0MzAwZGNlIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT concat(round(SUM(IF(t.fav_num=0 AND t.cart_num=0 AND t.buy_num=0,1,0))/COUNT(user_id)*100,2),\'%\') AS \'跳失率\' FROM( SELECT user_id, SUM(IF(Behavior_type=\'fav\',1,0)) fav_num, SUM(IF(Behavior_type=\'cart\',1,0)) cart_num, SUM(IF(Behavior_type=\'buy\',1,0)) buy_num FROM data_random GROUP BY user_id ) t', 2, '2022-05-31 18:19:00', '2022-03-18 18:14:50');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (17, 'RFM用户分类', '根据RFM分析，得出四类用户，进行商业决策。', 10004, 'R（Recency）：客户最近一次交易时间的间隔。R值越大，表示客户交易发生的日期越久，反之则表示客户交易发生的日期越近。\nF（Frequency）：客户在最近一段时间内交易的次数。F值越大，表示客户交易越频繁，反之则表示客户交易不够活跃。\nM（Monetary）：客户在最近一段时间内交易的金额。M值越大，表示客户价值越高，反之则表示客户价值越低。\n\nRFM分析的主要作用：\n1. 识别优质客户。可以指定个性化的沟通和营销服务，为更多的营销决策提供有力支持。\n2. 能够衡量客户价值和客户利润创收能力。', 'eyJrIjoiM2QyNjAzMDAtMTZkZi00MDc5LWJjNDYtN2RkNmJhZTk4MDg4IiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', '# 计算最近交易时间lasttime, 最后一次购买距今几天recency。2021-12-07是指本项目的分析日期，在实际项目中可以使用curdate()来返回当前日期。\ncreate view recency as \nselect User_ID, max(Buy_date) as \'lasttime\', datediff(\'2021-12-07\', max(Buy_date)) as recency\nfrom data_random where Behavior_type = \'buy\' \ngroup by User_ID order by recency desc;\n\n# 计算购买频率frequency\ncreate view frequency as \nselect User_ID, count(Behavior_type) as frequency \nfrom data_random where Behavior_type = \'buy\' \ngroup by User_ID order by frequency desc;\n\n# 对recency从1-5划分等级R，可以根据业务理解相应修改。本项目选择3、6、9、12、更久。\ncreate view r等级 as \nselect *, (case \nwhen recency<=3 then 5\nwhen recency<=6 then 4\nwhen recency<=9 then 3\nwhen recency<=12 then 2\nelse 1 end) as R \nfrom recency;\n\n# 对frequency从1-5划分等级F，本项目选择2、4、6、8、更久。\ncreate view f等级 as \nselect *, (case \nwhen frequency<=2 then 1 \nwhen frequency<=4 then 2\nwhen frequency<=6 then 3\nwhen frequency<=8 then 4\nelse 5 end) as F \nfrom frequency;\n\n# 计算R/F/M,本项目无M，省略\ncreate view rfm as \nselect User_ID, \nmax(Buy_date) as \'lasttime\', datediff(\'2021-12-07\', max(Buy_date)) as recency,\ncount(behavior_type) as frequency,\n(case \nwhen datediff(\'2021-12-07\', max(Buy_date))<=3 then 5\nwhen datediff(\'2021-12-07\', max(Buy_date))<=6 then 4\nwhen datediff(\'2021-12-07\', max(Buy_date))<=9 then 3\nwhen datediff(\'2021-12-07\', max(Buy_date))<=12 then 2\nelse 1 end) as R ,\n(case \nwhen count(Behavior_type)<=2 then 1 \nwhen count(Behavior_type)<=4 then 2\nwhen count(Behavior_type)<=6 then 3\nwhen count(Behavior_type)<=8 then 4\nelse 5 end) as F \nfrom data_random where Behavior_type = \'buy\' \ngroup by User_ID;\n\n# 计算平均值\nselect avg(F) as F平均值 from rfm; # 1.4768\nselect avg(R) as R平均值 from rfm; # 2.6031\n\n# 没有M，建立在购买量M大的前提下，对用户做出以下4类分类\ncreate view rfm用户分类 as \nselect *, \n(case \nwhen R>2.6031 and F>1.4768 then \'重要价值用户\'\nwhen R>2.6031 and F<1.4768 then \'重要深耕用户\'\nwhen R<2.6031 and F>1.4768 then \'重要唤回用户\'\nwhen R<2.6031 and F<1.4768 then \'重要挽留用户\'\nend) as 用户分类 \nfrom rfm;\n\nselect 用户分类 from rfm用户分类;', 0, '2022-04-19 14:53:39', '2022-03-19 16:46:37');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (18, '游览量Top10 - 商品类目', '通过游览量、商品数量、购买量榜单，分析得到较为热门的产品类别，对商业决策有辅助性帮助。', 10005, '分析Top10的主要功能是什么?\n1、市场趋势：搜索词的搜索趋势及其人群特征，一次最多可对比三个词。\n2、市场细分：搜索词的类目分布，及近一个月成交人群的特征。并且可以查询特定人群的购物偏好。\n3、排行榜：Top20类目的热搜词，及子类目、品牌成交排行。\n4、其实，Top榜单就是帮助相关行业人分析行业数据提供的数据，同时也给到消费者最直观的商品排行，这样也有利于用户选择产品，所以，能够上榜的商家也非常有实力。\n5、至少平台会把大多数的资源推给你，消费者通常都喜欢锦上添花，所以，卖的好，当然更加受到大家的欢迎。\n\n字段说明：\nCategory_ID：商品类目代码', 'eyJrIjoiZTYxMjU0YjctZmY4NS00MGIyLTljMjYtYmJiMjA0MWYzYzJkIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'create view pv_top10(cate_id,浏览量,购买量,转化率,商品数量) as\nSELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),\'%\') as 转化率,商品数量\nFROM\n(SELECT Category_ID,count(*)as 浏览量,\nsum(case when Behavior_type=\'buy\' then 1 else 0 end) as 购买量,\ncount(DISTINCT Item_ID) as 商品数量\nfrom data_random\nGROUP BY Category_ID\nORDER BY 浏览量 desc\nLIMIT 10) sub;\n\nselect * from pv_top10;', 1, '2022-05-15 22:17:32', '2022-04-29 11:26:33');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (22, '转化率Top10 - 商品类目', '通过分析Top10转化率，有效改善商品页面内容。', 10000, '转化率是指某一反应物转化的百分率或分率，转化物是针对反应物而言的。\n商品转化率，就是所有到达该商品页面并产生购买行为的人数和所有到达该商品页面的人数的比率。\n\nTop10浏览量商品类目往往是需求量最大商品类目，同时也是竞争商品最多的商品类目，这符合我们日常的购买经验，有更多商品数量，顾客往往会花费更多的时间进行选择，从而导致了高浏览量，低转化率。\n\n作为对比转化率top10商品类目，竞争商品数量较小，顾客可能不需要那么多时间对比选择，从而有更高的转化率，而购买量高的商品则是结合转化率和浏览量综合排名的结果。', 'eyJrIjoiOTVmZjNlMDYtNjBhZi00YzMzLThmYzktZmFlNTZjNzAyYTgyIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'create view trans_top10(Category_ID,浏览量,购买量,转化率,商品数量)as\nSELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),\'%\') as 转化率,商品数量\nFROM\n(SELECT Category_ID,count(*) as 浏览量,\nsum(case when Behavior_type=\'buy\' then 1 else 0 end) as 购买量,\ncount(DISTINCT Item_ID) as 商品数量\nfrom data_random\nGROUP BY Category_ID) sub\nORDER BY 转化率 DESC\nLIMIT 10；\n\nselect * from trans_top10;', 1, '2022-04-29 23:24:25', '2022-04-29 23:24:25');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (23, '购买量Top10 - 商品类目', '通过分析Top10购买量，得到平台客户关注热门商品的趋势。', 10001, '购买量往往指某一次购买行为中的量，对应于交换发生的那个时点，即属于一个存量。\n最高排名是指一类对象的前几名或前100名的排名。\n排名是一种客观实力的反映，相互比较。Top代表含金量最高的几个。', 'eyJrIjoiYTYxN2ZiMGEtNjNhZi00NjE0LWI3OTQtMGFhYWJkYzk2MzJkIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'create view buy_top10(cate_id,浏览量,购买量,转化率,商品数量)as\nSELECT Category_ID,浏览量,购买量,concat(round(购买量*100/浏览量,3),\'%\') as 转化率,商品数量\nFROM\n(SELECT Category_ID,count(*) as 浏览量,\nsum(case when Behavior_type=\'buy\' then 1 else 0 end) as 购买量,\ncount(DISTINCT item_id) as 商品数量\nfrom data_random\nGROUP BY Category_ID\nORDER BY 购买量 desc\nLIMIT 10) data_random', 2, '2022-05-15 22:17:34', '2022-05-14 23:25:54');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (24, '用户行为（按日期）', '通过用户行为分析，得出平台用户的行为趋势。', 10002, '在数据分析的大框架下，通过对用户行为监测获得的数据进行分析研究的行为归结于用户行为分析。用户行为分析可以让产品更加详细、清楚地了解用户的行为习惯，从而找出网站、app、推广渠道等产品存在的问题，有助于产品发掘高转化率页面，让产品的营销更加精准、有效，提高业务转化率。', 'eyJrIjoiNzA2OGYzZDUtNjBkNi00ZWJmLWFmNWItMTMyZTNiMmZjM2VhIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT Buy_date,count(Behavior_type) as \'用户行为总数\',\nSUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\nSUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\nSUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\nSUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\nFROM data_random\nGROUP BY Buy_date\nORDER By Buy_date;', 2, '2022-05-15 22:17:39', '2022-05-15 10:35:38');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (25, '用户行为（按小时）', '通过用户行为分析，得出平台用户的行为趋势。', 10003, '在数据分析的大框架下，通过对用户行为监测获得的数据进行分析研究的行为归结于用户行为分析。用户行为分析可以让产品更加详细、清楚地了解用户的行为习惯，从而找出网站、app、推广渠道等产品存在的问题，有助于产品发掘高转化率页面，让产品的营销更加精准、有效，提高业务转化率。', 'eyJrIjoiMGJhNTFiM2QtMzNhZS00Yzc3LWI4ZWItOTE3NDkwZDA2N2IxIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'SELECT Time,count(Behavior_type) as \'用户行为总数\',\nSUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\nSUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\nSUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\nSUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\nFROM data_random\nGROUP BY Time\nORDER By Time;', 2, '2022-05-15 22:17:40', '2022-05-15 11:08:42');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (26, '留存率分析', '通过分析留存率，观察平台促销活动等因素对用户留存的影响。', 10004, '留存率是用于反映网站、互联网应用或网络游戏的运营情况的统计指标，其具体含义为在统计周期（周/月）内，每日活跃用户数在第N日仍启动该App的用户数占比的平均值。\n\n本分析采用的数据集天数较少，分析结果可能不准确。', 'eyJrIjoiNTJkOTM5M2ItYjUxYi00MmNjLWFhYWQtYmNkZmM5Y2Q2ZjVhIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', 'create view time_inter as \nselect a.*, b.firstday, datediff(a.Buy_date, b.firstday) as day_diff from \n(select User_ID, Buy_date from data_random group by User_ID, Buy_date) as a,\n(select User_ID, min(Buy_date) as firstday from data_random group by User_ID) as b\nwhere a.User_ID = b.User_ID order by User_ID, Buy_date;\n\n# 搭建留存天数模型retention_day，第一次活跃用户会持续活跃几天\ncreate view retention_day as \nselect firstday,\nsum(case when day_diff=0 then 1 else 0 end) as day_0,\nsum(case when day_diff=1 then 1 else 0 end) as day_1,\nsum(case when day_diff=2 then 1 else 0 end) as day_2,\nsum(case when day_diff=3 then 1 else 0 end) as day_3,\nsum(case when day_diff=4 then 1 else 0 end) as day_4,\nsum(case when day_diff=5 then 1 else 0 end) as day_5,\nsum(case when day_diff=6 then 1 else 0 end) as day_6,\nsum(case when day_diff=7 then 1 else 0 end) as day_7,\nsum(case when day_diff=15 then 1 else 0 end) as day_15,\nsum(case when day_diff=30 then 1 else 0 end) as day_30\nfrom time_inter\ngroup by firstday\norder by firstday;\n\n# 搭建留存率模型retention_rate\ncreate view retention_rate as \nselect firstday, day_0,\nconcat(format(day_1/day_0*100, 2), \'%\') as day_1,\nconcat(format(day_2/day_0*100, 2), \'%\') as day_2,\nconcat(format(day_3/day_0*100, 2), \'%\') as day_3,\nconcat(format(day_4/day_0*100, 2), \'%\') as day_4,\nconcat(format(day_5/day_0*100, 2), \'%\') as day_5,\nconcat(format(day_6/day_0*100, 2), \'%\') as day_6,\nconcat(format(day_7/day_0*100, 2), \'%\') as day_7,\nconcat(format(day_15/day_0*100, 2), \'%\') as day_15,\nconcat(format(day_30/day_0*100, 2), \'%\') as day_30\nfrom retention_day;', 2, '2022-05-15 22:17:44', '2022-05-15 17:22:09');
INSERT INTO `chart` (`cid`, `name`, `chart_des`, `creator`, `detail`, `power_bi_key`, `sql_text`, `view_status`, `update_time`, `create_time`) VALUES (27, '用户留存漏斗图', '通过分析不同的用户留存漏斗图，得到不同环节的转化率。', 10005, '漏斗图是由 Light 与 Pillemer于1984年所提出，并由 Egger 等人深入探讨，是Meta分析的有用工具。漏斗图结合相关的统计检验，在系统评价中检查研究是否存在报告偏倚的可能性。\n\n本分析包括【点击-购买】【点击-收藏-购买】【点击-加购物车-购买】【点击-收藏且加购物车-购买】共四个环节的漏斗图。', 'eyJrIjoiMWI5ZTkwMWMtMjllNC00MjVhLWFjNmEtNmVjM2I2ZTQ1Y2NiIiwidCI6IjY3ZThjOTUyLThhZjgtNDgyMC05N2RlLTBkZWZkMmRmM2FjMyIsImMiOjEwfQ%3D%3D', '# 点击用户数\nSELECT\nSUM(case WHEN 点击量>0 THEN 1 else 0 END) as 点击\nFROM(\n	SELECT User_ID,count(Behavior_type) as \'用户行为总数\',\n	SUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\n	SUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\n	SUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\n	SUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\n	FROM data_random\n	GROUP BY User_ID\n	ORDER BY User_ID DESC) a;\n\n# 第一个图\nSELECT\nSUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买\nFROM(\n	SELECT User_ID,count(Behavior_type) as \'用户行为总数\',\n	SUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\n	SUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\n	SUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\n	SUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\n	FROM data_random\n	GROUP BY User_ID\n	ORDER BY User_ID DESC) a\nWHERE 收藏=0 and 加购物车=0;\n\n# 第二个图\nSELECT\nSUM(case WHEN 收藏>0 THEN 1 else 0 END) as 收藏,\nSUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买\nFROM(\n	SELECT User_ID,count(Behavior_type) as \'用户行为总数\',\n	SUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\n	SUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\n	SUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\n	SUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\n	FROM data_random\n	GROUP BY User_ID\n	ORDER BY User_ID DESC) a\nWHERE 收藏!=0 and 加购物车=0;\n\n# 第三个图\nSELECT\nSUM(case WHEN 加购物车>0 THEN 1 else 0 END) as 加购物车,\nSUM(case WHEN 购买>0 THEN 1 else 0 END) as 购买\nFROM(\n	SELECT User_ID,count(Behavior_type) as \'用户行为总数\',\n	SUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\n	SUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\n	SUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\n	SUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\n	FROM data_random\n	GROUP BY User_ID\n	ORDER BY User_ID DESC) a\nWHERE 收藏=0 and 加购物车!=0;\n\n# 第四个图\nSELECT\n(SUM(case WHEN 收藏>0 THEN 1 ELSE 0 END)+\nSUM(case WHEN 加购物车>0 THEN 1 ELSE 0 END)) as 收藏且加购物车,\nSUM(case WHEN 购买>0 THEN 1 ELSE 0 END) as 购买\nFROM(\n	SELECT User_ID,count(Behavior_type) as \'用户行为总数\',\n	SUM(case WHEN Behavior_type = \'pv\' THEN 1 ELSE 0 END) as \'点击量\',\n	SUM(case WHEN Behavior_type = \'fav\' THEN 1 ELSE 0 END) as \'收藏\',\n	SUM(case WHEN Behavior_type = \'cart\' THEN 1 ELSE 0 END) as \'加购物车\',\n	SUM(case WHEN Behavior_type = \'buy\' THEN 1 ELSE 0 END) as \'购买\'\n	FROM data_random\n	GROUP BY User_ID\n	ORDER BY User_ID DESC) a\nWHERE 收藏!=0 and 加购物车!=0;', 2, '2022-05-15 22:17:46', '2022-05-15 20:24:12');
COMMIT;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `dep_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '部门编号',
  `dep_name` varchar(15) NOT NULL COMMENT '部门名称',
  `dep_des` varchar(1000) NOT NULL COMMENT '部门描述/介绍',
  `dep_manager` int(8) DEFAULT NULL COMMENT '部门管理者',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`dep_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70082 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
BEGIN;
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (30000, '管理层', '管理层的职能就是利用人力和物质资源造就一家能创造经济价值的企业。在信息技术领域，管理层是介于数据和应用之间的介质和桥梁，通常上层自动应用或产品化所需的数据会直接由计算层调用。', 10001, '2022-05-06 16:39:36', '2022-03-28 11:42:55');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (40001, 'CDG-企业发展事务群', '为公司新业务孵化和新业态探索的平台，推动包括基础支付、金融应用在内的金融科技业务、广告营销服务和国际业务等领域的发展和创新。同时作为专业支持平台，为公司及各事业群提供战略规划、投资并购、投资者关系及国际传讯、市场公关等专业支持。', NULL, '2022-06-01 10:26:25', '2022-03-28 11:45:06');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (40002, 'CSIG-云与智慧产业事业群', '推进云与产业互联网战略，依托云、安全、人工智能等技术创新，打造智慧产业升级方案。探索用户与产业的创新互动，打通产业上下游不同企业，联动线上线下的场景与资源，助力零售、医疗、教育、交通等产业数字化升级，同时协助企业更智能地服务用户，构建连接用户与商业的智慧产业新生态。', NULL, '2022-06-01 10:26:26', '2023-02-08 13:41:51');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (50001, 'IEG-互动娱乐事业群', '发展网络游戏、电竞等互动娱乐业务，打造一个从策划、研发、发行，运营及营销的垂直生态链。致力为中国以及全球游戏用户创造高品质产品，并通过在线游戏，直播和线下电竞赛事联动用户，提升总体游戏体验。', NULL, '2022-06-01 10:26:28', '2023-02-08 13:42:20');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (60001, 'PCG-平台与内容事业群', '推进互联网平台和内容文化生态融合发展，整合QQ、QQ空间等社交平台，和应用宝、浏览器等流量平台，以及新闻资讯、视频、体育、直播、动漫、影业等内容平台，为内容生态创造更好的生长环境。同时，以技术驱动，推动IP 跨平台多形态发展，为更多用户创造多样化的优质数字内容体验。', NULL, '2022-04-14 10:32:18', '2023-02-08 13:42:41');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (70001, 'TEG-技术工程事业群', '为公司及各事业群提供技术及运营平台支持、研发管理、数据中心的建设与运营，并为用户提供全线产品的客户服务。作为运营着亚洲最大网络、服务器集群和数据中心的事业群，并牵头腾讯技术委员会，通过内部分布式开源协同，加强基础研发，建设技术中台等措施，支持业务创新。', NULL, '2022-04-14 10:32:44', '2023-02-08 13:43:03');
INSERT INTO `department` (`dep_id`, `dep_name`, `dep_des`, `dep_manager`, `update_time`, `create_time`) VALUES (70080, 'WXG-微信事业群', '搭建和运营微信生态体系，依托微信基础平台，以及微信公众号、小程序、微信支付、企业微信、微信搜索等开放平台，为各行各业的智慧化升级提供解决方案和连接能力。同时开发和运营包括邮箱、通讯录、微信读书等产品。', 10002, '2022-05-07 10:30:40', '2023-02-08 13:43:33');
COMMIT;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `jid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '职位编号',
  `job_name` varchar(30) NOT NULL COMMENT '职位名称',
  `job_des` varchar(1000) NOT NULL COMMENT '职位描述/介绍',
  `base_salary` varchar(10) NOT NULL COMMENT '基础工资',
  `dep_id` int(10) unsigned NOT NULL COMMENT '所属部门编号',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`jid`) USING BTREE,
  KEY `job_ibfk1` (`dep_id`) USING BTREE,
  CONSTRAINT `job_ibfk1` FOREIGN KEY (`dep_id`) REFERENCES `department` (`dep_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=700802 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of job
-- ----------------------------
BEGIN;
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (300009, 'CEO', '对公司的一切重大经营运作事项进行决策,包括对财务、经营方向、业务范围的增减等。', '50001', 30000, '2022-05-07 11:14:58', '2022-03-31 12:08:59');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (400011, '网络工程师', '掌握常见网络协议和相关的其他底层网络协议的全面知识。 ', '32000', 40001, '2022-05-28 17:33:46', '2022-02-08 13:45:25');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (400012, '视觉设计师', '设计的意义是什么？\r\n在这里，设计意味着触动亿万人的情感和引领互联网时尚潮流，\r\n如果你希望来到这个汇聚全国乃至全球在软件、网站、品牌领域的优秀视觉设计师、专家达人的地方，\r\n那么相信你将收获最有效的设计方法、最真诚的帮助以及最有价值的成长。\r\n美术、设计相关专业本科或研究生，综合素质扎实，热爱设计行业。', '10000', 40001, '2022-04-14 10:56:15', '2022-04-14 10:55:02');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (400021, '游戏客户端开发工程师', '对于创新和解决有挑战性的问题充满激情，有较强的学习能力、分析及解决问题能力。', '20000', 40002, '2022-04-14 10:39:12', '2022-03-20 19:23:16');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (400022, '财务分析员', 'C++/C#开发和调试经验者优先；', '9500', 40002, '2022-04-14 10:39:42', '2022-04-04 15:52:34');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (500011, '初级员工', 'Windows/macOS/Linux 下的网络编程经验；掌握 Windows/macOS/Linux 客户端开发、调试技能； ', '30000', 50001, '2022-04-14 10:39:10', '2022-03-20 19:23:30');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (500012, '测试与质量管理工程师', '需要运用自己的综合能力，发现预警产品质量存在的隐患风险，解决流程的低效问题。', '11000', 50001, '2022-04-14 10:56:51', '2022-04-14 10:56:51');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (600011, '初级工程师', '负责软件测试、搭建测试环境,按照测试流程、计划以及对产品特性的把握,编写测试案例,确保测试目的的达成。', '8000', 60001, '2022-04-14 10:35:29', '2022-03-20 19:23:47');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (600012, '云计算产品经理', '你将布局最强大的计算能力，构建最高效的全球网络，探索最前沿的技术应用，成为最懂客户的代言人，从零到一梳理出最有价值的产品文档，打造出最行业领先的产品。', '10000', 60001, '2022-04-14 10:57:55', '2022-04-14 10:57:55');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (700011, '高级工程师', '具有独立承担重要研究课题或有主持和组织重大工程项目设计的能力，能解决本专业领域的关键性技术问题。', '9000', 70001, '2022-04-14 10:36:27', '2022-03-20 19:23:59');
INSERT INTO `job` (`jid`, `job_name`, `job_des`, `base_salary`, `dep_id`, `update_time`, `create_time`) VALUES (700801, '初级推销员', '反应敏捷、表达能力强，具有较强的沟通能力及交际技巧，具有亲和力。', '15000', 70080, '2022-04-14 10:39:10', '2022-03-20 19:24:43');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(8) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(20) NOT NULL COMMENT '昵称',
  `real_name` varchar(20) NOT NULL COMMENT '使用者真实姓名',
  `jid` int(10) unsigned NOT NULL COMMENT '职位编号',
  `password` varchar(16) NOT NULL COMMENT '密码',
  `avatar` varchar(1000) DEFAULT 'https://yun.515code.com/static/visual-system/default.png' COMMENT '头像链接',
  `phone` varchar(11) NOT NULL COMMENT '手机号码',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `permission` varchar(10) NOT NULL COMMENT 'staff/manager/admin',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE KEY `nick_name` (`username`) USING BTREE,
  KEY `user_ibfk1` (`jid`) USING BTREE,
  CONSTRAINT `user_ibfk1` FOREIGN KEY (`jid`) REFERENCES `job` (`jid`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10006 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10000, 'admin', '管理员', 400022, '123456', 'https://yun.515code.com/prj/visual-system/19beb217-afcd-4e3d-83fe-39aad0119342.png', '18129986366', '798998087@qq.com', 'admin', '2023-02-14 23:34:47', '2022-02-08 13:58:51', '喜欢小众的东西，就不要害怕大众的眼光。');
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10001, 'manager', '部门', 400021, '123456', 'https://thirdqq.qlogo.cn/g?b=sdk&k=4GMA98lhJ5f5efmzica8mHw&s=140&t=1633434026', '18819022293', '29985986@qq.com', 'manager', '2022-04-17 23:34:47', '2022-02-19 18:01:36', '做人就是要像小饼干一样，干干脆脆，可甜可咸。');
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10002, 'staff', '员工', 400022, '123456', 'https://yun.515code.com/prj/visual-system/4f5f456a-9ec2-4a7a-aff7-eceb80582d8e.png', '13800138000', '29985986@qq.com', 'staff', '2023-02-14 23:34:44', '2022-04-01 14:18:20', '年纪不小了，该干嘛干嘛去，别一头扎进那美丽的忧伤，一边拼命往里钻还一边喊救命。');
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10003, 'banana', '张钊铭', 400012, '123456', 'https://thirdqq.qlogo.cn/g?b=sdk&k=rQ8qsP8SFj22QOuxibt5b0Q&s=140&t=1644590269', '18819022293', '29985986@qq.com', 'staff', '2022-04-16 18:11:32', '2022-04-01 17:13:10', '小时候不开心是意外，长大后不开心是常态。');
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10004, 'apple', '牛梓雨', 400011, 'hoS5gIVv', 'https://thirdqq.qlogo.cn/g?b=sdk&k=CHtTthuJ5icp4PiaXWXwetcA&s=140&t=1626063900', '18819022293', '29985986@qq.com', 'staff', '2022-04-14 10:44:40', '2022-04-01 17:17:00', '没有本事过上好日子，那就先降低标准把日子过好，等攒足本事之后再挑三拣四。');
INSERT INTO `user` (`uid`, `username`, `real_name`, `jid`, `password`, `avatar`, `phone`, `email`, `permission`, `update_time`, `create_time`, `signature`) VALUES (10005, 'orange', '快节奏', 600011, '123456', 'https://thirdqq.qlogo.cn/g?b=sdk&k=v4EXLXfDOJH1jBJsMa3Jlw&s=140&t=1567902785', '18819022293', '29985986@qq.com', 'staff', '2022-04-14 10:44:42', '2022-04-09 20:19:44', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
