package com.snow.framework.shiro.service;

import com.snow.common.config.CacheMap;
import com.snow.system.domain.LicenseManage;
import com.snow.system.service.SysLicenseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SysLicense
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/22 10:35
 * @Version 1.0
 */
@Component
public class SysLicenseManage {

    private static CacheMap<String, Map<String,String>> cache = new CacheMap<String, Map<String,String>>(TimeUnit.DAYS,10);

    public static CacheMap<String, Map<String, String>> getCache() {
        return cache;
    }


    @Autowired
    SysLicenseManageService sysLicenseManageService;


    public LicenseManage getLicenseManage(String license, String date) {

        return  sysLicenseManageService.getLicenseManage(license,date);
    }
}
