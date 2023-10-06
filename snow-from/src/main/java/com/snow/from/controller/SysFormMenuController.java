package com.snow.from.controller;

import java.util.List;

import com.snow.from.domain.SysFormMenu;
import com.snow.from.service.ISysFormMenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.snow.common.annotation.Log;
import com.snow.common.enums.BusinessType;
import org.springframework.stereotype.Controller;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 菜单-单关联Controller
 * 
 * @author Agee
 * @date 2022-05-18
 */
@Controller
@RequestMapping("/form/formMenu")
public class SysFormMenuController extends BaseController
{
    private String prefix = "form/formMenu";

    @Autowired
    private ISysFormMenuService sysFormMenuService;

    @RequiresPermissions("form:formMenu:view")
    @GetMapping()
    public String menu()
    {
        return prefix + "/menu";
    }

    /**
     * 查询菜单-单关联列表
     */
    @RequiresPermissions("form:formMenu:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFormMenu sysFormMenu)
    {
        startPage();
        List<SysFormMenu> list = sysFormMenuService.selectSysFormMenuList(sysFormMenu);
        return getDataTable(list);
    }

    /**
     * 导出菜单-单关联列表
     */
    @RequiresPermissions("form:formMenu:export")
    @Log(title = "菜单-单关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFormMenu sysFormMenu)
    {
        List<SysFormMenu> list = sysFormMenuService.selectSysFormMenuList(sysFormMenu);
        ExcelUtil<SysFormMenu> util = new ExcelUtil<SysFormMenu>(SysFormMenu.class);
        return util.exportExcel(list, "menu");
    }

    /**
     * 新增菜单-单关联
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存菜单-单关联
     */
    @RequiresPermissions("form:formMenu:add")
    @Log(title = "菜单-单关联", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFormMenu sysFormMenu)
    {
        return toAjax(sysFormMenuService.insertSysFormMenu(sysFormMenu));
    }

    /**
     * 修改菜单-单关联
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysFormMenu sysFormMenu = sysFormMenuService.selectSysFormMenuById(id);
        mmap.put("sysFormMenu", sysFormMenu);
        return prefix + "/edit";
    }

    /**
     * 修改保存菜单-单关联
     */
    @RequiresPermissions("form:formMenu:edit")
    @Log(title = "菜单-单关联", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFormMenu sysFormMenu)
    {
        return toAjax(sysFormMenuService.updateSysFormMenu(sysFormMenu));
    }

    /**
     * 删除菜单-单关联
     */
    @RequiresPermissions("form:formMenu:remove")
    @Log(title = "菜单-单关联", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysFormMenuService.deleteSysFormMenuByIds(ids));
    }
}
