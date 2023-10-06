package com.snow.from.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SysClickHouseMapper {


    void insertTaskFrom(@Param("taskId") String taskId, @Param("formData") String formData, @Param("formField") String formField,@Param("processDefinitionId")  String processDefinitionId);







}
