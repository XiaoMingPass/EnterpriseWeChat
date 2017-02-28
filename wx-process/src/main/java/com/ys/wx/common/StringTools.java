package com.ys.wx.common;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: 字符串操作相关
 * Description:
 * Author: Maxwell wen
 * Since: 2016年4月5日 下午5:08:22
 * Version: 1.1.0
 * Copyright: (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class StringTools {

    /**
     * 将"1,2,3,4,5..."这种形式的字符串转成"'1','2','3','4'..."这种形式
     */
    public static String convertString(String strs) {
        if (StringUtils.isNotBlank(strs)) {
            String[] idStrs = strs.trim().split(",");
            if (idStrs.length > 0) {
                StringBuilder sbf = new StringBuilder("");
                for (String str : idStrs) {
                    if (StringUtils.isNotBlank(str)) {
                        sbf.append("'").append(str.trim()).append("'").append(",");
                    }
                }
                if (sbf.length() > 0) {
                    sbf = sbf.deleteCharAt(sbf.length() - 1);
                    return sbf.toString();
                }
            }
        }
        return "";
    }

    /**
     * 将"1,2,3,4,5..."这种形式的字符串转成"1,2,3,4..."这种形式
     */
    public static String convertStringTwo(String strs) {
        if (StringUtils.isNotBlank(strs)) {
            String[] idStrs = strs.trim().split(",");
            if (idStrs.length > 0) {
                StringBuilder sbf = new StringBuilder("");
                for (String str : idStrs) {
                    if (StringUtils.isNotBlank(str)) {
                        sbf.append(str.trim()).append(",");
                    }
                }
                if (sbf.length() > 0) {
                    sbf = sbf.deleteCharAt(sbf.length() - 1);
                    return sbf.toString();
                }
            }
        }
        return "";
    }

    /**
     * 将"1,2,3,4,5..."这种形式的字符串转成List<String> 集合
     */
    public static List<String> convertStringToList(String strs) {
        if (StringUtils.isNotBlank(strs)) {
            String[] idStrs = strs.trim().split(",");
            if (idStrs.length > 0) {
                List<String> strsList = new ArrayList<String>();
                for (String str : idStrs) {
                    if (StringUtils.isNotBlank(str)) {
                        strsList.add(str.trim());
                    }
                }
                if (strsList.size() > 0) {
                    return strsList;
                }
            }
        }
        return null;
    }

    /**
     * 将"1,2,3,4,5..."这种形式的字符串转成List<Integer> 集合
     */
    public static List<Integer> convertStringToIntegerList(String strs) {
        if (StringUtils.isNotBlank(strs)) {
            String[] idStrs = strs.trim().split(",");
            if (idStrs.length > 0) {
                List<Integer> strsList = new ArrayList<Integer>();
                for (String str : idStrs) {
                    if (StringUtils.isNotBlank(str)) {
                        strsList.add(Integer.parseInt(str.trim()));
                    }
                }
                if (strsList.size() > 0) {
                    return strsList;
                }
            }
        }
        return null;
    }

    /**
     * 将list 转换为 '1','2','3','4','5','6'
     */
    public static String convertListToString(List<String> strList) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(strList)) {
            for (int i = 0; i < strList.size(); i++) {
                if (i == 0) {
                    sb.append("'").append(strList.get(i)).append("'");
                } else {
                    sb.append(",").append("'").append(strList.get(i)).append("'");
                }
            }
        }
        return sb.toString();
    }

}
