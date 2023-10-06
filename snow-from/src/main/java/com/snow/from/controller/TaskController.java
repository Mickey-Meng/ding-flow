package com.snow.from.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.annotation.RepeatSubmit;
import com.snow.common.constant.SequenceConstants;
import com.snow.common.core.controller.BaseController;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.enums.FormFieldTypeEnum;
import com.snow.common.utils.StringUtils;
import com.snow.flowable.common.constants.FlowConstants;
import com.snow.flowable.common.enums.FlowTypeEnum;
import com.snow.flowable.domain.*;
import com.snow.flowable.service.FlowableService;
import com.snow.flowable.service.FlowableTaskService;
import com.snow.framework.util.ShiroUtils;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormField;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.domain.request.FormFieldRequest;
import com.snow.from.domain.request.FormRequest;
import com.snow.from.service.ISysFormDataRecordService;
import com.snow.from.service.ISysFormInstanceService;
import com.snow.from.service.SysClickHouseService;
import com.snow.from.service.impl.SysFormDataRecordServiceImpl;
import com.snow.from.service.impl.SysFormFieldServiceImpl;
import com.snow.from.util.FormUtils;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysMenuService;
import com.snow.system.service.ISysSequenceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/18 14:55
 */
@Controller
@RequestMapping("hadopTask")
@Slf4j
public class TaskController extends BaseController {




    @Autowired
    private ISysFormInstanceService sysFormInstanceService;



    /**
     * 预览
     *
     * @return 预览页
     */
    @GetMapping("selectCreateTbale")
    @ResponseBody
    public String fromPreview() throws IOException {
        sysFormInstanceService.selectCreateTbale();

        return "selectCreateTbale";
    }
    /**
     * 预览
     *
     * @return 预览页
     */
    @GetMapping("tasklist")
    @ResponseBody
    public String list() {
        sysFormInstanceService.tasklist();

        return "tasklist";
    }




}
