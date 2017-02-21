package com.ys.wx.service.wx;

import com.ys.wx.vo.ReturnVo;

/**
 * 企业微信号业务逻辑
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2017年2月17日 上午10:01:15
 * @Version:1.1.0
 * @Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface IEnterpriseWeChatService {

	/**
	 * 同步数据到微信
	 * @Description:
	 * @author wangzequan 2017 上午10:01:35
	 */
	
	public ReturnVo<String> processDataToWeiXin();
	
	/**
	 * 第一次环境切换时使用,新旧数据对比分了三种情况，具体请参考实现方法
    //case1:匹配到的数据以微信服务端(老数据)的数据为准，但是部门除外,部门以外部流入的数据为准
	copyOldPersonToNewPersonExcludeDeptId(matchedfw, personalForImport, userlistbean);
	//case2:数据内容以新数据为准，部门以老数据的组织为准
	//personalForImport.setPsn_deptcode(null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"");
	//case3:所有数据一律以新数据为准，这种情况不需要做任何处理。本次循环逻辑可以直接去掉
	 */
	public void processChangeEnvironment();
	
}
