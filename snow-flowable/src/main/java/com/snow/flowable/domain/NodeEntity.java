package com.snow.flowable.domain;

import com.snow.flowable.NodeTypeEnum;
import org.flowable.bpmn.model.FormProperty;

import java.util.List;

/**
 * @ClassName NodeEntity
 * @Description TODO
 * @Author Karl
 * @Date 2022/5/19 18:22
 * @Version 1.0
 */
public class NodeEntity  implements java.io.Serializable{
    private String id;

    private String name;

    /**
     * 外置表单
     */
    private String formKey;

    /**
     * 内置表单
     */
    private List<FormProperty> formPropertys;

    private NodeTypeEnum nodeTypeEnum;

    public NodeTypeEnum getNodeTypeEnum() {
        return nodeTypeEnum;
    }

    public void setNodeTypeEnum(NodeTypeEnum nodeTypeEnum) {
        this.nodeTypeEnum = nodeTypeEnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public List<FormProperty> getFormPropertys() {
        return formPropertys;
    }

    public void setFormPropertys(List<FormProperty> formPropertys) {
        this.formPropertys = formPropertys;
    }

}
