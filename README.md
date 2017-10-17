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
```sql
CREATE TABLE `tbl_wx_syn_data_record` (
   `id` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'ID',
   `type` int(2) DEFAULT NULL COMMENT '类型 (1)',
   `start_time` datetime DEFAULT NULL COMMENT '开始时间',
   `end_time` datetime DEFAULT NULL COMMENT '结束时间',
   `operator` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '操作人',
   `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据同步记录表'
 ```
 日常从外部数据拉取数据是增量方式（调度任务），在此工程外部数据的场景是每天从其他系统根据时间拉取数据，本次开始时间(start_time)即上次的结束时间(end_time)。
 
二、第二期

第一个版本与2.0版本切换步骤：
第一步，数据表初始化：
```sql
CREATE TABLE `tbl_wx_company` (
`id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '编码',
`pid` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父级编码',
`code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
`pcode` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父级编号',
`name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
`order_no` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '排序',
`del_flag` int(1) DEFAULT NULL COMMENT '删除状态(0:未删除;1:已删除)',
`syn` int(1) DEFAULT NULL COMMENT '是否修改(0:未修改;1:已修改)',
`create_time` datetime DEFAULT NULL,
`update_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
```sql
CREATE TABLE `tbl_wx_department` (
`id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '编码',
`pid` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父级编码',
`company_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '公司id',
`code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '编号',
`pcode` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '父级编号',
`name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
`order_no` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '排序',
`del_flag` int(1) DEFAULT NULL COMMENT '删除状态(0:未删除;1:已删除)',
`syn` int(1) DEFAULT NULL COMMENT '是否修改(0:未修改;1:已修改)',
`create_time` datetime DEFAULT NULL,
`update_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
```sql
CREATE TABLE `tbl_wx_person` (
`id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '主键',
`name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
`idcard` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT '账号',
`sex` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
`weixin` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '微信号',
`mobile` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
`email` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
`company_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '所在公司编码',
`dept_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '所在部门编码',
`dept_code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '所在部门',
`position` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '职位',
`office_addr` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '所在办公室',
`full_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '全称',
`short_name` varchar(16) COLLATE utf8_bin DEFAULT NULL COMMENT '简称',
`order_no` int(12) DEFAULT NULL COMMENT '排序号',
`is_visable` varchar(8) COLLATE utf8_bin DEFAULT NULL COMMENT '是否可见',
`phone` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT '固定电话',
`short_phone` varchar(9) COLLATE utf8_bin DEFAULT NULL COMMENT '座机短号',
`no` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '工号',
`short_mobile` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '手机短号',
`syn` int(1) DEFAULT NULL COMMENT '是否修改(0:未修改;1:已修改)',
`del_flag` int(1) DEFAULT NULL COMMENT '删除状态(0:删除;1:未删除)',
`create_time` datetime DEFAULT NULL,
`update_time` datetime DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
--  唯一索引 `no`
```sql
CREATE TABLE `sequence` (
`seq_name` varchar(50) COLLATE utf8_bin NOT NULL,
`current_val` int(11) NOT NULL,
`increment_val` int(11) NOT NULL DEFAULT '1',
PRIMARY KEY (`seq_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
```sql
INSERT INTO sequence(seq_name,current_val,increment_val)  VALUES('GLOBAL',1,1);
```
```sql
create function currval(v_seq_name VARCHAR(50))   
returns integer  
begin      
	declare value integer;       
	set value = 0;       
	select current_val into value  from sequence where seq_name = v_seq_name; 
   return value; 
end;
```
```sql
create function nextval (v_seq_name VARCHAR(50))
	returns integer
begin
    update sequence set current_val = current_val + increment_val  where seq_name = v_seq_name;
	return currval(v_seq_name);
end;
```

第二步：
正式环境建立mq队列: `ys.wechat.userdepartcompany.queue`
mq队列监听地址改为正式环境地址，本地数据库改为正式环境数据库，即将工程配置改为正式环境配置。
全量拉取MDM数据：

第三步、
去掉代码里边的200条限制。（FirstPollMDMDataProgress line :469）
启动微信工程监听队列，在MDM做全量下发。
首先是公司，

设置公司表【亚厦控股】的pcode为1.
紧接着是部门和人员的同步。

第四步：运行`FirstPollMDMDataProgress.useTempDeptUseFile`做首次切换。

第五步：
`com.ys.wx.api.impl.WxJobApiImpl.processLocalToWeiXin()`作为后台调度，每天调度一次，作用是将一天积累的修改同步到微信企业公众号上去。









