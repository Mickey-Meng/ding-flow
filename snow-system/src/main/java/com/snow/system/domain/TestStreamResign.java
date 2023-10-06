package com.snow.system.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.snow.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * 工作流测试对象 test_stream_resign
 * 
 * @author Agee
 * @date 2022-11-30
 */
@Data
public class TestStreamResign  extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** null */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 离职标题 */
    @Excel(name = "离职标题")
    private String name;

    /** 离职理由 */
    @Excel(name = "离职理由")
    private String reason;

    /** 审批人 */
    @Excel(name = "审批人")
    private String transitionPerson;

    /** 离职时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "离职时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resignTime;

    /** 流程状态（0--待发起，1-审批中，2--审批通过，3--已驳回，4--作废） */
    @Excel(name = "流程状态", readConverterExp = "0=--待发起，1-审批中，2--审批通过，3--已驳回，4--作废")
    private Long processStatus;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applyPerson;

    /** 删除标识（0--正常，1--删除） */
    @Excel(name = "删除标识", readConverterExp = "0=--正常，1--删除")
    private Long isDelete;

    /** 附件 */
    @Excel(name = "附件")
    private String fileUrl;



}
