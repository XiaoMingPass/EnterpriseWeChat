package com.ys.wx.model.wx.response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Title : 获取部门列表
 * Description :
 * Author : Jerry xu    date : 2017/1/17
 * Update :             date :
 * Version : 1.0.0
 */
public class PartyListResponse implements Serializable {

    public int                   errcode;//返回码
    public String                errmsg;//对返回码的文本描述内容
    public ArrayList<Department> department;//部门列表数据。以部门的order字段从小到大排列

    public class Department implements Serializable {
        public int    id;//部门id
        public String name;//部门名称
        public int    parentid;//父亲部门id。根部门为1
        public int    order;//在父部门中的次序值。order值小的排序靠前。

        @Override
        public String toString() {
            return "Department [id=" + id + ", name=" + name + ", parentid=" + parentid + ", order=" + order + "]";
        }
    }

}
