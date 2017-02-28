package com.ys.wx.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * Title: 数据同步记录类
 * Description:
 * Author: Ceaser wang
 * Since: 2016-11-23 10:43:57
 * Version:1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public class SynDataRecord implements Serializable {


    /**
     *
     */

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 类型 (1 公司 2 部门 3人员 4 人员级别 5人员类型  枚举) @see SynDataRecordEnum
     */
    private Integer type;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    private Date operateTime;

	 /* //临时变量-s
    private String startTime; 
    private String endTime;
    //临时变量-e
    
    public String getStartTime() {
    	return startTime;
    }
    
    public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
    
    public String getEndTime() {
    	return endTime;
    }
    
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

}
