package com.ys.wx.utils.pager;

/**
 * Title : 排序的enum
 * Description :
 * Author : Bruce.liu    date : 2015年1月24日
 * Update :              date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public enum ORDERBY {
    desc, asc;

    public ORDERBY reverse() {
        return (this == asc) ? desc : asc;
    }
}
