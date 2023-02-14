# **电商用户行为分析及可视化系统**
### 摘要

移动终端和支付技术的进步推动着电商发展，用户规模的增长到达了瓶颈期，因此各电商企业开始把重心从“量”转为“质”的运营，为平台的发展制定更多精细化策略。针对以上应用需求，设计功能完备，交互性良好的电商用户行为分析及可视化系统有较大市场需求。

本系统基于游览器/服务器架构，采用流行的Vue前端框架 + Ant Design企业级UI组件，后台业务基于SpringBoot框架，数据库采用MySQL + Redis + MongoDB组合，使用分布式系统设计了“管理分析端”和“大屏端”。通过以上技术，实现了数据分析、部门管理、岗位管理、账号管理、大屏可视化等功能，通过采集阿里巴巴天池提供的百万条“淘宝用户行为数据集”，进行基于AARRR模型的分析、RFM分析与商品分析，目的是对用户进行游览、收藏、加入购物车和购买等行为进行数据分析。商家可以研究用户行为规律，确定各环节流失率，找到需改进环节。比较各群体行为差异，有利于进行差异化营销。找出明星产品，分析不同商品推广效率，优化店铺管理和商品展示等。对用户来说，购物的整个流程会被优化，由于商家提供的服务会更加完善，利于提高用户的网络购物体验。

### ABSTRACT

The progress of mobile terminal and payment technology promotes the development of e-commerce, and the growth of user scale has reached a bottleneck. Therefore, e-commerce enterprises begin to shift their focus from "quantity" to "quality" operation, and formulate more refined strategies for the development of the platform. In view of the above application requirements, the design of complete functions and good interaction of e-commerce user behavior analysis and visualization system has a large market demand.

The system is based on the tourist/server architecture, using the popular Vue front-end framework + Ant Design enterprise UI components, background business based on SpringBoot framework, database using MySQL + Redis + MongoDB combination. The "management analysis terminal" and "large screen terminal" are designed using distributed system. Through the above technologies, data analysis, department management, post management, account management, large screen visualization and other functions are realized. By importing millions of "Taobao user behavior data sets" provided by Alibaba Tianchi, AARRR model based analysis, RFM analysis and commodity analysis are carried out. The purpose is to conduct data analysis on users' behaviors such as visiting, collecting, adding to shopping cart and purchasing. Merchants can study the rules of user behavior, determine the loss rate of each link, and find the link to be improved. Comparing the behavioral differences of different groups is conducive to differentiated marketing. Identify star products, analyze the promotion efficiency of different products, optimize store management and product display, etc. For users, the whole shopping process will be optimized, and the services provided by merchants will be more perfect, which is conducive to improving users' online shopping experience.

### 指南

backend：后端文件，基于SpringBoot编写，建议使用IDEA导入

frontend：前端文件，包括bigscreen（大屏可视化）和analyze（后台分析管理系统）

database：包含MySQL数据库的设计与数据处理过程

本项目采用了PowerBI作为可视化工具，详见：https://app.powerbi.com/
