package com.ys.wx.utils;

import java.util.regex.Pattern;

/**
 * Title : 字符串处理
 * Description :
 * Author : Jerry xu    date : 2017/1/13
 * Update :             date :
 * Version : 1.0.0
 */
public class StringUtils {

    /**
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String convertString = "";
        if (str != null) {
            // 正则匹配去换行等
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            // 去除字符串的引号
            String regexp = "\"";
            convertString = p.matcher(str).replaceAll("").replaceAll(regexp, "");
        }
        return convertString.replaceAll("[,\\[\\]?'‘’`·，。\\\\/;；：•\\+()\\-\\*\"]", "");
    }

}
