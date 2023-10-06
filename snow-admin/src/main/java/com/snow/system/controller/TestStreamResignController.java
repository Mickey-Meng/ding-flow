package com.snow.system.controller;

import java.util.List;

import com.snow.flowable.domain.resign.SysOaResignForm;
import com.snow.flowable.domain.test.TestStreamResignForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysOaResign;
import com.snow.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
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
import com.snow.system.domain.TestStreamResign;
import com.snow.system.service.ITestStreamResignService;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.common.core.page.TableDataInfo;

/**
 * 工作流测试Controller
 * 
 * @author Agee
 * @date 2022-11-30
 */
@Controller
@RequestMapping("/system/test")
public class TestStreamResignController extends BaseController
{
    private String prefix = "system/test";

    @Autowired
    private ITestStreamResignService testStreamResignService;
    @Autowired
    private FlowableService flowableService;
    @Autowired
    private FlowableTaskService flowableTaskService;

    @RequiresPermissions("system:test:view")
    @GetMapping()
    public String test()
    {
        return prefix + "/test";
    }

    /**
     * 查询工作流测试列表
     */
    @RequiresPermissions("system:test:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TestStreamResign testStreamResign)
    {
        startPage();
        List<TestStreamResign> list = testStreamResignService.selectTestStreamResignList(testStreamResign);
        return getDataTable(list);
    }

    /**
     * 导出工作流测试列表
     */
    @RequiresPermissions("system:test:export")
    @Log(title = "工作流测试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TestStreamResign testStreamResign)
    {
        List<TestStreamResign> list = testStreamResignService.selectTestStreamResignList(testStreamResign);
        ExcelUtil<TestStreamResign> util = new ExcelUtil<TestStreamResign>(TestStreamResign.class);
        return util.exportExcel(list, "test");
    }

    /**
     * 新增工作流测试
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存工作流测试
     */
    @RequiresPermissions("system:test:add")
    @Log(title = "工作流测试", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TestStreamResign testStreamResign)
    {
        return toAjax(testStreamResignService.insertTestStreamResign(testStreamResign));
    }

    /**
     * 修改工作流测试
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TestStreamResign testStreamResign = testStreamResignService.selectTestStreamResignById(id);
        mmap.put("testStreamResign", testStreamResign);
        return prefix + "/edit";
    }

    /**
     * 修改保存工作流测试
     */
    @RequiresPermissions("system:test:edit")
    @Log(title = "工作流测试", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TestStreamResign testStreamResign)
    {
        SysUser sysUser = ShiroUtils.getSysUser();
        testStreamResign.setUpdateBy(String.valueOf(sysUser.getUserId()));
        testStreamResign.setProcessStatus(1L);
        int i = testStreamResignService.updateTestStreamResign(testStreamResign);
        TestStreamResign testStreamResign1 = testStreamResignService.selectTestStreamResignById(testStreamResign.getId());
        //发起审批
        TestStreamResignForm sysOaResignForm=new TestStreamResignForm();
        BeanUtils.copyProperties(testStreamResign1,sysOaResignForm);
        sysOaResignForm.setBusinessKey(sysOaResignForm.getName());
        sysOaResignForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        sysOaResignForm.setBusVarUrl("/system/test/detail");
        ProcessInstance processInstance = flowableService.startProcessInstanceByAppForm(sysOaResignForm);
        //推进任务节点
        flowableTaskService.automaticTask(processInstance.getProcessInstanceId());
        return toAjax(i);
    }

    /**
     * 删除工作流测试
     */
    @RequiresPermissions("system:test:remove")
    @Log(title = "工作流测试", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(testStreamResignService.deleteTestStreamResignByIds(ids));
    }
}
