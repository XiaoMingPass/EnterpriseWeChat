package com.ys.wx.constant;

/**
 * 常量
 *
 * @Title:
 * @Description:
 * @Author:wentaoxiang
 * @Since:2016年1月13日 下午5:32:24
 * @Version:1.1.0
 * @Copyright:Copyright (c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */
public class WxConstant {
    /**
     * EHR oracle数据库地址
     * 10.10.20.1 测试环境
     * 10.10.12.87 正式环境
     */
    public static final String EHR_DB_SERVICE_IP = "ehr_db_service_ip";// props.getProperty("ehr_db_service_ip"); //"10.10.20.1";

    //微信企业Id
    public static String CORP_ID = "wx_corp_id";
    //微信管理组的凭证密钥
    public static String SECRET = "wx_secret";
    
    public static int SUCCESS=1;
    public static int ERROR=0;
    
    
    public static int MODYFIED = 1;
    public static int NOMODYFIED = 0;
    
    public static int NODEL = 1;
    public static int DEL = 0;
    
    public static String GLOBAL = "GLOBAL";

}
