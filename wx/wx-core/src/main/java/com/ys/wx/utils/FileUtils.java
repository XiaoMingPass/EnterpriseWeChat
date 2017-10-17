package com.ys.wx.utils;

import com.mhome.tools.common.ReadProperty;
import com.mhome.tools.common.SpringContextHolder;

import java.io.File;

/**
 * Title :
 * Description :
 * Author : Jerry xu    date : 2017/4/10
 * Update :             date :
 * Version : 1.0.0
 */
public class FileUtils {

    private final static ReadProperty readProperty = SpringContextHolder.getBean("readProperty");

    /**
     * 获取文件存储路径
     *
     * @return
     */
    public static String storageFilePath() {
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

    /**
     * 获取指定路径符合格式的CSV文件
     *
     * @param path       文件路径
     * @param filePrefix 文件前缀
     * @return
     */
    public static File getFiles(String path, String filePrefix) {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().matches(filePrefix + "\\w+.csv")) {
                    return file;
                }
            }
        }
        return null;
    }

    /**
     * 清除前置文件
     * 将系统中的临时文件清除
     */
    public static void preCleanFiles() {
        String path = FileUtils.storageFilePath();
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        File[] files = filePath.listFiles();
        assert files != null;
        for (File file : files) {
            boolean isDelete = file.delete();
            if (!isDelete) {
                System.out.println("DataToCSVFile-DataToCSV 前置文件 删除操作失败, 文件：" + file.getName());
            }
        }
    }

}
