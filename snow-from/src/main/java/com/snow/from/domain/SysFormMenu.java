package com.snow.from.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 菜单-单关联对象 sys_form_menu
 * 
 * @author Agee
 * @date 2022-05-18
 */
@Data
public class SysFormMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 菜单名称 */
    @Excel(name = "菜单名称")
    private String menuCode;

    /** 菜单名称 */
    @Excel(name = "菜单名称")
    private String formCode;



}
