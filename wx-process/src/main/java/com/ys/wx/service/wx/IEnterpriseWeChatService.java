package com.ys.wx.service.wx;

import com.ys.wx.vo.ReturnVo;

/**
 * Title : 微信数据切换准备接口
 * Description :
 * Author : Ceaser wang Jerry xu    date : 2017/1/12
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public interface IEnterpriseWeChatService {

    /**
     * 同步数据到微信
     */
    ReturnVo<String> processDataToWeiXin();

    /**
     * 第一次环境切换时使用,新旧数据对比分了三种情况，具体请参考实现方法
     * //case1:匹配到的数据以微信服务端(老数据)的数据为准，但是部门除外,部门以外部流入的数据为准
     * copyOldPersonToNewPersonExcludeDeptId(matchedFW, personalForImport, userlistbean);
     * //case2:数据内容以新数据为准，部门以老数据的组织为准
     * //personalForImport.setPsn_deptcode(null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"");
     * //case3:所有数据一律以新数据为准，这种情况不需要做任何处理。本次循环逻辑可以直接去掉
     */
    void processChangeEnvironment();

}
