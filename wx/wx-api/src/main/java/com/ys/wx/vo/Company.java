package com.ys.wx.vo;

import java.io.Serializable;

import com.mhome.tools.common.BaseModel;

/**
 * 公司类
 * @author CeaserWang
 * @date 2017-08-28 16:43:59
 */
public class Company extends BaseModel implements Serializable{
    
    /**
     * 编码
     */
    private String id;
    
    /**
     * 父级编码
     */
    private String pid;
    
    /**
     * 公司id
     */
    private String companyId;
    
    /**
     * 编号
     */
    private String code;
    
    /**
     * 父级编号
     */
    private String pcode;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 排序
     */
    private String orderNo;
    
    /**
     * 删除状态(0:未删除;1:已删除)
     */
    private Integer delFlag;
    
    /**
     * 是否修改(0:未修改;1:已修改)
     */
    private Integer syn;
	
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
	
    public String getId()
    {
        return id;
    }
        
    public void setId(String id)
    {
        this.id = id;
    }
        
    public String getPid()
    {
        return pid;
    }
        
    public void setPid(String pid)
    {
        this.pid = pid;
    }
        
    public String getCode()
    {
        return code;
    }
        
    public void setCode(String code)
    {
        this.code = code;
    }
        
    public String getPcode()
    {
        return pcode;
    }
        
    public void setPcode(String pcode)
    {
        this.pcode = pcode;
    }
        
    public String getName()
    {
        return name;
    }
        
    public void setName(String name)
    {
        this.name = name;
    }
        
        
    public Integer getDelFlag()
    {
        return delFlag;
    }
        
    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }
        
    public Integer getSyn()
    {
        return syn;
    }
        
    public void setSyn(Integer syn)
    {
        this.syn = syn;
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
        
}
