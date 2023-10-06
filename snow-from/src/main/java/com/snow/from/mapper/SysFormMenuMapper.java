package com.snow.from.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snow.from.domain.SysFormMenu;

/**
 * 菜单-单关联Mapper接口
 * 
 * @author Agee
 * @date 2022-05-18
 */
public interface SysFormMenuMapper extends BaseMapper<SysFormMenu>
{
    /**
     * 查询菜单-单关联
     * 
     * @param id 菜单-单关联ID
     * @return 菜单-单关联
     */
    public SysFormMenu selectSysFormMenuById(Long id);

    /**
     * 查询菜单-单关联列表
     * 
     * @param sysFormMenu 菜单-单关联
     * @return 菜单-单关联集合
     */
    public List<SysFormMenu> selectSysFormMenuList(SysFormMenu sysFormMenu);

    /**
     * 新增菜单-单关联
     * 
     * @param sysFormMenu 菜单-单关联
     * @return 结果
     */
    public int insertSysFormMenu(SysFormMenu sysFormMenu);

    /**
     * 修改菜单-单关联
     * 
     * @param sysFormMenu 菜单-单关联
     * @return 结果
     */
    public int updateSysFormMenu(SysFormMenu sysFormMenu);

    /**
     * 删除菜单-单关联
     * 
     * @param id 菜单-单关联ID
     * @return 结果
     */
    public int deleteSysFormMenuById(Long id);

    /**
     * 批量删除菜单-单关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysFormMenuByIds(String[] ids);
}
