package com.ys.wx.api.impl;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.api.IWxJobApi;
import com.ys.wx.service.wx.IEnterpriseWeChatService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Title : 对外提供产生CSV接口
 * Description :
 * Author : Jerry xu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 */

@Service
public class WxJobApiImpl implements IWxJobApi {

    private static final Logger logger = Logger.getLogger(WxJobApiImpl.class);

    @Resource
    IEnterpriseWeChatService enterpriseWeChatService;

    /**
     * 拉取数据 同步到微信
     */
    @Deprecated
    public ReturnVo<String> processDataToWeiXin() {
        ReturnVo<String> rvo = new ReturnVo<String>(0, "失败");
        try {
            rvo = enterpriseWeChatService.processDataToWeiXin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rvo;
    }

	@Override
	public ReturnVo<String> processLocalToWeiXin() {
		 ReturnVo<String> rvo = new ReturnVo<String>(0, "失败");
		 rvo = enterpriseWeChatService.processLocalToWeiXin();
		 return rvo;
	}

    
    
}
