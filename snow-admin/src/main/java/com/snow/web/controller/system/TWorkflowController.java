package com.snow.web.controller.system;

import com.snow.common.core.controller.BaseController;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.http.HttpUtils;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.service.ISysFormInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/TWorkflow")
public class TWorkflowController extends BaseController {


    @Autowired
    private ISysFormInstanceService sysFormInstanceService;

    /***
     *  根据菜单code获取对应表单code，根据表单code，获取表单的id,用于重定向
     *
     * @param menuCode 菜单代码
     * @return
     */
    @GetMapping("/getTWorkflow/{menuCode}")
    public String getTWorkflow(@PathVariable("menuCode") String menuCode){
        if(StringUtils.isNull(menuCode)){
            return "business";
        }
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceByMenuCode(menuCode);
        if(StringUtils.isNull(sysFormInstance)){
            return "business";
        }
        Long id = sysFormInstance.getId();
        return "forward:/from/instance/getFromInfo?formId=" + id;
    }

}
