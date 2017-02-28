package com.ys.wx.api.impl;

import com.ys.wx.api.IWxJobApi;
import com.ys.wx.service.wx.IEnterpriseWeChatService;
import com.ys.wx.vo.ReturnVo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Title : 对外提供产生CSV接口
 * Description :
 * Author : Jerry xu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
@Service
public class WxJobApiImpl implements IWxJobApi {

    private static final Logger logger = Logger.getLogger(WxJobApiImpl.class);

    @Resource
    private IEnterpriseWeChatService enterpriseWeChatService;

    /**
     * Ceaser wang
     * 拉取数据 同步到微信
     */
    public ReturnVo<String> processDataToWeiXin() {
        ReturnVo<String> rvo = new ReturnVo<String>(0, "失败");
        try {
            rvo = enterpriseWeChatService.processDataToWeiXin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvo;
    }

}
