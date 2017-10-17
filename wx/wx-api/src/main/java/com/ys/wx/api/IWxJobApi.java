package com.ys.wx.api;

import com.mhome.tools.vo.ReturnVo;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/1/12
 * Update :             date :
 * Version : 1.0.0
 */
public interface IWxJobApi {


    /**
     * 拉取数据到微信
     * @Description:
     * @author wangzequan 2017 上午9:37:52
     */
    ReturnVo<String> processDataToWeiXin();
    
    ReturnVo<String> processLocalToWeiXin();
}
