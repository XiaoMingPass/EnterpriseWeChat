package com.ys.wx.vo;

import java.io.Serializable;

import com.mhome.tools.common.BaseModel;

/**
 * 人员类
 * @author CeaserWang
 * @date 2017-08-28 16:45:45
 */
public class Person extends BaseModel implements Serializable{
    
    /**
     * 主键
     */
    private String id;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 账号
     */
    private String idcard;
    
    /**
     * 性别
     */
    private String sex;
    
    /**
     * 微信号
     */
    private String weixin;
    
    /**
     * 手机号
     */
    private String mobile;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 所在公司编码
     */
    private String companyId;
    
    /**
     * 所在部门编码
     */
    private String deptId;
    
    /**
     * 所在部门编号
     */
    private String deptCode;
    
    /**
     * 职位
     */
    private String position;
    
    /**
     * 所在办公室
     */
    private String officeAddr;
    
    /**
     * 全称
     */
    private String fullName;
    
    /**
     * 简称
     */
    private String shortName;
    
    /**
     * 排序号
     */
    private Integer orderNo;
    
    /**
     * 是否可见
     */
    private String isVisable;
    
    /**
     * 固定电话
     */
    private String phone;
    
    /**
     * 座机短号
     */
    private String shortPhone;
    
    /**
     * 工号
     */
    private String no;
    
    /**
     * 手机短号
     */
    private String shortMobile;
    
    /**
     * 是否修改(0:未修改;1:已修改)
     */
    private Integer syn;
    
    /**
     * 删除状态(0:删除;1:未删除)
     */
    private Integer delFlag;
	
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
        
    public String getName()
    {
        return name;
    }
        
    public void setName(String name)
    {
        this.name = name;
    }
        
    public String getIdcard()
    {
        return idcard;
    }
        
    public void setIdcard(String idcard)
    {
        this.idcard = idcard;
    }
        
    public String getSex()
    {
        return sex;
    }
        
    public void setSex(String sex)
    {
        this.sex = sex;
    }
        
    public String getWeixin()
    {
        return weixin;
    }
        
    public void setWeixin(String weixin)
    {
        this.weixin = weixin;
    }
        
    public String getMobile()
    {
        return mobile;
    }
        
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
        
    public String getEmail()
    {
        return email;
    }
        
    public void setEmail(String email)
    {
        this.email = email;
    }
        
    public String getDeptCode()
    {
        return deptCode;
    }
        
    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode;
    }
        
    public String getPosition()
    {
        return position;
    }
        
    public void setPosition(String position)
    {
        this.position = position;
    }
        
    public String getOfficeAddr()
    {
        return officeAddr;
    }
        
    public void setOfficeAddr(String officeAddr)
    {
        this.officeAddr = officeAddr;
    }
        
    public String getFullName()
    {
        return fullName;
    }
        
    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }
        
    public String getShortName()
    {
        return shortName;
    }
        
    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }
        
        
    public String getIsVisable()
    {
        return isVisable;
    }
        
    public void setIsVisable(String isVisable)
    {
        this.isVisable = isVisable;
    }
        
    public String getPhone()
    {
        return phone;
    }
        
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
        
    public String getShortPhone()
    {
        return shortPhone;
    }
        
    public void setShortPhone(String shortPhone)
    {
        this.shortPhone = shortPhone;
    }
        
    public String getNo()
    {
        return no;
    }
        
    public void setNo(String no)
    {
        this.no = no;
    }
        
    public String getShortMobile()
    {
        return shortMobile;
    }
        
    public void setShortMobile(String shortMobile)
    {
        this.shortMobile = shortMobile;
    }
        
    public Integer getSyn()
    {
        return syn;
    }
        
    public void setSyn(Integer syn)
    {
        this.syn = syn;
    }
        
    public Integer getDelFlag()
    {
        return delFlag;
    }
        
    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", idcard=" + idcard + ", sex=" + sex + ", weixin=" + weixin
				+ ", mobile=" + mobile + ", email=" + email + ", companyId=" + companyId + ", deptId=" + deptId
				+ ", deptCode=" + deptCode + ", position=" + position + ", officeAddr=" + officeAddr + ", fullName="
				+ fullName + ", shortName=" + shortName + ", orderNo=" + orderNo + ", isVisable=" + isVisable
				+ ", phone=" + phone + ", shortPhone=" + shortPhone + ", no=" + no + ", shortMobile=" + shortMobile
				+ ", syn=" + syn + ", delFlag=" + delFlag + "]";
	}
        
}
