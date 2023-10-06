package com.snow.system.service.impl;

import com.snow.system.domain.LicenseManage;
import com.snow.system.mapper.SysLicenseManageMapper;
import com.snow.system.service.SysLicenseManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName SysLicenseManageServiceImpl
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/22 10:37
 * @Version 1.0
 */
@Service
public class SysLicenseManageServiceImpl implements SysLicenseManageService
{
    @Autowired
    SysLicenseManageMapper sysLicenseManageMapper;
    @Override
    public LicenseManage getLicenseManage(String license, String date) {

        return sysLicenseManageMapper.getLicenseManage(license,date);
    }
}
