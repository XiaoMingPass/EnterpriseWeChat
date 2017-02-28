package com.ys.wx.model.wx.response;

import java.io.Serializable;
import java.util.List;

/**
 * Title : 获取人员信息详情Response
 * Description :
 * Author : Jerry xu    date : 2017/1/23
 * Update :             date :
 * Version : 1.0.0
 */
public class UserDetailsResponse implements Serializable {

    public int                errcode;  //返回码
    public String             errmsg;   //对返回码的文本描述内容
    public List<UserlistBean> userlist; //成员列表

    public class UserlistBean implements Serializable {
        public String        avatar;    //头像url。注：如果要获取小图将url最后的
        public ExtattrBean   extattr;   //扩展属性
        public String        gender;    //性别。0表示未定义，1表示男性，2表示女性
        public String        name;      //成员名称
        public String        position;  //职位信息
        public int           status;    //关注状态: 1=已关注，2=已冻结，4=未关注
        public String        userid;    //成员UserID。对应管理端的帐号
        public String        weixinid;  //微信号
        public List<Integer> department;//成员所属部门id列表
        public String        mobile;
        public String        email;

        @Override
        public String toString() {
            return "UserlistBean [avatar=" + avatar + ", extattr=" + extattr + ", gender=" + gender + ", name=" + name
                    + ", position=" + position + ", status=" + status + ", userid=" + userid + ", weixinid=" + weixinid
                    + ", department=" + department + ", mobile=" + mobile + ", email=" + email + "]";
        }

        public class ExtattrBean implements Serializable {
            public List<AttrsBean> attrs;

            public class AttrsBean implements Serializable {
                public String name; //扩展字段对应name
                public String value;//扩展字段对应value
            }
        }
    }

}
