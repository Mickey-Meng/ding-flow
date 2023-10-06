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
import com.snow.framework.config.QRCodeProperties;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.snow.framework.util.ShiroUtils.getSysUser;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/11/18 14:55
 */
@Controller
@RequestMapping()
@Slf4j
public class FormController extends BaseController {

    @Autowired
    private ISysFormInstanceService sysFormInstanceService;

    @Autowired
    private ISysFormDataRecordService iSysFormDataRecordService;

    @Autowired
    private SysFormFieldServiceImpl sysFormFieldService;

    @Autowired
    private SysFormDataRecordServiceImpl sysFormDataRecordService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private FlowableTaskService flowableTaskService;

    @Autowired
    private ISysSequenceService sequenceService;

    @Autowired
    private ISysMenuService sysMenuService;

    /**
     * 跳转form表单首页
     *
     * @return 首页url路径
     */
    @GetMapping("formIndex")
    public String fromPreview() {
        return "formIndex";
    }

    @GetMapping("preview.html")
    public String preview() {
        return "preview";
    }

    @GetMapping("handwrittenSignature.html")
    public String handwrittenSignature() {
        return "handwrittenSignature";
    }

    @GetMapping("editorMenu.html")
    public String editorMenu() {
        return "editorMenu";
    }


    @GetMapping("updateFromPreview")
    public String updateFromPreview(String formDetailsId, ModelMap mmap) {


        SysFormDataRecord sysFormDataRecord = iSysFormDataRecordService.selectSysFormDataRecordById(Integer.parseInt(formDetailsId));


//        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(1);
        mmap.put("formId", sysFormDataRecord.getFormId());
        mmap.put("name", "");
        mmap.put("formDetailsId", formDetailsId);
        return "updateFromPreview";
    }


    @GetMapping("updateFromPreviewInfo")
    @ResponseBody
    public Map<Object, Object> updateFromPreviewInfo(String formDetailsId) {


        SysFormDataRecord sysFormDataRecord = iSysFormDataRecordService.selectSysFormDataRecordById(Integer.parseInt(formDetailsId));

        Map<Object, Object> item = new HashMap<>();
        item.put("sysFormDataRecordId", sysFormDataRecord.getId());
        JSONObject formField = JSONObject.parseObject(sysFormDataRecord.getFormField());
        JSONArray jsonObject = JSONObject.parseArray(sysFormDataRecord.getFormData());
        for (Object o : jsonObject) {
            JSONObject json = (JSONObject) o;
//            item.put(json.get("id"), json.get("defaultValue"));

            if (!json.get("tag").toString().equals("grid")) {
                Map<String, Object> stringObjectMap = resolverColumn(json);
                if (!stringObjectMap.isEmpty()) {
                    item.put(stringObjectMap.get("field"), formField.get(stringObjectMap.get("field")));
                }
            } else {
                List<Map<String, Object>> maps = resolverGrid(json);
                for (Map<String, Object> map : maps) {
                    if (!map.isEmpty()) {
                        item.put(map.get("field"), formField.get(map.get("field")));
                    }

                }

            }

        }


        return item;
    }


    @GetMapping("formDataUrl")
    public String formDataUrl(String formCode, ModelMap mmap) {

        mmap.put("formCode", formCode);

        return "flow/fromData";
    }


    @PostMapping("formData")
    @ResponseBody
    public TableDataInfo formData(String formCode, @RequestParam Map<String, Object> params) {
//        formCode = "P_INFO";
        startPage();

        List<SysFormDataRecord> list = sysFormInstanceService.ListSysFormInstanceByFormCode(formCode);
        List<Map<Object, Object>> pageList = new ArrayList<>();
        params.remove("formCode");
        params.remove("pageNum");
        params.remove("pageSize");
        params.remove("orderByColumn");
        params.remove("isAsc");


        List<Map<Object, Object>> listDataMap = new ArrayList<>();

        for (SysFormDataRecord sysFormDataRecord : list) {

            Map<Object, Object> item = new HashMap<>();
            item.put("sysFormDataRecordId", sysFormDataRecord.getId());
            JSONArray jsonObject = JSONObject.parseArray(sysFormDataRecord.getFormData());
            JSONObject formField = JSONObject.parseObject(sysFormDataRecord.getFormField());
            for (Object o : jsonObject) {
                JSONObject json = (JSONObject) o;
                if (!json.get("tag").toString().equals("grid")) {
                    Map<String, Object> stringObjectMap = resolverColumn(json);
                    if (!stringObjectMap.isEmpty()) {
                        item.put(stringObjectMap.get("field"), formField.get(stringObjectMap.get("field")));
                    }
                } else {
                    List<Map<String, Object>> maps = resolverGrid(json);
                    for (Map<String, Object> map : maps) {
                        if (!map.isEmpty()) {
                            item.put(map.get("field"), formField.get(map.get("field")));
                        }

                    }

                }
            }
            listDataMap.add(item);

        }
        AtomicBoolean flage = new AtomicBoolean(false);
        params.forEach((i, e) -> {
            if ("".equals(e) || e == null) {
                return;
            }
            flage.set(true);
        });
        if (flage.get()) {
            for (Map<Object, Object> objectObjectMap : listDataMap) {

                AtomicBoolean is_show = new AtomicBoolean(true);
                params.forEach((i, e) -> {
                    if ("".equals(e) || e == null) {
                        return;
                    }
                    if (!e.equals(objectObjectMap.get(i))) {
                        is_show.set(false);
                        return;
                    }
                });
                if (is_show.get()) {
                    pageList.add(objectObjectMap);
                }
            }

        } else {
            pageList.addAll(listDataMap);
        }


        TableDataInfo dataTable = getDataTable(pageList);
        dataTable.setRows(pageList);
        return dataTable;

    }


    @PostMapping("formDataColumn")
    @ResponseBody
    public List<Map<String, Object>> formDataColumn(String formCode) {
//        formCode = "P_INFO";
        startPage();
        List<SysFormDataRecord> list = sysFormInstanceService.ListSysFormInstanceByFormCode(formCode);
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        SysFormDataRecord sysFormDataRecord = list.get(0);

        List<Map<String, Object>> result = new ArrayList<>();
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.parseLong(sysFormDataRecord.getFormId()));

        JSONArray jsonObject = JSONObject.parseArray(sysFormDataRecord.getFormData());


        // 如果是grid
        for (Object o : jsonObject) {
            JSONObject json = (JSONObject) o;
            Map<String, Object> item = new HashMap<>();
            if (!json.get("tag").toString().equals("grid")) {
                Map<String, Object> stringObjectMap = resolverColumn(json);

                if (!stringObjectMap.isEmpty()) {
                    result.add(stringObjectMap);
                }

            } else {
                result.addAll(resolverGrid(json));
            }


        }
        return result;

    }

    //判断是否是 grid  如果是 grid 就递归去找下一层,  如果不是grid 就找flagView
    private List<Map<String, Object>> resolverGrid(JSONObject json) {
        List<Map<String, Object>> result = new ArrayList<>();

        JSONArray columns = json.getJSONArray("columns");
        if (columns == null) {
            return result;
        }
        for (Object column : columns) {
            if (column == null) {
                continue;
            }
            JSONObject column_json = (JSONObject) column;
            JSONArray listArray = column_json.getJSONArray("list");
            for (Object listArrayitem : listArray) {
                if (listArrayitem == null) {
                    continue;
                }
                JSONObject arrayitemJson = (JSONObject) listArrayitem;
                if (!arrayitemJson.get("tag").toString().equals("grid")) {

                    Map<String, Object> stringObjectMap = resolverColumn(arrayitemJson);

                    if (!stringObjectMap.isEmpty()) {
                        result.add(stringObjectMap);
                    }


                } else {
                    List<Map<String, Object>> next = resolverGrid(arrayitemJson);
                    result.addAll(next);
                }
            }
        }
        return result;
    }

    private Map<String, Object> resolverColumn(JSONObject json) {
        Object flagView = json.get("flagView");
        Map<String, Object> item = new HashMap<>();
        if (flagView == null) {
            return item;
        }
        if (flagView.toString().equals("false")) {
            return item;
        }

        if (json.containsKey("selectFlag")) {
            item.put("selectFlag", json.get("selectFlag"));
        } else {
            item.put("selectFlag", "flase");
        }
        item.put("title", json.get("label"));
        String s = json.get("id").toString().replaceAll("date", "datedate");
        item.put("field", s);
        return item;

    }


    /**
     * 保存表单数据
     *
     * @param formRequest 表单参数
     * @return 是否成功
     */
    @PostMapping("/form/saveForm")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult saveForm(FormRequest formRequest) {
        log.info("@@保存设计的表单前端传入的数据:{}", JSON.toJSONString(formRequest));
        String formData = formRequest.getFormData();
        if (StrUtil.isBlank(formData)) {
            return AjaxResult.error("还没有创建组件呢！");
        }
        if (StringUtils.isEmpty(formRequest.getFormId())) {
            return AjaxResult.error("表单id必填项");
        }
        if (StringUtils.isEmpty(formRequest.getFormName())) {
            return AjaxResult.error("表单名称必填项");
        }
        SysFormInstance sysFormInstanceCode = sysFormInstanceService.selectSysFormInstanceByFormCode(formRequest.getFormId());
        if (StringUtils.isNotNull(sysFormInstanceCode)) {
            return AjaxResult.error(StrUtil.format("表单编号:{}已存在", formRequest.getFormId()));
        }
        SysFormInstance sysFormInstanceName = sysFormInstanceService.selectSysFormInstanceByFormName(formRequest.getFormName());
        if (StringUtils.isNotNull(sysFormInstanceName)) {
            return AjaxResult.error(StrUtil.format("表单名称:{}已存在", formRequest.getFormName()));
        }
        formRequest.setMenuCode("2135");

//        SysMenu sysMenu = sysMenuService.selectMenuByCode(formRequest.getMenuCode());
//        if(StringUtils.isNull(sysMenu)){
//            return AjaxResult.error(StrUtil.format("菜单编号:{}不存在",formRequest.getMenuCode()));
//        }
        //保存主表数据
        SysFormInstance sysFormInstance = new SysFormInstance();
        sysFormInstance.setFormCode(formRequest.getFormId());
        sysFormInstance.setFormName(formRequest.getFormName());
        sysFormInstance.setMenuCode(formRequest.getMenuCode());
        sysFormInstance.setRev(1L);
        sysFormInstance.setFromContentHtml(formData);
        sysFormInstance.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        sysFormInstance.setUpdateTime(new Date());
        sysFormInstanceService.insertSysFormInstance(sysFormInstance);
        //保存子表数据
        saveFormField(sysFormInstance.getId(), formData);
        return AjaxResult.success();
    }

    /**
     * 完成任务
     *
     * @return
     */
    @PostMapping("/finishTask")
    @RequiresPermissions("system:flow:finishTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult finishTask(FinishTaskDTO finishTaskDTO) {
        SysUser sysUser = getSysUser();
        finishTaskDTO.setUserId(String.valueOf(sysUser.getUserId()));
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }


    /**
     * 保存表单填写记录
     *
     * @return
     */
    @PostMapping("updateFormRecord")
    @ResponseBody
    public AjaxResult updateFormRecord(@RequestParam String formId, @RequestParam String formData, @RequestParam String formField, Integer formDetailsId) {


        //生成单号
        String formNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_FORM_SEQUENCE);
        //把用户填写的值赋值到表单里面去
        String newFormData = FormUtils.fillFormFieldValue(formData, formField);
        Long userId = ShiroUtils.getUserId();
        SysFormDataRecord sysFormDataRecord = new SysFormDataRecord();
        sysFormDataRecord.setBelongUserId(String.valueOf(userId));
        sysFormDataRecord.setFormData(newFormData);
        sysFormDataRecord.setFormId(formId);
        sysFormDataRecord.setFormField(formField);
        sysFormDataRecord.setCreateBy(String.valueOf(userId));

        sysFormDataRecord.setId(formDetailsId);
        //获取最大版本号
        Integer maxVersion = sysFormDataRecordService.getMaxVersionByUsrId(userId);
        //版本号+1组成最新版本号
        Integer version = Optional.ofNullable(maxVersion).orElse(0) + 1;
        sysFormDataRecord.setVersion(version);
        sysFormDataRecord.setFormNo(formNo);
        sysFormDataRecordService.updateSysFormDataRecord(sysFormDataRecord);


        return AjaxResult.success(sysFormDataRecord.getId());
    }

    /**
     * 预览
     *
     * @return 预览页
     */
    @GetMapping("fromPreview")
    public String fromPreview(@RequestParam Long id, ModelMap mmap, String taskId) {
//        sysFormInstanceService.selectCreateTbale();
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("formId", id);
        mmap.put("taskId", taskId);
        mmap.put("name", sysFormInstance.getFormName());
        return "fromPreview";
    }

    /**
     * 预览
     *
     * @return 预览页
     */
    @GetMapping("tasklist")
    public String list(@RequestParam Long id, ModelMap mmap, String taskId) {
        sysFormInstanceService.tasklist();

        return "fromPreview";
    }


    /**
     * 跳转绑定流程页
     *
     * @return 跳转绑定流程页
     */
    @GetMapping("bindProcess")
    public String bindProcess(@RequestParam Long id, ModelMap mmap) {
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(id);
        mmap.put("sysFormInstance", sysFormInstance);
        return "system/instance/bindProcess";
    }

    @Autowired
    SysClickHouseService sysClickHouseService;

    /**
     * 保存表单填写记录
     *
     * @return
     */
    @PostMapping("/form/saveFormRecord")
    @ResponseBody
    public AjaxResult saveFormRecord(@RequestParam String taskId, @RequestParam String formId, @RequestParam String formData, @RequestParam String formField) {
        SysUser sysUser = getSysUser();

        String processDefinitionId = null;
        String taskName = null;
        Integer taskVersion = 1;// 20220626 由null改为0，否则表单查询的时候，判断is not null;

        if (taskId != null && !"".equals(taskId)) {
            Task task = flowableTaskService.getTask(taskId);
            processDefinitionId = task.getProcessInstanceId();
            SysFormDataRecord sysFormDataRecordSelect = new SysFormDataRecord();
            sysFormDataRecordSelect.setFlowId(processDefinitionId);
            sysFormDataRecordSelect.setTaskName(task.getName());
            taskName = task.getName();
            List<SysFormDataRecord> sysFormDataRecords = sysFormDataRecordService.selectSysFormDataRecordList(sysFormDataRecordSelect);
            taskVersion = sysFormDataRecords.size() + 1;
        }
        //生成单号
        String formNo = sequenceService.getNewSequenceNo(SequenceConstants.OA_FORM_SEQUENCE);
        //把用户填写的值赋值到表单里面去
        String newFormData = FormUtils.fillFormFieldValue(formData, formField);
        SysUser sysUser1 = getSysUser();
        Long userId = null;
        if (sysUser1 == null) {
            userId = 1L;

        } else {
            userId = ShiroUtils.getUserId();
        }

        SysFormDataRecord sysFormDataRecord = new SysFormDataRecord();
        sysFormDataRecord.setBelongUserId(String.valueOf(userId));
        sysFormDataRecord.setFormData(newFormData);
        sysFormDataRecord.setFormId(formId);
        sysFormDataRecord.setFormField(formField);
        sysFormDataRecord.setCreateBy(String.valueOf(userId));
        if (processDefinitionId == null) {
            processDefinitionId = UUID.randomUUID().toString();
        }
        sysFormDataRecord.setFlowId(processDefinitionId);
        sysFormDataRecord.setTaskName(taskName);
        sysFormDataRecord.setTaskVersion(taskVersion);
        //获取最大版本号
        Integer maxVersion = sysFormDataRecordService.getMaxVersionByUsrId(userId);
        //版本号+1组成最新版本号
        Integer version = Optional.ofNullable(maxVersion).orElse(0) + 1;
        sysFormDataRecord.setVersion(version);
        sysFormDataRecord.setFormNo(formNo);
        sysFormDataRecordService.insertSysFormDataRecord(sysFormDataRecord);


        if (taskId != null && !"".equals(taskId)) {
            Object parse = JSONObject.parse(formField);
            Object parse1 = JSON.parse(formField);
            HashMap hashMap = JSONObject.parseObject(formField, HashMap.class);


            flowableTaskService.submitTaskFrom(taskId, sysUser.getUserId(), sysFormDataRecord.getId(), hashMap);


//            sysClickHouseService.insertTaskFrom(taskId,formData,formField,processDefinitionId);

        }

        return AjaxResult.success(sysFormDataRecord.getId());
    }

    /**
     * 跳转到详情
     *
     * @param id  记录id
     * @param map 返回前端的数据
     * @return 跳转页面
     */
    @GetMapping("/toFormRecordDetail")
    public String toFormRecordDetail(String id, ModelMap map) {
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordById(Integer.valueOf(id));
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.valueOf(sysFormDataRecord.getFormId()));
        map.put("id", id);
        map.put("name", sysFormInstance.getFormName());
        String formId = sysFormDataRecord.getFormId();
        map.put("formId", formId);
        map.put("formDetailsId", id);

        map.put("createTime", DateUtil.formatDateTime(sysFormDataRecord.getCreateTime()));
        return "formDetail";
    }


    /**
     * 表单详情
     *
     * @param id 表单记录id
     * @return 表单数据
     */
    @PostMapping("/form/getFormRecordDetail")
    @ResponseBody
    public AjaxResult getFormRecordDetail(Integer id) {
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordById(id);
        return AjaxResult.success(sysFormDataRecord.getFormData());
    }

    /**
     * 发起流程
     *
     * @param id 表单记录id
     * @return 是否发起成功
     */
    @GetMapping("/form/startProcess")
    @ResponseBody
    @Transactional
    public AjaxResult startProcess(Integer id) {
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordById(id);
        SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.parseLong(sysFormDataRecord.getFormId()));
        StartProcessDTO startProcessDTO = new StartProcessDTO();
        startProcessDTO.setStartUserId(String.valueOf(ShiroUtils.getUserId()));
        startProcessDTO.setBusinessKey(sysFormDataRecord.getFormNo());
        startProcessDTO.setProcessDefinitionKey(sysFormInstance.getProcessKey());
        String formData = sysFormDataRecord.getFormData();
        String formField = sysFormDataRecord.getFormField();
        Map<String, Object> variables = Convert.toMap(String.class, Object.class, JSON.parse(formField));
        variables.put(FlowConstants.DF_FORM_ID, sysFormDataRecord.getFormId());
        variables.put(FlowConstants.FORM_DATA, formData);
        variables.put(FlowConstants.PROCESS_TYPE, FlowTypeEnum.FORM_PROCESS.getCode());
        startProcessDTO.setVariables(variables);
        ProcessInstance processInstance = flowableService.startProcessInstanceByKey(startProcessDTO);
        log.info("@@表单编号：{},发起流程id：{}", sysFormDataRecord.getFormNo(), processInstance.getId());
        return AjaxResult.success();
    }

    @Autowired
    private QRCodeProperties qRCodeProperties;

    /**
     * 生成二维码
     */
    @GetMapping("/createQRCode")
    public void createQRCode(@RequestParam("id") int id, HttpServletResponse response) throws UnknownHostException {
        Object domain = Inet4Address.getLocalHost().getHostAddress();
        QrConfig config = new QrConfig(500, 500);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN);
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY);
        config.setQrVersion(10);
        // config.setImg("https://qimetons.oss-cn-beijing.aliyuncs.com/45a22bcc93644dfe8bcacf690fe133f3.png");
        // 生成二维码
        String format = StrUtil.format("http://{}:{}/fromPreview?id={}", qRCodeProperties.getLocalhost(), qRCodeProperties.getHostAddress(), id);
        System.out.println("format===========" + format);
        log.info("format===========" + format);
        BufferedImage bufferedImage = QrCodeUtil.generate(format, config);
        try {
            //以PNG格式向客户端发送
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "PNG", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new RuntimeException("生成二维码异常");
        }
    }

    /**
     * 跳转待办页
     *
     * @param taskId 任务id
     * @param mmap   返回参数
     * @return 页面
     */
    @GetMapping("/toFinishTask")
    public String toFinishTask(String taskId, ModelMap mmap) {
        Task task = flowableTaskService.getTask(taskId);
        HistoricProcessInstance historicProcessInstance = flowableService.getHistoricProcessInstanceById(task.getProcessInstanceId());
        Object formData = flowableService.getHisVariable(task.getProcessInstanceId(), FlowConstants.FORM_DATA);
        Object formId = flowableService.getHisVariable(task.getProcessInstanceId(), FlowConstants.DF_FORM_ID);
        if (ObjectUtil.isNotEmpty(formId)) {
            SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.parseLong(String.valueOf(formId)));
            mmap.put("name", sysFormInstance.getFormName());
        }
        mmap.put("taskId", taskId);
        mmap.put("businessKey", historicProcessInstance.getBusinessKey());
        mmap.put("processInstanceId", task.getProcessInstanceId());
        mmap.put("formData", String.valueOf(formData));
        return "formProcessDetail";
    }

    /**
     * 流程表单详情
     *
     * @param processInstanceId 流程实例id
     * @return 表单数据
     */
    @PostMapping("/form/getProcessFormData")
    @ResponseBody
    public AjaxResult getProcessFormData(String processInstanceId) {
        Object formData = flowableService.getHisVariable(processInstanceId, FlowConstants.FORM_DATA);
        return AjaxResult.success(String.valueOf(formData));
    }

    /**
     * 提交任务
     *
     * @param finishTaskDTO 前端参数
     */
    @PostMapping("/form/submitTask")
    @ResponseBody
    @RepeatSubmit
    public AjaxResult submitTask(FinishTaskDTO finishTaskDTO) {
        finishTaskDTO.setUserId(String.valueOf(ShiroUtils.getUserId()));
        finishTaskDTO.setIsUpdateBus(true);
        flowableTaskService.submitTask(finishTaskDTO);
        return AjaxResult.success();
    }

    /**
     * 跳转已办任务详情
     *
     * @param taskId   任务id
     * @param modelMap 返回map
     * @return 返回页面
     */
    @GetMapping("/getMyTaskedDetail")
    public String getMyHisTaskedDetail(String taskId, ModelMap modelMap) {
        //获取任务实例
        HistoricTaskInstanceVO hisTask = flowableTaskService.getHisTask(taskId);
        Object formId = flowableService.getHisVariable(hisTask.getProcessInstanceId(), FlowConstants.DF_FORM_ID);
        if (ObjectUtil.isNotEmpty(formId)) {
            SysFormInstance sysFormInstance = sysFormInstanceService.selectSysFormInstanceById(Long.parseLong(String.valueOf(formId)));
            modelMap.put("name", sysFormInstance.getFormName());
        }
        //获取流程实例
        ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(hisTask.getProcessInstanceId());
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordByFormNo(processInstanceVo.getBusinessKey());
        modelMap.put("hisTask", hisTask);
        modelMap.put("appId", sysFormDataRecord.getId());
        modelMap.put("processInstance", processInstanceVo);
        return "/myTaskedDetail";
    }

    /**
     * 跳转我发起的流程详情
     */
    @GetMapping("/startFormProcessDetail")
    @RequiresPermissions("system:flow:myStartProcessDetail")
    public String myStartProcessDetail(String processInstanceId, ModelMap modelMap) {
        ProcessInstanceVO processInstanceVo = flowableService.getProcessInstanceVoById(processInstanceId);
        HistoricTaskInstanceDTO historicTaskInstanceDTO = new HistoricTaskInstanceDTO();
        historicTaskInstanceDTO.setProcessInstanceId(processInstanceId);
        List<HistoricTaskInstanceVO> historicTaskInstanceList = flowableTaskService.getHistoricTaskInstanceNoPage(historicTaskInstanceDTO);
        SysFormDataRecord sysFormDataRecord = sysFormDataRecordService.selectSysFormDataRecordByFormNo(processInstanceVo.getBusinessKey());
        modelMap.put("historicTaskInstanceList", historicTaskInstanceList);
        modelMap.put("processInstanceId", processInstanceId);
        modelMap.put("processInstance", processInstanceVo);
        modelMap.put("appId", sysFormDataRecord.getId());
        return "/startFormProcessDetail";
    }

    /**
     * 构建子表数据
     *
     * @param formId   表单id
     * @param formData 表单数据
     */
    private void saveFormField(Long formId, String formData) {
        //解析表单
        JSONArray formDataArray = JSON.parseArray(formData);
        for (int i = 0; i < formDataArray.size(); i++) {
            JSONObject fieldObject = formDataArray.getJSONObject(i);
            //一行多列布局
            if (fieldObject.getString("tag").equals(FormFieldTypeEnum.GRID.getCode())) {
                JSONObject gridObject = formDataArray.getJSONObject(i);
                JSONArray columnArray = gridObject.getJSONArray("columns");
                for (int j = 0; j < columnArray.size(); j++) {
                    JSONObject columnObject = columnArray.getJSONObject(j);
                    JSONArray listArray = columnObject.getJSONArray("list");
                    for (int k = 0; k < listArray.size(); k++) {
                        JSONObject listObject = listArray.getJSONObject(k);
                        FormFieldRequest formFieldRequest = listObject.toJavaObject(FormFieldRequest.class);
                        saveSysFormField(formFieldRequest, formId, JSON.toJSONString(listObject));
                    }
                }
            }
            //正常单行布局
            else {
                FormFieldRequest formFieldRequest = fieldObject.toJavaObject(FormFieldRequest.class);
                saveSysFormField(formFieldRequest, formId, JSON.toJSONString(fieldObject));
            }
        }
    }

    /**
     * 保存
     */
    public void saveSysFormField(FormFieldRequest formFieldRequest, Long formId, String jsonString) {
        SysFormField sysFormField = BeanUtil.copyProperties(formFieldRequest, SysFormField.class, "id");
        sysFormField.setFromId(formId);
        sysFormField.setFieldKey(formFieldRequest.getId());
        sysFormField.setFieldName(formFieldRequest.getLabel());
        sysFormField.setFieldType(formFieldRequest.getTag());
        sysFormField.setFieldHtml(jsonString);
        sysFormFieldService.insertSysFormField(sysFormField);
    }

}
