package com.ys.wx.common;

import java.util.Calendar;

/**
 * Title: 文件路径类
 * Description:
 * Author: Maxwell wen
 * Since: 2016年2月22日 上午9:32:26
 * Version: 1.1.0
 * Copyright: (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public class ComFilePart {

    /**
     * 取得年月日
     *
     * @return module+年/月/日/
     */
    private static String getFilePath() {
        StringBuilder path = new StringBuilder("/prdct/imgs/");
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int date = now.get(Calendar.DATE);
        path.append(year);
        path.append("/");
        path.append(month);
        path.append("/");
        path.append(date);
        path.append("/");
        return path.toString();
    }

    public static void main(String[] args) {
        System.err.println(getFilePath());
    }
}
