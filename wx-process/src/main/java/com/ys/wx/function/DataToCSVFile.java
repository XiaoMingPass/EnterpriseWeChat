package com.ys.wx.function;


import com.ys.wx.common.SpringContextHolder;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.utils.ReadProperty;
import com.ys.wx.utils.StringUtils;
import com.ys.wx.utils.ToCSVUtils;
import com.ys.wx.vo.ReturnVo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Title : 生成CSV报表
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
@Service
public class DataToCSVFile {

    private static final Logger       logger       = Logger.getLogger(DataToCSVFile.class);
    private final static ReadProperty readProperty = SpringContextHolder.getBean("readProperty");
    @Resource
    private IWxServiceClient wxServiceClient;
    @Resource
    private IDeptService     deptService;
    @Resource
    private IPersonService   personService;

    public DataToCSVFile() {
    }

    /**
     * 生成CSV报表
     */
    public void DataToCSV() {
        try {
            //清除前置文件
            preCleanFiles();
            //获取部门数据
            ReturnVo<List<DepartmentForImport>> dVo = deptService.getEHRDeptData();

            //生成部门表
            File deptCSVFile = DeptDataToCSV(dVo);
            logger.info(">>>>>>>>>>>>>>《开始》上传party报表>>>>>>>>>>>>\n");
            wxServiceClient.uploadFile(deptCSVFile);
            logger.info(">>>>>>>>>>>>>>《完成》上传party报表>>>>>>>>>>>>\n");
            if (WxServiceClient.isIsSuccessParty()) {//部门表同步成功
                //获取成员数据
                ReturnVo<List<PersonalForImport>> pVo = personService.getEHRPersonalData();
                //生成用户表
                File personCSVFile = PersonDataToCSV(pVo);
                logger.info(">>>>>>>>>>>>>>《开始》上传user报表>>>>>>>>>>>>\n");
                wxServiceClient.uploadFile(personCSVFile);
                logger.info(">>>>>>>>>>>>>>《完成》上传user报表>>>>>>>>>>>>\n");
            } else {
                logger.info(">>>>>>>>>>>>>>部门表同步失败，中断用户表数据同步>>>>>>>>>>>>\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成部门CSV报表
     */
    public File DeptDataToCSV(ReturnVo<List<DepartmentForImport>> dVo) {
        logger.info(">>>>>>>>>>>>>>【开始】组装party报表>>>>>>>>>>>>");
        List<Map<String, String>> exportData = new ArrayList<Map<String, String>>();
        List<DepartmentForImport> depts = dVo.getData();
        for (DepartmentForImport dept : depts) {
            if (null != dept) {
                Map<String, String> row = new LinkedHashMap<String, String>();
                row.put("1", (StringUtils.replaceBlank(dept.getDept_name()).equals("")
                        || StringUtils.replaceBlank(dept.getDept_name()) == null) ? "" : StringUtils.replaceBlank(dept.getDept_name()));

                row.put("2", (StringUtils.replaceBlank(dept.getDept_id()).equals("")
                        || StringUtils.replaceBlank(dept.getDept_id()) == null) ? "" : StringUtils.replaceBlank(dept.getDept_id()));

                row.put("3", (StringUtils.replaceBlank(dept.getDept_fid()).equals("")
                        || StringUtils.replaceBlank(dept.getDept_fid()) == null) ? "" : StringUtils.replaceBlank(dept.getDept_fid()));

                row.put("4", (StringUtils.replaceBlank(dept.getDept_order()).equals("")
                        || StringUtils.replaceBlank(dept.getDept_order()) == null) ? "" : StringUtils.replaceBlank(dept.getDept_order()));

                exportData.add(row);
            }
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("1", "部门名称");
        map.put("2", "部门ID");
        map.put("3", "父部门ID");
        map.put("4", "排序");

        //存储文件名称
        String fileName = "party";
        //获取到存储文件路径
        String path = storageFilePath();

        File file = ToCSVUtils.createCSVFile(exportData, map, path, fileName);//生成CSV文件
        logger.info(">>>>>>>>>>>>>>【完成】组装party报表>>>>>>>>>>>>");
        return file;
    }


    /**
     * 生成用户CSV报表
     */
    public File PersonDataToCSV(ReturnVo<List<PersonalForImport>> pVo) {
        logger.info(">>>>>>>>>>>>>>【开始】组装user报表>>>>>>>>>>>>");
        File file = null;
        try {
            List<Map<String, String>> exportData = new ArrayList<Map<String, String>>();
            List<PersonalForImport> personals = pVo.getData();
            System.out.println("生成人员信息报表前数据中条数:\n" + pVo.getData().size());
            for (PersonalForImport person : personals) {
                if (null != person) {
                    Map<String, String> row = new LinkedHashMap<String, String>();
                    row.put("1", (StringUtils.replaceBlank(person.getPsn_name()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_name()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_name()));
                    row.put("2", (StringUtils.replaceBlank(person.getPsn_id()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_id()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_id()));
                    row.put("3", "");
                    row.put("4", CommUtils.defaultIfEmpty(person.getWeixinid(), ""));//weixinno
                    row.put("5", (StringUtils.replaceBlank(person.getPsn_mobile()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_mobile()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_mobile()));
                    row.put("6", (StringUtils.replaceBlank(person.getPsn_email()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_email()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_email()));
                    row.put("7", (StringUtils.replaceBlank(person.getPsn_deptcode()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_deptcode()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_deptcode()));
                    row.put("8", (StringUtils.replaceBlank(person.getPsn_postname()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_postname()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_postname()));

                    row.put("9", CommUtils.defaultIfEmpty(person.getLocaloffice(), ""));//所在办公地
                    row.put("10", CommUtils.defaultIfEmpty(person.getFullName(), "")); //全称
                    row.put("11", CommUtils.defaultIfEmpty(person.getShortName(), "")); //简称
                    row.put("12", person.getOrderNo() + ""); //排序号
                    row.put("13", CommUtils.defaultIfEmpty(person.getIsvisible(), "")); //是否可见
                    row.put("14", (StringUtils.replaceBlank(person.getPsn_officephone()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_officephone()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_officephone()));
                    row.put("15", CommUtils.defaultIfEmpty(person.getPhoneShort(), "")); // 座机短号
                    row.put("16", (StringUtils.replaceBlank(person.getPsn_code()).equals("")
                            || StringUtils.replaceBlank(person.getPsn_code()) == null) ? "" : StringUtils.replaceBlank(person.getPsn_code()));
                    row.put("17", CommUtils.defaultIfEmpty(person.getMobileShortNo(), "")); //手机短号
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
            String path = storageFilePath();
            //存储文件名称
            String fileName = "user";
            file = ToCSVUtils.createCSVFile(exportData, map, path, fileName);//生成CSV文件
            logger.info(">>>>>>>>>>>>>>【完成】组装user报表>>>>>>>>>>>>");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 清除前置文件
     * 将系统中的临时文件清除
     */
    private void preCleanFiles() {
        String path = storageFilePath();
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File[] files = filePath.listFiles();
        assert files != null;
        for (File file : files) {
            boolean isDelete = file.delete();
            if (!isDelete) {
                logger.info("DataToCSVFile-DataToCSV 前置文件 删除操作失败, 文件：" + file.getName());
            }
        }
    }

    /**
     * 存储文件路径
     */
    private String storageFilePath() {
        String linuxPath = readProperty.getValue("linux_cvs_file_path");
        String winPath = readProperty.getValue("win_cvs_file_path");
        String path;
        if (CommUtils.isLinuxOs()) {
            path = linuxPath;
        } else {
            path = winPath;
        }
        return path;
    }
}
