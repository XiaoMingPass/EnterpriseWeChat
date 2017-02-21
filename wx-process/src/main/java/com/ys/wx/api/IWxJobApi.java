package com.ys.wx.api;

import com.ys.wx.vo.ReturnVo;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 * @Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface IWxJobApi {


    /**
     * 拉取数据到微信
     * @Description:
     * @author wangzequan 2017 上午9:37:52
     */
    public ReturnVo<String> processDataToWeiXin();
}
