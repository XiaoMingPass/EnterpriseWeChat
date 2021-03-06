# EnterpriseWeChat
同步企业微信号通讯录数据

### 项目背景
1. 原企业微信号通讯录中已存在数据（与新数据结构，信息不一致）；
2. 通讯录中字段有扩展字段

### 项目需求
1. 从数据源拉取数据，同步更新至企业号；
2. 每天定时同步数据；
3. 不能改变人员关注状态

### 项目分析
>根据微信提供[开发文档](http://qydev.weixin.qq.com/wiki/index.php)，[通讯录管理](http://qydev.weixin.qq.com/wiki/index.php?title=%E5%BC%82%E6%AD%A5%E4%BB%BB%E5%8A%A1%E6%8E%A5%E5%8F%A3)模块API可以实现
* 注意：部门只能有`覆盖`

#### 一、第一期  
* 数据切换，数据自动同步  

由于存在历史数据，因此项目需要做数据切换，切换成功后才可以进行每天定时同步数据

##### 首次切换
1. 备份数据
    * 创建临时部门
    * 获取人员数据，并重新设置`所在部门`为对应的临时部门，生成CSV人员表
    * 上传人员CSV数据表（实质：统一将人员移动至临时部门）
2. 数据融合  
    由于原数据部门不正确，故以新数据部门为准
    * 新部门数据生成CSV部门数据表
    * 获取新的人员数据，并和之前获取的历史人员数据进行做比较匹配融合数据
        1. 如果新数据中存在的人，历史数据不存在，则保留
        2. 如果新数据中存在的人，历史数据也存在，以新数据人员所在部门为准，其他扩展字段数据也以新数据为准，如果扩宽字段新数据没有，则以历史数据扩展字段为准
    * 将融合后的数据生成CSV人员表  
3. 同步数据 
    * 上传部门CSV表
    * 部门数据同步成功后，上传人员CSV表  
    
##### 正常同步
1. 同步部门数据

2. 同步人员数据
    * 增量获取新数据，并上传增量生成人员表  
    
注意：该方案，适用于`原历史数据与新数据相差比较大`

##### 数据库设计
CREATE TABLE `tbl_wx_syn_data_record` (
   `id` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'ID',
   `type` int(2) DEFAULT NULL COMMENT '类型 (1)',
   `start_time` datetime DEFAULT NULL COMMENT '开始时间',
   `end_time` datetime DEFAULT NULL COMMENT '结束时间',
   `operator` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人',
   `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据同步记录表'
 
 日常从外部数据拉取数据是增量方式（调度任务），在此工程外部数据的场景是每天从其他系统根据时间拉取数据，本次开始时间(start_time)即上次的结束时间(end_time)。
 
二、第二期
* 数据自动同步，并清理已离职人员
* 数据同步完成，并发送消息给管理员，通知同步结果

稍后上传源码

