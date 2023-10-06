package com.snow.common.utils;

import com.snow.common.utils.uuid.UUID;

/**
 * @ClassName IDUtils
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/9 15:14
 * @Version 1.0
 */
public class IDUtils {

    /**
     * 唯一ID生成器，可以生成唯一ID
     * @return
     */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString()+System.currentTimeMillis();
    }
}
