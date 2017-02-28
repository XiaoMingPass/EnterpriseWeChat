package com.ys.wx.utils;

import com.ys.wx.common.DecryptPropertyPlaceholderConfigurer;

/**
 * Title : 读取配置文件application.property
 * Description :
 * Author : Bruce.liu    date : 2015年1月24日
 * Update :              date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class ReadProperty {

    public DecryptPropertyPlaceholderConfigurer dppc;

    /**
     * 读取配置文件的内容
     *
     * @param key
     * @return value
     */
    public String getValue(String key) {
        return dppc.getValue(key);
    }

    public void setDppc(DecryptPropertyPlaceholderConfigurer dppc) {
        this.dppc = dppc;
    }


}
