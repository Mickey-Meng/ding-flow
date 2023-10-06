package com.snow.from.service.impl;

import com.snow.common.annotation.DataSource;
import com.snow.common.enums.DataSourceType;
import com.snow.from.mapper.SysClickHouseMapper;
import com.snow.from.service.SysClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SysClickHouseServiceImpl implements SysClickHouseService {
    @Autowired
    SysClickHouseMapper sysClickHouseMapper;

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public void insertTaskFrom(String taskId, String formData, String formField, String processDefinitionId) {

        sysClickHouseMapper.insertTaskFrom(taskId, formData, formField, processDefinitionId);
    }
}
