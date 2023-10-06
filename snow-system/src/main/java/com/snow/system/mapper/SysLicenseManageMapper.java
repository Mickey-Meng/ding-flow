package com.snow.system.mapper;

import com.snow.system.domain.ActDeModel;
import com.snow.system.domain.LicenseManage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 设计器modelMapper接口
 *
 * @author qimingjin
 * @date 2020-12-01
 */
@Component
public interface SysLicenseManageMapper
{


    LicenseManage getLicenseManage(@Param("license") String license, @Param("date") String date);
}
