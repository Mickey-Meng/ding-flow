package com.snow.common.utils;

/**
 * @ClassName UploadUtils
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/9 15:15
 * @Version 1.0
 */
public class UploadUtils {

    /**
     * 文件名称替换工具，将文件名称替换为随机名称
     * @param oldName
     * @return
     */
    public static String generateFileName(String oldName){
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        return IDUtils.generateUniqueId()+suffix;
    }
}
