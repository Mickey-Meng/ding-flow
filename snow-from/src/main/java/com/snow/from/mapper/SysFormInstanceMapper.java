package com.snow.from.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.service.impl.InformationSchema;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 单实例Mapper接口
 *
 * @author 没用的阿吉
 * @date 2021-03-21
 */
public interface SysFormInstanceMapper extends BaseMapper<SysFormInstance> {
    /**
     * 根据菜单ID查询信息
     *
     * @param menuCode 菜单Code
     * @return 菜单信息
     */
    public SysFormInstance selectByMenuCode(String menuCode);

    List<SysFormDataRecord> ListSysFormInstanceByFormCode(@Param("formCode") String formCode);

    List<InformationSchema> selectCreateInfo(@Param("table_schema")String table_schema, @Param("table_name") String table_name);

}
