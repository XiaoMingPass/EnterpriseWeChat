package com.ys.wx.model.wx.response;

import java.io.Serializable;

/**
 * Title : 同步部门和人员返回Response
 * Description :
 * Author : Jerry xu    date : 2017/1/9
 * Update :             date :
 * Version : 1.0.0
 */
public class PartyAndUserResponse implements Serializable {
    public int    errcode;//返回码
    public String errmsg;//对返回码的文本描述内容
    public String jobid;//异步任务id，最大长度为64字节
}
