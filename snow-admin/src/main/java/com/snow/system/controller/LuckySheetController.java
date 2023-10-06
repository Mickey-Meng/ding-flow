package com.snow.system.controller;

import com.snow.common.annotation.Log;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.BusinessType;
import com.snow.common.utils.poi.ExcelUtil;
import com.snow.flowable.domain.test.TestStreamResignForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysUser;
import com.snow.system.domain.TestStreamResign;
import com.snow.system.service.ITestStreamResignService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作流测试Controller
 * 
 * @author Agee
 * @date 2022-11-30
 */
@Controller
@RequestMapping("/system/luckysheet")
public class LuckySheetController extends BaseController
{
    private String prefix = "system/luckysheet";

    @RequiresPermissions("system:luckysheet:view")
    @GetMapping()
    public String test()
    {
        return prefix + "/luckysheet";
    }
}
