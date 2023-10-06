package com.snow.system.controller;

import com.snow.common.annotation.Log;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.enums.BusinessType;
import com.snow.flowable.domain.purchaseOrder.PurchaseOrderForm;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.PurchaseOrderMain;
import com.snow.system.domain.SysUser;
import com.snow.system.service.IPurchaseOrderMainService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName FlowDefinitionController
 * @Description TODO
 * @Author Karl
 * @Date 2022/5/19 8:53
 * @Version 1.0
 */
@Controller
@RequestMapping("system/flowDefinition")
public class FlowDefinitionController {

    @Autowired
    protected ModelService modelService;
    @Autowired
    private IPurchaseOrderMainService purchaseOrderMainService;

    @Autowired
    private FlowableService flowableService;
    @Autowired
    private FlowableTaskService flowableTaskService;

    /**
     * 修改保存采购单并发起申请
     * lixiaojie  20220524
     * for:
     */

    @Log(title = "采购单主表", businessType = BusinessType.OTHER)
    @PostMapping("/startFlow")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult startFlow(String FlowId, String fromInfoId) throws IOException {
        PurchaseOrderMain purchaseOrderMain = new PurchaseOrderMain();
        SysUser sysUser = ShiroUtils.getSysUser();
        purchaseOrderMain.setUpdateBy(String.valueOf(sysUser.getUserId()));
        int i = purchaseOrderMainService.updatePurchaseOrderMain(purchaseOrderMain);
        PurchaseOrderMain newPurchaseOrderMain = purchaseOrderMainService.selectPurchaseOrderMainById(purchaseOrderMain.getId());
        //发起审批
        PurchaseOrderForm purchaseOrderForm = new PurchaseOrderForm();

        //System.out.println("");
        purchaseOrderForm.setBusinessKey(purchaseOrderMain.getOrderNo());
        purchaseOrderForm.setStartUserId(String.valueOf(sysUser.getUserId()));
        purchaseOrderForm.setBusVarUrl("/system/purchaseOrder/detail");
        AjaxResult processInstance = flowableService.startFlow(FlowId, sysUser.getUserId(), fromInfoId);


        //推进任务节点

        return processInstance;
    }

}
