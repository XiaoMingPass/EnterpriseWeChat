package com.ys.wx.service.wx;

import com.mhome.tools.vo.ReturnVo;

/**
 * 企业微信号业务逻辑
 *
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2017年2月17日 上午10:01:15
 * @Version:1.1.0
 * @Copyright:Copyright (c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */
public interface IEnterpriseWeChatService {

    /**
     * 同步数据到微信
     */
	@Deprecated
    ReturnVo<String> processDataToWeiXin();

    @Deprecated
    void processChangeEnvironment();

    /**
     * 删除所有组织数据
     */
    void deleteTestEnvironmentOrganizationData(final String token , final int excludeId);

    
    /**
     * 删除排除指定组织外的所有组织数据
     */
    void recursivelyDelete(String token, int id, final int excludeId);

    /**
     * 基于消息改造版本的定时任务
     * 抽取当天本地数据库为同步的数据，同步到微信企业号上去
     * @return
     */
	ReturnVo<String> processLocalToWeiXin();
}
