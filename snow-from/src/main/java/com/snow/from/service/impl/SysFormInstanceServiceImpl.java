package com.snow.from.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.common.core.text.Convert;
import com.alibaba.fastjson.JSONObject;
import com.snow.common.utils.DateUtils;
import com.snow.from.controller.txtExport;
import com.snow.from.domain.SysFormDataRecord;
import com.snow.from.domain.SysFormInstance;
import com.snow.from.mapper.SysFormInstanceMapper;
import com.snow.from.service.ISysFormInstanceService;
import com.snow.from.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 单实例Service业务层处理
 *
 * @author 没用的阿吉
 * @date 2021-03-21
 */
@Service
public class SysFormInstanceServiceImpl extends ServiceImpl<SysFormInstanceMapper, SysFormInstance> implements ISysFormInstanceService {

    @Resource
    private SysFormInstanceMapper sysFormInstanceMapper;

    /**
     * 查询表单实例
     *
     * @param id 表单实例ID
     * @return 表单实例
     */
    @Override
    public SysFormInstance selectSysFormInstanceById(Long id) {
        return sysFormInstanceMapper.selectById(id);
    }

    @Override
    public SysFormInstance selectSysFormInstanceByMenuCode(String menuCode) {
        return sysFormInstanceMapper.selectByMenuCode(menuCode);
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormCode(String fromCode) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        return sysFormInstanceMapper.selectOne(lambda.eq(SysFormInstance::getFormCode, fromCode));
    }

    @Override
    public SysFormInstance selectSysFormInstanceByFormName(String fromName) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        return sysFormInstanceMapper.selectOne(lambda.eq(SysFormInstance::getFormCode, fromName));
    }

    /**
     * 查询表单实例列表
     *
     * @param sysFormInstance 表单实例
     * @return 表单实例
     */
    @Override
    public List<SysFormInstance> selectSysFormInstanceList(SysFormInstance sysFormInstance) {
        LambdaQueryWrapper<SysFormInstance> lambda = new QueryWrapper<SysFormInstance>().lambda();
        lambda.like(StrUtil.isNotBlank(sysFormInstance.getFormCode()), SysFormInstance::getFormCode, sysFormInstance.getFormCode());
        lambda.like(StrUtil.isNotBlank(sysFormInstance.getFormName()), SysFormInstance::getFormName, sysFormInstance.getFormName());
        lambda.orderByDesc(SysFormInstance::getCreateTime);
        return sysFormInstanceMapper.selectList(lambda);
    }

    /**
     * 新增表单实例
     *
     * @param sysFormInstance 表单实例
     * @return 结果
     */
    @Override
    public int insertSysFormInstance(SysFormInstance sysFormInstance) {
        sysFormInstance.setCreateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.insert(sysFormInstance);
    }

    /**
     * 修改表单实例
     *
     * @param sysFormInstance 表单实例
     * @return 结果
     */
    @Override
    public int updateSysFormInstance(SysFormInstance sysFormInstance) {
        sysFormInstance.setUpdateTime(DateUtils.getNowDate());
        return sysFormInstanceMapper.updateById(sysFormInstance);
    }

    /**
     * 删除表单实例对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceByIds(String ids) {
        return sysFormInstanceMapper.deleteBatchIds(Convert.toStrList(ids));
    }

    /**
     * 删除表单实例信息
     *
     * @param id 表单实例ID
     * @return 结果
     */
    @Override
    public int deleteSysFormInstanceById(Long id) {
        return sysFormInstanceMapper.deleteById(id);
    }

    @Override
    public List<SysFormDataRecord> ListSysFormInstanceByFormCode(String formCode) {
        return sysFormInstanceMapper.ListSysFormInstanceByFormCode(formCode);
    }

    static Map<String, InformationSchema> InformationSchema = new HashMap<>();

    static {

        InformationSchema.put("invest_account_info", new InformationSchema("investment-center", "invest_account_info"));
        InformationSchema.put("invest_cost_payment", new InformationSchema("investment-center", "invest_cost_payment"));
        InformationSchema.put("invest_cost_plan", new InformationSchema("investment-center", "invest_cost_plan"));
        InformationSchema.put("invest_cost_setting_ref", new InformationSchema("investment-center", "invest_cost_setting_ref"));
        InformationSchema.put("invest_interest_payment_plan", new InformationSchema("investment-center", "invest_interest_payment_plan"));
        InformationSchema.put("invest_iou_rollover_record", new InformationSchema("investment-center", "invest_iou_rollover_record"));
        InformationSchema.put("invest_loan_additional_info", new InformationSchema("investment-center", "invest_loan_additional_info"));
        InformationSchema.put("invest_loan_condition_verify", new InformationSchema("investment-center", "invest_loan_condition_verify"));
        InformationSchema.put("invest_loan_contract", new InformationSchema("investment-center", "invest_loan_contract"));
        InformationSchema.put("invest_loan_iou", new InformationSchema("investment-center", "invest_loan_iou"));
        InformationSchema.put("invest_loan_query", new InformationSchema("investment-center", "invest_loan_query"));
        InformationSchema.put("invest_loan_quotas_info", new InformationSchema("investment-center", "invest_loan_quotas_info"));
        InformationSchema.put("invest_loan_rate", new InformationSchema("investment-center", "invest_loan_rate"));
        InformationSchema.put("invest_loan_rate_result", new InformationSchema("investment-center", "invest_loan_rate_result"));
        InformationSchema.put("invest_repay_capital_interest", new InformationSchema("investment-center", "invest_repay_capital_interest"));
        InformationSchema.put("invest_repay_plan", new InformationSchema("investment-center", "invest_repay_plan"));
        InformationSchema.put("invest_tax_payment", new InformationSchema("investment-center", "invest_tax_payment"));
        InformationSchema.put("prod_base_info", new InformationSchema("product-center", "prod_base_info"));
        InformationSchema.put("prod_date_job", new InformationSchema("product-center", "prod_date_job"));
        InformationSchema.put("prod_date_setting", new InformationSchema("product-center", "prod_date_setting"));
        InformationSchema.put("prod_date_setting_log", new InformationSchema("product-center", "prod_date_setting_log"));
        InformationSchema.put("prod_directory", new InformationSchema("product-center", "prod_directory"));
        InformationSchema.put("prod_element", new InformationSchema("product-center", "prod_element"));
        InformationSchema.put("prod_element_search", new InformationSchema("product-center", "prod_element_search"));
        InformationSchema.put("prod_extension", new InformationSchema("product-center", "prod_extension"));
        InformationSchema.put("prod_extension_baseinfo", new InformationSchema("product-center", "prod_extension_baseinfo"));
        InformationSchema.put("prod_extension_income", new InformationSchema("product-center", "prod_extension_income"));
        InformationSchema.put("prod_file_path", new InformationSchema("product-center", "prod_file_path"));
        InformationSchema.put("prod_fund_raising_agency", new InformationSchema("product-center", "prod_fund_raising_agency"));
        InformationSchema.put("prod_group", new InformationSchema("product-center", "prod_group"));
        InformationSchema.put("prod_group_element_ref", new InformationSchema("product-center", "prod_group_element_ref"));
        InformationSchema.put("prod_income", new InformationSchema("product-center", "prod_income"));
        InformationSchema.put("prod_inventory", new InformationSchema("product-center", "prod_inventory"));
        InformationSchema.put("prod_message_identification", new InformationSchema("product-center", "prod_message_identification"));
        InformationSchema.put("prod_notice_attachment", new InformationSchema("product-center", "prod_notice_attachment"));
        InformationSchema.put("prod_notice_customer_handler", new InformationSchema("product-center", "prod_notice_customer_handler"));
        InformationSchema.put("prod_openday_scheme", new InformationSchema("product-center", "prod_openday_scheme"));
        InformationSchema.put("prod_performance_reward", new InformationSchema("product-center", "prod_performance_reward"));
        InformationSchema.put("prod_performance_reward_log", new InformationSchema("product-center", "prod_performance_reward_log"));
        InformationSchema.put("prod_product_notice", new InformationSchema("product-center", "prod_product_notice"));
        InformationSchema.put("prod_shareday_scheme", new InformationSchema("product-center", "prod_shareday_scheme"));
        InformationSchema.put("prod_shareday_scheme_log", new InformationSchema("product-center", "prod_shareday_scheme_log"));
        InformationSchema.put("prod_sharescheme_baseinfo", new InformationSchema("product-center", "prod_sharescheme_baseinfo"));
        InformationSchema.put("prod_template", new InformationSchema("product-center", "prod_template"));
        InformationSchema.put("prod_template_ref", new InformationSchema("product-center", "prod_template_ref"));
        InformationSchema.put("prod_usufruct", new InformationSchema("product-center", "prod_usufruct"));
        InformationSchema.put("ac_apply_contract", new InformationSchema("project-contract", "ac_apply_contract"));
        InformationSchema.put("ac_apply_contract_classify", new InformationSchema("project-contract", "ac_apply_contract_classify"));
        InformationSchema.put("ac_apply_contract_delaytime", new InformationSchema("project-contract", "ac_apply_contract_delaytime"));
        InformationSchema.put("ac_apply_contract_summary", new InformationSchema("project-contract", "ac_apply_contract_summary"));
        InformationSchema.put("ac_apply_guarantee_relation", new InformationSchema("project-contract", "ac_apply_guarantee_relation"));
        InformationSchema.put("ac_asset_management", new InformationSchema("project-contract", "ac_asset_management"));
        InformationSchema.put("ac_buyback", new InformationSchema("project-contract", "ac_buyback"));
        InformationSchema.put("ac_date_rate", new InformationSchema("project-contract", "ac_date_rate"));
        InformationSchema.put("ac_fixed_rate", new InformationSchema("project-contract", "ac_fixed_rate"));
        InformationSchema.put("ac_fund_info", new InformationSchema("project-contract", "ac_fund_info"));
        InformationSchema.put("ac_invest", new InformationSchema("project-contract", "ac_invest"));
        InformationSchema.put("ac_loan_contract", new InformationSchema("project-contract", "ac_loan_contract"));
        InformationSchema.put("ac_loan_credit_asset_transfers", new InformationSchema("project-contract", "ac_loan_credit_asset_transfers"));
        InformationSchema.put("ac_loan_situation", new InformationSchema("project-contract", "ac_loan_situation"));
        InformationSchema.put("ac_lpr_rate", new InformationSchema("project-contract", "ac_lpr_rate"));
        InformationSchema.put("ac_non_private_funds_contract", new InformationSchema("project-contract", "ac_non_private_funds_contract"));
        InformationSchema.put("ac_non_standardized_debt_asset_contract", new InformationSchema("project-contract", "ac_non_standardized_debt_asset_contract"));
        InformationSchema.put("ac_partner_info", new InformationSchema("project-contract", "ac_partner_info"));
        InformationSchema.put("ac_penalty_rules", new InformationSchema("project-contract", "ac_penalty_rules"));
        InformationSchema.put("ac_profit_contract", new InformationSchema("project-contract", "ac_profit_contract"));
        InformationSchema.put("ac_property_contract", new InformationSchema("project-contract", "ac_property_contract"));
        InformationSchema.put("ac_rate_cost", new InformationSchema("project-contract", "ac_rate_cost"));
        InformationSchema.put("ac_receivable_info", new InformationSchema("project-contract", "ac_receivable_info"));
        InformationSchema.put("ac_repayment_rules", new InformationSchema("project-contract", "ac_repayment_rules"));
        InformationSchema.put("ac_risk", new InformationSchema("project-contract", "ac_risk"));
        InformationSchema.put("ac_rollover_contract", new InformationSchema("project-contract", "ac_rollover_contract"));
        InformationSchema.put("ac_spv_contract", new InformationSchema("project-contract", "ac_spv_contract"));
        InformationSchema.put("ac_sup_abs_asset", new InformationSchema("project-contract", "ac_sup_abs_asset"));
        InformationSchema.put("ac_sup_abs_base", new InformationSchema("project-contract", "ac_sup_abs_base"));
        InformationSchema.put("ac_sup_abs_credit_enhancement", new InformationSchema("project-contract", "ac_sup_abs_credit_enhancement"));
        InformationSchema.put("ac_sup_infrastructure", new InformationSchema("project-contract", "ac_sup_infrastructure"));
        InformationSchema.put("ac_sup_real_estate", new InformationSchema("project-contract", "ac_sup_real_estate"));
        InformationSchema.put("ac_sup_real_estate_licence", new InformationSchema("project-contract", "ac_sup_real_estate_licence"));
        InformationSchema.put("ac_sup_real_estate_schedule", new InformationSchema("project-contract", "ac_sup_real_estate_schedule"));
        InformationSchema.put("ac_target_corporate", new InformationSchema("project-contract", "ac_target_corporate"));
        InformationSchema.put("ac_transfer_info", new InformationSchema("project-contract", "ac_transfer_info"));
        InformationSchema.put("gc_asset_goods_summary", new InformationSchema("project-contract", "gc_asset_goods_summary"));
        InformationSchema.put("gc_contract_summary", new InformationSchema("project-contract", "gc_contract_summary"));
        InformationSchema.put("gc_ensure_contract", new InformationSchema("project-contract", "gc_ensure_contract"));
        InformationSchema.put("gc_ensure_holder_relation", new InformationSchema("project-contract", "gc_ensure_holder_relation"));
        InformationSchema.put("gc_mortgage_assess", new InformationSchema("project-contract", "gc_mortgage_assess"));
        InformationSchema.put("gc_mortgage_contract", new InformationSchema("project-contract", "gc_mortgage_contract"));
        InformationSchema.put("gc_mortgage_goods", new InformationSchema("project-contract", "gc_mortgage_goods"));
        InformationSchema.put("gc_pledge_assess", new InformationSchema("project-contract", "gc_pledge_assess"));
        InformationSchema.put("gc_pledge_contract", new InformationSchema("project-contract", "gc_pledge_contract"));
        InformationSchema.put("gc_pledge_goods", new InformationSchema("project-contract", "gc_pledge_goods"));
        InformationSchema.put("invest_cost_setting", new InformationSchema("project-contract", "invest_cost_setting"));
        InformationSchema.put("pm_apply_contract_text", new InformationSchema("project-contract", "pm_apply_contract_text"));
        InformationSchema.put("pm_apply_contract_text_check", new InformationSchema("project-contract", "pm_apply_contract_text_check"));
        InformationSchema.put("account_bank", new InformationSchema("project_centre", "account_bank"));
        InformationSchema.put("account_base", new InformationSchema("project_centre", "account_base"));
        InformationSchema.put("account_file", new InformationSchema("project_centre", "account_file"));
        InformationSchema.put("account_printing_info", new InformationSchema("project_centre", "account_printing_info"));
        InformationSchema.put("account_receipt_info", new InformationSchema("project_centre", "account_receipt_info"));
        InformationSchema.put("account_visitationer", new InformationSchema("project_centre", "account_visitationer"));
        InformationSchema.put("issuance_quota", new InformationSchema("project_centre", "issuance_quota"));
        InformationSchema.put("official_letter_base", new InformationSchema("project_centre", "official_letter_base"));
        InformationSchema.put("project_account", new InformationSchema("project_centre", "project_account"));
        InformationSchema.put("project_all_element", new InformationSchema("project_centre", "project_all_element"));
        InformationSchema.put("project_auditors", new InformationSchema("project_centre", "project_auditors"));
        InformationSchema.put("project_base_info", new InformationSchema("project_centre", "project_base_info"));
        InformationSchema.put("project_counterparty", new InformationSchema("project_centre", "project_counterparty"));
        InformationSchema.put("project_declare", new InformationSchema("project_centre", "project_declare"));
        InformationSchema.put("project_entrust_info", new InformationSchema("project_centre", "project_entrust_info"));
        InformationSchema.put("project_file", new InformationSchema("project_centre", "project_file"));
        InformationSchema.put("project_finances", new InformationSchema("project_centre", "project_finances"));
        InformationSchema.put("project_initial_registration", new InformationSchema("project_centre", "project_initial_registration"));
        InformationSchema.put("project_job", new InformationSchema("project_centre", "project_job"));
        InformationSchema.put("project_parent", new InformationSchema("project_centre", "project_parent"));
        InformationSchema.put("project_period_report", new InformationSchema("project_centre", "project_period_report"));
        InformationSchema.put("project_period_report_file", new InformationSchema("project_centre", "project_period_report_file"));
        InformationSchema.put("project_period_report_message", new InformationSchema("project_centre", "project_period_report_message"));
        InformationSchema.put("project_publish_specification", new InformationSchema("project_centre", "project_publish_specification"));
        InformationSchema.put("project_publish_supplement", new InformationSchema("project_centre", "project_publish_supplement"));
        InformationSchema.put("project_publish_verify", new InformationSchema("project_centre", "project_publish_verify"));
        InformationSchema.put("project_recommend_place", new InformationSchema("project_centre", "project_recommend_place"));
        InformationSchema.put("project_relation_dealer", new InformationSchema("project_centre", "project_relation_dealer"));
        InformationSchema.put("project_review_opinion", new InformationSchema("project_centre", "project_review_opinion"));
        InformationSchema.put("project_rollover_item_change", new InformationSchema("project_centre", "project_rollover_item_change"));
        InformationSchema.put("project_tot", new InformationSchema("project_centre", "project_tot"));
        InformationSchema.put("project_valuation", new InformationSchema("project_centre", "project_valuation"));
        InformationSchema.put("stakeholder_account", new InformationSchema("tripartite", "stakeholder_account"));
        InformationSchema.put("stakeholder_certificate_information", new InformationSchema("tripartite", "stakeholder_certificate_information"));
        InformationSchema.put("stakeholder_contacts", new InformationSchema("tripartite", "stakeholder_contacts"));
        InformationSchema.put("stakeholder_financial_product_base", new InformationSchema("tripartite", "stakeholder_financial_product_base"));
        InformationSchema.put("stakeholder_invoice", new InformationSchema("tripartite", "stakeholder_invoice"));
        InformationSchema.put("stakeholder_organization_base", new InformationSchema("tripartite", "stakeholder_organization_base"));
        InformationSchema.put("stakeholder_organization_business", new InformationSchema("tripartite", "stakeholder_organization_business"));
        InformationSchema.put("stakeholder_organization_earnings", new InformationSchema("tripartite", "stakeholder_organization_earnings"));
        InformationSchema.put("stakeholder_organization_shareholder_relation", new InformationSchema("tripartite", "stakeholder_organization_shareholder_relation"));
        InformationSchema.put("stakeholder_parties_certificate_relation", new InformationSchema("tripartite", "stakeholder_parties_certificate_relation"));
        InformationSchema.put("stakeholder_person_base", new InformationSchema("tripartite", "stakeholder_person_base"));
        InformationSchema.put("stakeholder_query", new InformationSchema("tripartite", "stakeholder_query"));
        InformationSchema.put("stakeholder_shareholder_base", new InformationSchema("tripartite", "stakeholder_shareholder_base"));
        InformationSchema.put("tripartite_certificate_information", new InformationSchema("tripartite", "tripartite_certificate_information"));
        InformationSchema.put("tripartite_investment_contract", new InformationSchema("tripartite", "tripartite_investment_contract"));
        InformationSchema.put("tripartite_organization_base", new InformationSchema("tripartite", "tripartite_organization_base"));
        InformationSchema.put("tripartite_organization_contract_relation", new InformationSchema("tripartite", "tripartite_organization_contract_relation"));
        InformationSchema.put("tripartite_parties_certificate_relation", new InformationSchema("tripartite", "tripartite_parties_certificate_relation"));


    }

    public static void main(String[] args) {
        Map<String, String> database = new HashMap<>();

        InformationSchema.forEach((key, value) -> {
            System.out.println("drop table " + value.getTableName() + ";");
            database.put(value.getTableSchema(), value.getTableSchema());
        });

        database.forEach((i, e) -> {

            System.out.println("drop database " + i + ";");
        });


    }


    static String operationCreateDll(String s) throws RuntimeException {

        String[] split = s.split("\n");
        StringBuffer stringBuffer = new StringBuffer();


        System.out.println(stringBuffer);
        for (String s1 : split) {
            s1 = s1.replaceAll("\\)", "）");
            s1 = s1.replaceAll("\\(", "（");
            if (s1.contains("int（")) {
                s1 = s1.replaceAll("int（[0-9]+）", "int");

            }
            if (s1.contains("datetime")) {
                s1 = s1.replaceAll("datetime", "TIMESTAMP(3)");

            }


            s1 = s1.replaceAll("）", "\\)");
            s1 = s1.replaceAll("（", "\\(");
            stringBuffer.append(s1);

        }


        String s1 = stringBuffer.toString();
        s1 = s1.replaceAll("COMMENT=", "");
//        s1 = s1.replaceAll("COMMENT", "");
        s1 = s1.replaceAll("CHARSET=utf8mb4", "");
        s1 = s1.replaceAll("DEFAULT NULL", "");
        s1 = s1.replaceAll("DEFAULT", "");
        s1 = s1.replaceAll("BTREE", "");
        s1 = s1.replaceAll("NOT NULL", "");
        s1 = s1.replaceAll("USING", " not enforced");
        s1 = s1.replaceAll("ENGINE=InnoDB", "");
        s1 = s1.replaceAll("AUTO_INCREMENT", "");
        String[] primaries = s1.split("PRIMARY");
        primaries[0] = primaries[0] + "  `DATA_TIME` TIMESTAMP(3),`COLLECT_TIME` TIMESTAMP(3),`partition` TIMESTAMP(3),";
        String s2 = primaries.toString();
        s1 = "";
        for (
                int i = 0;
                i < primaries.length; i++) {
            s1 += primaries[i];
            if (i == 0) {
                s1 += " PRIMARY ";
            }
        }

        int i = s1.indexOf("=");
        int length = s1.length();
        if (i != -1) {
            s1 = s1.substring(0, s1.indexOf("=") - 1);
        }

        return s1;
    }


    @Autowired
    private RestTemplate restTemplate;

    void sendCreateUrl(StringBuffer stringBuffer, String taskName) {
        String url = "http://10.0.25.3:10000/flink/app/create";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", Authorization);
        UUID uuid = UUID.randomUUID();


        MultiValueMap map = new LinkedMultiValueMap();

        map.add("jobType", "2");
        map.add("executionMode", "4");
        map.add("versionId", "100000");
        map.add("flinkSql", stringBuffer.toString());
        map.add("appType", "1");
        map.add("jobName", taskName);
        map.add("options", "{}");
        map.add("resolveOrder", "0");
        map.add("restartSize", "0");
        map.add("socketId", uuid.toString());
        HttpEntity requestBody = new HttpEntity(map, headers);

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, requestBody, HashMap.class);
        HashMap body = responseEntity.getBody();
        System.out.println("body===========" + body);

    }

    static String Authorization = "87855669d8f6d90f2d5a85a7fadf1521f0e9d78bed38f7f0772c489548f871957ee58e65d521049e643108417fd2578235566dbd7fd575363852e6d7dde7597874622d68407c44a9bd9105ec1e299ef328ad86723338f66c9e49d594724c74ba2bcf7dd22847ffd6636a06df9f5140cb4675e0c6ed9a4f7f7767e7fdb67fdf7196acaea5819640b9";

    @Override
    public void tasklist() {
        String url = "http://10.0.25.3:10000/flink/app/list";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", Authorization);
        UUID uuid = UUID.randomUUID();


        MultiValueMap map = new LinkedMultiValueMap();

        map.add("pageSize", "200");
        map.add("pageNum", "1");

        HttpEntity requestBody = new HttpEntity(map, headers);

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, requestBody, HashMap.class);
        HashMap body = responseEntity.getBody();

        HashMap o = (HashMap) body.get("data");
        List<Map<String, String>> records = (List<Map<String, String>>) o.get("records");

        for (Map<String, String> record : records) {

            String id = record.get("id");
            build(id);
//            deleteTask(id);
        }
    }


    void deleteTask(String id) {
        String url = "http://10.0.25.3:10000/flink/app/delete";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", Authorization);
        UUID uuid = UUID.randomUUID();
        MultiValueMap map = new LinkedMultiValueMap();

        map.add("id", id);


        HttpEntity requestBody = new HttpEntity(map, headers);

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, requestBody, HashMap.class);
        HashMap body = responseEntity.getBody();
    }

    void build(String id) {
        String url = "http://10.0.25.3:10000/flink/pipe/build";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", Authorization);
        UUID uuid = UUID.randomUUID();
        MultiValueMap map = new LinkedMultiValueMap();

        map.add("appId", id);


        HttpEntity requestBody = new HttpEntity(map, headers);

        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(url, requestBody, HashMap.class);
        HashMap body = responseEntity.getBody();
    }

    @Override
    public void selectCreateTbale() throws IOException {
        List<InformationSchema> r = new ArrayList<>();
        InformationSchema.forEach((key, value) -> {

            List<InformationSchema> maps = sysFormInstanceMapper.selectCreateInfo(value.getTableSchema(), value.getTableName());

            System.out.println(maps);
            String tipDll = generatetip(maps);
            String kafkaDll = generateKafka(maps);
            String hiveDll = generateHive(maps);
            String kafkaStatementSql = generateKafkaStatement(maps);
            String SelectView = generateSelectView(maps);
            String HiveStatementSql = generateHiveStatement(maps);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("begin statement set; \n");
            stringBuffer.append(tipDll);
            stringBuffer.append(kafkaDll);
            stringBuffer.append(hiveDll);
            stringBuffer.append(kafkaStatementSql);
            stringBuffer.append(SelectView);
            stringBuffer.append(HiveStatementSql);
            stringBuffer.append("end;\n");
//            sendCreateUrl(stringBuffer, value.getTableSchema() + "_" + value.getTableName());

            System.out.println(stringBuffer.toString());
            try {
                txtExport.creatTxtFile("D:/sql_file/" + value.getTableSchema() + "/", value.getTableSchema() + "_" + value.getTableName());
                txtExport.writeTxtFile(stringBuffer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }


        });


//        ExcelUtil.writeToExcel("D://a.xlsx", InformationSchema.class, r);


    }


    private String generateKafkaStatement(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("insert into kafka_" + tableInfos.get(0).getTableName() + " select *, DATE_FORMAT(LOCALTIMESTAMP, 'yyyy-MM-dd HH:mm:ss') as `operation_ts` from tip_" + tableInfos.get(0).getTableName() + ";\n");


        return stringBuilder.toString();
    }

    private String generateHiveStatement(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("insert into hive_" + tableInfos.get(0).getTableName() + " select * from " + tableInfos.get(0).getTableName() + "_v;\n");


        return stringBuilder.toString();
    }

    private String generateSelectView(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("create view " + tableInfos.get(0).getTableName() + "_v AS SELECT ");
        for (int i = 0; i < tableInfos.size(); i++) {

            if (tableInfos.get(i).getDataType().equals("datetime")) {
                stringBuilder.append("DATE_FORMAT(" + tableInfos.get(i).getColumnName() + ",'yyyy-MM-dd HH:mm:ss'),");
            } else if (tableInfos.get(i).getDataType().equals("date")) {
                stringBuilder.append("IFNULL(DATE_FORMAT(CAST(" + tableInfos.get(i).getColumnName() + " AS TIMESTAMP(3)),'yyyy-MM-dd HH:mm:ss'),null) as `" + tableInfos.get(i).getColumnName() + "`,");
            } else {
                stringBuilder.append("`" + tableInfos.get(i).getColumnName() + "`" + ",");
            }
        }

        stringBuilder.append(" operation_ts, ");
        stringBuilder.append(" DATE_FORMAT(TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP), 'yyyy-MM-dd') as `DATA_TIME`, ");
        stringBuilder.append(" DATE_FORMAT(LOCALTIMESTAMP, 'yyyy-MM-dd HH:mm:ss') as `COLLECT_TIME`,");
        stringBuilder.append(" DATE_FORMAT(LOCALTIMESTAMP, 'yyyy-MM-dd') as `partition`");
        stringBuilder.append("from kafka_" + tableInfos.get(0).getTableName() + ";\n");
        return stringBuilder.toString();
    }

    static String hostname = "10.0.23.20";
    static String port = "3306";
    static String username = "root";
    static String password = "123456root";

    private String generatetip(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("CREATE TABLE tip_" + tableInfos.get(0).getTableName() + "(\n");
        for (int i = 0; i < tableInfos.size(); i++) {
            stringBuilder.append("`" + tableInfos.get(i).getColumnName() + "`");
            if (tableInfos.get(i).getDataType().equals("datetime")) {
                stringBuilder.append("\t TIMESTAMP(3)");
            } else if (tableInfos.get(i).getDataType().equals("int")) {
                stringBuilder.append("\t int");
            } else if (tableInfos.get(i).getDataType().equals("bigint")) {
                stringBuilder.append("\t bigint");
            } else if (tableInfos.get(i).getDataType().equals("text")) {
                stringBuilder.append("\t string");
            } else if (tableInfos.get(i).getDataType().equals("double")) {
                stringBuilder.append("\t double");
            } else {
                stringBuilder.append("\t" + tableInfos.get(i).getColumnType());
            }

            if (tableInfos.get(i).getColumnName().equals("ID")) {
                stringBuilder.append("\t PRIMARY KEY NOT ENFORCED");
            }

            if (tableInfos.get(i).getColumnComment() != null && !"".equals(tableInfos.get(i))) {

                stringBuilder.append("\t COMMENT '" + tableInfos.get(i).getColumnComment() + "'");
            }
            if (i < tableInfos.size() - 1) {
                stringBuilder.append(",");
            }

            stringBuilder.append("\n");

        }
        stringBuilder.append(")\n");
        stringBuilder.append("WITH (\n");
        stringBuilder.append("'connector'= 'mysql-cdc',\n");
        stringBuilder.append("'hostname'= '10.0.23.20', -- 需变化\n");
        stringBuilder.append("'port'= '3306', -- 需变化\n");
        stringBuilder.append("'username'= 'root', -- 需变化\n");
        stringBuilder.append("'password'= '123456root', -- 需变化\n");
        stringBuilder.append("'server-time-zone'= 'Asia/Shanghai',\n");
        stringBuilder.append("'debezium.snapshot.mode'='initial',\n");
        stringBuilder.append("'database-name'= '" + tableInfos.get(0).getTableSchema() + "', -- 需变化\n");
        stringBuilder.append("'table-name'= '" + tableInfos.get(0).getTableName() + "' -- 需变化\n");
        stringBuilder.append(");\n");
//        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String generateKafka(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("CREATE TABLE kafka_" + tableInfos.get(0).getTableName() + "(\n");
        for (int i = 0; i < tableInfos.size(); i++) {
            stringBuilder.append("`" + tableInfos.get(i).getColumnName() + "`");
            if (tableInfos.get(i).getDataType().equals("datetime")) {
                stringBuilder.append("\t TIMESTAMP(3)");
            } else if (tableInfos.get(i).getDataType().equals("int")) {
                stringBuilder.append("\t int");
            } else if (tableInfos.get(i).getDataType().equals("text")) {
                stringBuilder.append("\t string");
            } else if (tableInfos.get(i).getDataType().equals("bigint")) {
                stringBuilder.append("\t bigint");
            } else if (tableInfos.get(i).getDataType().equals("double")) {
                stringBuilder.append("\t double");
            } else {
                stringBuilder.append("\t" + tableInfos.get(i).getColumnType());
            }


            if (tableInfos.get(i).getColumnComment() != null && !"".equals(tableInfos.get(i))) {

                stringBuilder.append("\t COMMENT '" + tableInfos.get(i).getColumnComment() + "'");
            }

            stringBuilder.append(",");


            stringBuilder.append("\n");

        }
        stringBuilder.append("`operation_ts` string COMMENT '数据时间' \n");

        stringBuilder.append(")\n");
        stringBuilder.append("WITH (\n");
        stringBuilder.append("'connector' = 'kafka',\n");
        stringBuilder.append(" 'topic' = '" + tableInfos.get(0).getTableSchema() + "_" + tableInfos.get(0).getTableName() + "',  -- 需变化 库名-表名 主题名称\n");
        stringBuilder.append(" 'properties.group.id' = '" + tableInfos.get(0).getTableSchema() + "_" + tableInfos.get(0).getTableName() + "-group',  -- 需变化 数据库-表名-group kafka源的使用者组的标识，kafka接收器是可选的\n");
        stringBuilder.append(" 'properties.bootstrap.servers' = 'hadoop101:9092,hadoop102:9092,hadoop103:9092,hadoop105:9092,hadoop106:9092,hadoop107:9092',\n");
        stringBuilder.append(" 'scan.startup.mode' = 'earliest-offset', -- 对于Kafka使用者的启动模式，有效值为“最早偏移”，“最新偏移”，“组偏移”，“时间戳”和“特定偏移” 从最早的偏移量开始\n");
        stringBuilder.append(" 'debezium-json.ignore-parse-errors' = 'true', -- 忽略解析错误\n");
        stringBuilder.append(" 'format' = 'debezium-json' -- 这里必须是debezium-json，如果是Json那么 source mysql cdc表的数据无法写入到kafka中\n");


        stringBuilder.append(");\n");
//        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String generateHive(List<InformationSchema> tableInfos) {
        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("CREATE TABLE hive_" + tableInfos.get(0).getTableName() + "(\n");
        for (int i = 0; i < tableInfos.size(); i++) {
            stringBuilder.append("`" + tableInfos.get(i).getColumnName() + "`");
            if (tableInfos.get(i).getDataType().equals("datetime")) {
                stringBuilder.append("\t string");
            } else if (tableInfos.get(i).getDataType().equals("date")) {
                stringBuilder.append("\t string");
            } else if (tableInfos.get(i).getDataType().equals("int")) {
                stringBuilder.append("\t int");
            } else if (tableInfos.get(i).getDataType().equals("text")) {
                stringBuilder.append("\t string");
            } else if (tableInfos.get(i).getDataType().equals("bigint")) {
                stringBuilder.append("\t bigint");
            } else if (tableInfos.get(i).getDataType().equals("double")) {
                stringBuilder.append("\t double");
            } else {
                stringBuilder.append("\t" + tableInfos.get(i).getColumnType());
            }


            if (tableInfos.get(i).getColumnComment() != null && !"".equals(tableInfos.get(i))) {

                stringBuilder.append("\t COMMENT '" + tableInfos.get(i).getColumnComment() + "'");
            }

            stringBuilder.append(",");

            stringBuilder.append("\n");

        }
        stringBuilder.append("`operation_ts` string COMMENT '数据时间' ,\n");
        stringBuilder.append("`DATA_TIME` string,\n");
        stringBuilder.append("`COLLECT_TIME` string,\n");
        stringBuilder.append("`partition` string,\n");
        stringBuilder.append("primary key(`ID`) not enforced \n");
        stringBuilder.append(")\n");
        stringBuilder.append("PARTITIONED BY (`partition`)\n");
        stringBuilder.append("WITH (\n");

        stringBuilder.append("'connector'='hudi',");
        stringBuilder.append("'path'= 'hdfs://nameservice1/user/hudi/" + tableInfos.get(0).getTableSchema() + "_" + tableInfos.get(0).getTableName() + "' -- 需变化\n");
        stringBuilder.append(", 'hoodie.datasource.write.recordkey.field'= 'ID'-- 主键  -- 需变化\n");
        stringBuilder.append(", 'write.precombine.field'= 'operation_ts'-- 相同的键值时，取此字段最大值，默认ts字段  -- 需变化\n");
        stringBuilder.append(", 'write.tasks'= '1'\n");
        stringBuilder.append(", 'compaction.tasks'= '1'\n");
        stringBuilder.append(", 'write.rate.limit'= '2000'-- 限速\n");
        stringBuilder.append(", 'table.type'= 'MERGE_ON_READ'-- 默认COPY_ON_WRITE,可选MERGE_ON_READ \n");
        stringBuilder.append(", 'compaction.async.enabled'= 'true'-- 是否开启异步压缩\n");
        stringBuilder.append(", 'compaction.trigger.strategy'= 'num_commits'-- 按次数压缩\n");
        stringBuilder.append(", 'compaction.delta_commits'= '1'-- 默认为5\n");
        stringBuilder.append(", 'changelog.enabled'= 'true'-- 开启changelog变更\n");
        stringBuilder.append(", 'read.streaming.enabled'= 'true'-- 开启流读\n");
        stringBuilder.append(", 'read.streaming.check-interval'= '3'-- 检查间隔，默认60s\n");
        stringBuilder.append(", 'hive_sync.enable'= 'true'-- 开启自动同步hive\n");
        stringBuilder.append(", 'hive_sync.mode'= 'hms'-- 自动同步hive模式，默认jdbc模式\n");
        stringBuilder.append(", 'hive_sync.metastore.uris'= 'thrift://hadoop104:9083'-- hive metastore地址\n");
        stringBuilder.append("-- , 'hive_sync.jdbc_url'= 'jdbc:hive2://hadoop101:10000'-- hiveServer地址\n");
        stringBuilder.append(", 'hive_sync.table'= 'ods_" + tableInfos.get(0).getTableName() + "_s'-- hive 新建表名  - 需变化\n");
        String tableSchema = tableInfos.get(0).getTableSchema();
        tableSchema = tableSchema.replaceAll("-", "_");
        stringBuilder.append(", 'hive_sync.db'= '" + tableSchema + "'-- hive 新建数据库名  - 需变化\n");
        stringBuilder.append(", 'hive_sync.username'= 'root'-- HMS 用户名\n");
        stringBuilder.append(", 'hive_sync.password'= '000000'-- HMS 密码\n");
        stringBuilder.append(", 'hive_sync.support_timestamp'= 'true'-- 兼容hive timestamp类型\n");
        stringBuilder.append(",'hive_sync.skip_ro_suffix' = 'true'\n");


        stringBuilder.append(");\n");
//        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();


    }


}
