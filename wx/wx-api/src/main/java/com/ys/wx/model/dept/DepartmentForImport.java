package com.ys.wx.model.dept;

import com.mhome.tools.common.BaseModel;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 企业微信通讯录部门结构信息Bean
 *
 * @author Jerry xu
 * @date 2017-01-11 16:39:16
 */
@XStreamAlias("deptWXVO")
public class DepartmentForImport extends BaseModel implements Serializable {

    @XStreamAlias("dirty")
    private String dirty;

    @XStreamAlias("status")
    private String status;

    //部门名称
    @XStreamAlias("dept_name")
    private String dept_name;

    //部门ID
    @XStreamAlias("dept_id")
    private String dept_id;

    //父部门ID
    @XStreamAlias("dept_fid")
    private String dept_fid;

    //部门排序号
    @XStreamAlias("dept_order")
    private String dept_order;

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

    public String getDept_fid() {
        return dept_fid;
    }

    public void setDept_fid(String dept_fid) {
        this.dept_fid = dept_fid;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getDept_order() {
        return dept_order;
    }

    public void setDept_order(String dept_order) {
        this.dept_order = dept_order;
    }


    @Override
    public String toString() {
        return "DepartmentForImport [dirty=" + dirty + ", status=" + status + ", dept_fid=" + dept_fid + ", dept_id="
                + dept_id + ", dept_name=" + dept_name + ", dept_order=" + dept_order + "]";
    }

}
