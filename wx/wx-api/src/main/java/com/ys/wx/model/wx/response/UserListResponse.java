package com.ys.wx.model.wx.response;

import java.io.Serializable;
import java.util.List;

/**
 * Title : 部门成员列表
 * Description :
 * Author : Jerry xu    date : 2017/1/18
 * Update :             date :
 * Version : 1.0.0
 */
public class UserListResponse implements Serializable {

    public int                errcode; //返回码
    public String             errmsg; //对返回码的文本描述内容
    public List<UserlistBean> userlist;//成员列表

    public class UserlistBean {

        public String        userid;     //成员UserID。对应管理端的帐号
        public String        name;       //成员名称
        public List<Integer> department; //成员所属部门

    }
}
