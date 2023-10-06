package com.snow.from.service.impl;

import java.util.List;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snow.from.domain.SysFormMenu;
import com.snow.from.mapper.SysFormMenuMapper;
import com.snow.from.service.ISysFormMenuService;
import org.springframework.stereotype.Service;
import com.snow.common.core.text.Convert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import javax.annotation.Resource;

/**
 * 菜单-单关联Service业务层处理
 *
 * @author Agee
 * @date 2022-05-18
 */
@Service
public class SysFormMenuServiceImpl extends ServiceImpl<SysFormMenuMapper, SysFormMenu> implements ISysFormMenuService {
    @Resource
    private SysFormMenuMapper sysFormMenuMapper;

    /**
     * 查询菜单-单关联
     *
     * @param id 菜单-单关联ID
     * @return 菜单-单关联
     */
    @Override
    public SysFormMenu selectSysFormMenuById(Long id) {
        return sysFormMenuMapper.selectById(id);
    }

    /**
     * 查询菜单-单关联列表
     *
     * @param sysFormMenu 菜单-单关联
     * @return 菜单-单关联
     */
    @Override
    public List<SysFormMenu> selectSysFormMenuList(SysFormMenu sysFormMenu) {
        LambdaQueryWrapper<SysFormMenu> lambda = new QueryWrapper<SysFormMenu>().lambda();
        lambda.eq(ObjectUtil.isNotEmpty(sysFormMenu.getMenuCode()),SysFormMenu::getMenuCode,sysFormMenu.getMenuCode());
        lambda.eq(ObjectUtil.isNotEmpty(sysFormMenu.getFormCode()),SysFormMenu::getFormCode,sysFormMenu.getFormCode());
        return sysFormMenuMapper.selectList(lambda);
    }

    /**
     * 新增菜单-单关联
     *
     * @param sysFormMenu 菜单-单关联
     * @return 结果
     */
    @Override
    public int insertSysFormMenu(SysFormMenu sysFormMenu) {
        return sysFormMenuMapper.insert(sysFormMenu);
    }

    /**
     * 修改菜单-单关联
     *
     * @param sysFormMenu 菜单-单关联
     * @return 结果
     */
    @Override
    public int updateSysFormMenu(SysFormMenu sysFormMenu) {
        return sysFormMenuMapper.updateById(sysFormMenu);
    }

    /**
     * 删除菜单-单关联对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
     @Override
     public int deleteSysFormMenuByIds(String ids) {
        return sysFormMenuMapper.deleteBatchIds(Convert.toStrList(ids));
     }

    /**
     * 删除菜单-单关联信息
     *
     * @param id 菜单-单关联ID
     * @return 结果
     */
    @Override
    public int deleteSysFormMenuById(Long id) {
        return sysFormMenuMapper.deleteById(id);
    }
}
