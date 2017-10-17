package com.ys.wx.model.wx.response;

import java.io.Serializable;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/1/18
 * Update :             date :
 * Version : 1.0.0
 */
public class DeletePartyResponse implements Serializable {

    public String errcode;//调用接口凭证
    public String errmsg;//部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）
}
