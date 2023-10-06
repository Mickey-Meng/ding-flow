package com.snow.framework.shiro.service;

import com.snow.common.config.CacheMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CacheManager
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/22 11:05
 * @Version 1.0
 */
public class CacheManager {




    private static CacheMap<String, Map<String,String>> cache = new CacheMap<String, Map<String,String>>(TimeUnit.DAYS,10);




    public static CacheMap<String,Map<String,String>> getCache(){
        return cache;
    }


    static {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                cache.removeALLExpired();
                System.out.println("缓存清理----");
            }
        }, 10, 10, TimeUnit.SECONDS);

    }
}
