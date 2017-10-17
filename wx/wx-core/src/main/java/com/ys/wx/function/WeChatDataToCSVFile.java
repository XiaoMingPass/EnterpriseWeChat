package com.ys.wx.function;

import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.utils.FileUtils;
import com.ys.wx.utils.StringUtils;
import com.ys.wx.utils.ToCSVUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Title : 微信数据生成CSV报表
 * Description :
 * Author : Jerry xu    date : 2017/4/11
 * Update :             date :
 * Version : 1.0.0
 */
@Service
public class WeChatDataToCSVFile {

    private static final Logger logger = Logger.getLogger(WeChatDataToCSVFile.class);

    public WeChatDataToCSVFile() {

    }

    /**
     * weChat数据生成部门CSV报表
     */
    public File WeChatDeptDataToCSV(ArrayList<PartyListResponse.Department> dVo) {
        logger.info(">>>>>>>>>>>>>>【开始】组装party报表>>>>>>>>>>>>");
        List<Map<String, String>> exportData = new ArrayList<Map<String, String>>();
        for (PartyListResponse.Department dept : dVo) {
            if (null != dept) {
                Map<String, String> row = new LinkedHashMap<String, String>();
                row.put("1", (StringUtils.replaceBlank(dept.name).equals("")
                        || StringUtils.replaceBlank(dept.name) == null) ? "" : StringUtils.replaceBlank(dept.name));
                row.put("2", (StringUtils.replaceBlank(dept.id + "").equals("")
                        || StringUtils.replaceBlank(dept.id + "") == null) ? "" : StringUtils.replaceBlank(dept.id + ""));
                row.put("3", (StringUtils.replaceBlank(dept.parentid + "").equals("")
                        || StringUtils.replaceBlank(dept.parentid + "") == null) ? "" : StringUtils.replaceBlank(dept.parentid + ""));
                row.put("4", (StringUtils.replaceBlank(dept.order + "").equals("")
                        || StringUtils.replaceBlank(dept.order + "") == null) ? "" : StringUtils.replaceBlank(dept.order + ""));
                exportData.add(row);
            }
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("1", "部门名称");
        map.put("2", "部门ID");
        map.put("3", "父部门ID");
        map.put("4", "排序");
        //存储文件名称
        String fileName = "party-wechat";
        //获取到存储文件路径
        String path = FileUtils.storageFilePath();

        File file = ToCSVUtils.createCSVFile(exportData, map, path, fileName);//生成CSV文件
        logger.info(">>>>>>>>>>>>>>【完成】组装party报表>>>>>>>>>>>>");
        return file;
    }


    /**
     * weChat数据生成人员CSV报表
     */
/*    public File WeChatPersonDataToCSV(List<UserDetailsResponse.UserlistBean> pVo) {
        logger.info(">>>>>>>>>>>>>>【开始】组装user报表>>>>>>>>>>>>");
        File file = null;
        try {
            List<Map<String, String>> exportData = new ArrayList<Map<String, String>>();
//            List<UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean> attrsBeans = new LinkedList<UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean>();
            for (UserDetailsResponse.UserlistBean person : pVo) {
                if (null != person) {
                    Map<String, String> row = new LinkedHashMap<String, String>();
                    row.put("1", (StringUtils.replaceBlank(person.name).equals("")
                            || StringUtils.replaceBlank(person.name) == null) ? "" : StringUtils.replaceBlank(person.name));
                    row.put("2", (StringUtils.replaceBlank(person.userid).equals("")
                            || StringUtils.replaceBlank(person.userid) == null) ? "" : StringUtils.replaceBlank(person.userid));
                    row.put("3", (StringUtils.replaceBlank(person.gender).equals("")
                            || StringUtils.replaceBlank(person.gender) == null) ? "" : StringUtils.replaceBlank(person.gender));
                    row.put("4", (StringUtils.replaceBlank(person.weixinid).equals("")
                            || StringUtils.replaceBlank(person.weixinid) == null) ? "" : StringUtils.replaceBlank(person.weixinid));
                    row.put("5", (StringUtils.replaceBlank(person.mobile).equals("")
                            || StringUtils.replaceBlank(person.mobile) == null) ? "" : StringUtils.replaceBlank(person.mobile));
                    row.put("6", (StringUtils.replaceBlank(person.email).equals("")
                            || StringUtils.replaceBlank(person.email) == null) ? "" : StringUtils.replaceBlank(person.email));
//                    if (person.extattr.attrs != null && person.extattr.attrs.size() > 0) {
//                        for (UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean extattr : attrsBeans) {
//
//                        }
//                    }
//
//                    row.put("7", (StringUtils.replaceBlank(person.name).equals("")
//                            || StringUtils.replaceBlank(person.name) == null) ? "" : StringUtils.replaceBlank(person.name));
//                    row.put("8", (StringUtils.replaceBlank(person.position).equals("")
//                            || StringUtils.replaceBlank(person.position) == null) ? "" : StringUtils.replaceBlank(person.position));
//                    row.put("9", (StringUtils.replaceBlank(person.name).equals("")
//                            || StringUtils.replaceBlank(person.name) == null) ? "" : StringUtils.replaceBlank(person.name));
//                    row.put("7", (StringUtils.replaceBlank(person.name).equals("")
//                            || StringUtils.replaceBlank(person.name) == null) ? "" : StringUtils.replaceBlank(person.name));
                    exportData.add(row);
                }
            }

            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("1", "姓名");
            map.put("2", "账号");
            map.put("3", "性别");
            map.put("4", "微信号");
            map.put("5", "手机号");
            map.put("6", "邮箱");
            map.put("7", "所在部门");
            map.put("8", "职位");
            map.put("9", "所在办公地");
            map.put("10", "全称");
            map.put("11", "简称");
            map.put("12", "排序号");
            map.put("13", "是否可见");
            map.put("14", "固定电话");
            map.put("15", "座机短号");
            map.put("16", "工号");
            map.put("17", "手机短号");
            //获取到存储文件路径
            String path = FileUtils.storageFilePath();
            //存储文件名称
            String fileName = "user-wechat";
            file = ToCSVUtils.createCSVFile(exportData, map, path, fileName);//生成CSV文件
            logger.info(">>>>>>>>>>>>>>【完成】组装user报表>>>>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }*/
}
