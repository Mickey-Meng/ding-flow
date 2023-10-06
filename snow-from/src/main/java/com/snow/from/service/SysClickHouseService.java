package com.snow.from.service;

import org.springframework.stereotype.Service;

@Service
public interface SysClickHouseService {


    void insertTaskFrom(String taskId, String formData, String formField, String processDefinitionId);
}
