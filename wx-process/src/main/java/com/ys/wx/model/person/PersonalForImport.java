package com.ys.wx.model.person;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.ys.wx.common.BaseModel;

/**
 * 企业微信通讯录人员信息
 *
 * @author Jerry xu
 * @date 2017-01-11 16:39:16
 */
@XStreamAlias("psndocWXVO")
public class PersonalForImport extends BaseModel implements Serializable {

    @XStreamAlias("dirty")
    private String dirty;

    @XStreamAlias("status")
    private String status;

    //姓名
    @XStreamAlias("psn_name")
    private String psn_name;

    //账号==身份证号码
    @XStreamAlias("psn_id")
    private String psn_id;
    
    //微信号
    @XStreamAlias("weixinid")
    private String weixinid;

    //手机号
    @XStreamAlias("psn_mobile")
    private String psn_mobile;

    //邮箱
    @XStreamAlias("psn_email")
    private String psn_email;

    //所在部门
    @XStreamAlias("psn_deptcode")
    private String psn_deptcode;

    //职位
    @XStreamAlias("psn_postname")
    private String psn_postname;

    //固定电话
    @XStreamAlias("psn_officephone")
    private String psn_officephone;

    //工号
    @XStreamAlias("psn_code")
    private String psn_code;

    private String localoffice;//所在办公地
    private String fullName;//全称
    private String shortName;//简称
    private int orderNo; //排序号
    private String isvisible;//是否可见
    private String phone;//固定电话
    private String phoneShort;//座机短号
    private String mobileShortNo;//手机短号
    
    public String getDirty() {
        return dirty;
    }

    public void setDirty(String dirty) {
        this.dirty = dirty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPsn_code() {
        return psn_code;
    }

    public void setPsn_code(String psn_code) {
        this.psn_code = psn_code;
    }

    public String getPsn_deptcode() {
        return psn_deptcode;
    }

    public void setPsn_deptcode(String psn_deptcode) {
        this.psn_deptcode = psn_deptcode;
    }

    public String getPsn_email() {
        return psn_email;
    }

    public void setPsn_email(String psn_email) {
        this.psn_email = psn_email;
    }

    public String getPsn_id() {
        return psn_id;
    }

    public void setPsn_id(String psn_id) {
        this.psn_id = psn_id;
    }

    public String getPsn_mobile() {
        return psn_mobile;
    }

    public void setPsn_mobile(String psn_mobile) {
        this.psn_mobile = psn_mobile;
    }

    public String getPsn_name() {
        return psn_name;
    }

    public void setPsn_name(String psn_name) {
        this.psn_name = psn_name;
    }

    public String getPsn_officephone() {
        return psn_officephone;
    }

    public void setPsn_officephone(String psn_officephone) {
        this.psn_officephone = psn_officephone;
    }

    public String getPsn_postname() {
        return psn_postname;
    }

    public void setPsn_postname(String psn_postname) {
        this.psn_postname = psn_postname;
    }


	public String getWeixinid() {
		return weixinid;
	}

	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}

	public String getLocaloffice() {
		return localoffice;
	}

	public void setLocaloffice(String localoffice) {
		this.localoffice = localoffice;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getIsvisible() {
		return isvisible;
	}

	public void setIsvisible(String isvisible) {
		this.isvisible = isvisible;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobileShortNo() {
		return mobileShortNo;
	}

	public void setMobileShortNo(String mobileShortNo) {
		this.mobileShortNo = mobileShortNo;
	}

	public String getPhoneShort() {
		return phoneShort;
	}

	public void setPhoneShort(String phoneShort) {
		this.phoneShort = phoneShort;
	}

	@Override
	public String toString() {
		return "PersonalForImport [dirty=" + dirty + ", status=" + status + ", psn_name=" + psn_name + ", psn_id="
				+ psn_id + ", weixinid=" + weixinid + ", psn_mobile=" + psn_mobile + ", psn_email=" + psn_email
				+ ", psn_deptcode=" + psn_deptcode + ", psn_postname=" + psn_postname + ", psn_officephone="
				+ psn_officephone + ", psn_code=" + psn_code + ", localoffice=" + localoffice + ", fullName=" + fullName
				+ ", shortName=" + shortName + ", orderNo=" + orderNo + ", isvisible=" + isvisible + ", phone=" + phone
				+ ", phoneShort=" + phoneShort + ", mobileShortNo=" + mobileShortNo + "]";
	}

}
