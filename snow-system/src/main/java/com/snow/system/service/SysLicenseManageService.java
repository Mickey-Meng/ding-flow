package com.snow.system.service;

import com.snow.system.domain.LicenseManage;

import java.util.Date;

public interface SysLicenseManageService {


  LicenseManage getLicenseManage(String license, String date);
}
