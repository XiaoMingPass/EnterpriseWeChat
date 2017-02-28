package com.ys.wx.api;

import com.ys.wx.vo.ReturnVo;

/**
 * Title : 微信对外接口
 * Description :
 * Author : Jerry xu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public interface IWxJobApi {

    ReturnVo<String> processDataToWeiXin();
}
