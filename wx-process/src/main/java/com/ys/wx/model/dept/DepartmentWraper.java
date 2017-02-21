package com.ys.wx.model.dept;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("arrResult")
public class DepartmentWraper implements Serializable {
    @XStreamImplicit(itemFieldName = "deptWXVO")
    private List<DepartmentForImport> depts;
    @XStreamAlias("count")
    private String count;

    public List<DepartmentForImport> getDepts() {
        return depts;
    }

    public void setDepts(List<DepartmentForImport> depts) {
        this.depts = depts;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DepartmentWraper [depts=" + depts + ", count=" + count + "]";
    }
}
