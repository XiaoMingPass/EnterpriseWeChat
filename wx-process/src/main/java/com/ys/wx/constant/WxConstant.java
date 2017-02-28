package com.ys.wx.constant;

/**
 * Title: 常量
 * Description:
 * Author: Maxwell wen
 * Since: 2016年1月13日 下午5:32:24
 * Version:1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public class WxConstant {
    /**
     * EHR oracle数据库地址
     * 10.10.20.1 测试环境
     * 10.10.12.87 正式环境
     */
    public static final String EHR_DB_SERVICE_IP = "ehr_db_service_ip";
    // props.getProperty("ehr_db_service_ip"); //"10.10.20.1";

    //微信企业Id
    public static String CORP_ID = "wx_corp_id";
    //微信管理组的凭证密钥
    public static String SECRET  = "wx_secret";

}
