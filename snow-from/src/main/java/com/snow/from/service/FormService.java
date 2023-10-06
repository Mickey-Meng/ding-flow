package com.snow.from.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.snow.flowable.domain.response.ProcessDefinitionResponse;
import com.snow.flowable.service.FlowableService;
import com.snow.system.domain.ActDeModel;
import com.snow.system.service.IActDeModelService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.flowable.idm.api.User;
import org.flowable.ui.common.security.SecurityUtils;
import org.flowable.ui.common.service.exception.BadRequestException;
import org.flowable.ui.modeler.domain.Model;
import org.flowable.ui.modeler.model.ModelKeyRepresentation;
import org.flowable.ui.modeler.model.ModelRepresentation;
import org.flowable.ui.modeler.serviceapi.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/12/3 14:39
 */
@Service("form")
public class FormService {
    @Autowired
    private IActDeModelService actDeModelService;

    @Autowired
    private FlowableService flowableService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelService modelService;

    @SneakyThrows
    public List<ProcessDefinitionResponse> getFlow(String key) {

        User currentUser = SecurityUtils.getCurrentUserObject();
        ActDeModel actDeModel = new ActDeModel();
        actDeModel.setModelTypeList(Lists.newArrayList(ActDeModel.MODEL_TYPE_BPMN, ActDeModel.MODEL_TYPE_DECISION_TABLE));
        List<ActDeModel> list = actDeModelService.selectActDeModelList(actDeModel);
        List<ProcessDefinitionResponse> processDefByKey = new ArrayList<>();
        for (ActDeModel deModel : list) {
            Model model = modelService.getModel(deModel.getId());
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(model.getModelEditorJson());
            JsonNode jsonNode = editorJsonNode.get("childShapes");
            Map<String, String> taskInfo = new HashMap<>();

            for (JsonNode node : jsonNode) {

                ObjectNode objectNode = (ObjectNode) node;
                ObjectNode properties = (ObjectNode) objectNode.get("properties");
                if (properties==null) {
                    continue;
                }
                JsonNode resourceId = objectNode.get("stencil").get("id");
                JsonNode jsonNode1 = objectNode.get("resourceId");


                String s = resourceId.toString();
                if (s.contains("StartNoneEvent")) {
                    try {
                        taskInfo.put(jsonNode1.toString(), properties.get("name").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                if (s.contains("UserTask")) {
                    taskInfo.put(jsonNode1.toString(), properties.get("name").toString());
                }
                if (s.contains("EndNoneEvent")) {

                }


//
//                model = modelService.saveModel(model.getId(), model.getName(), model.getKey(), model.getDescription(), editorJsonNode.toString(), true,
//                        null, SecurityUtils.getCurrentUserObject());

            }
            ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse();
            processDefinitionResponse.setId(deModel.getId());
            processDefinitionResponse.setKey(deModel.getModelKey());
            processDefinitionResponse.setName(deModel.getName());
            String s = JSON.toJSONString(taskInfo);
            processDefinitionResponse.setTaskInfo(s);

            processDefByKey.add(processDefinitionResponse);
        }
//        List<ProcessDefinitionResponse> processDefByKey = flowableService.getProcessDefByKey(key);


        return processDefByKey;
    }

    protected ModelRepresentation updateModel(Model model, MultiValueMap<String, String> values, boolean forceNewVersion) {

        String name = values.getFirst("name");
        String key = values.getFirst("key").replaceAll(" ", "");
        String description = values.getFirst("description");
        String isNewVersionString = values.getFirst("newversion");
        String newVersionComment = null;

        ModelKeyRepresentation modelKeyInfo = modelService.validateModelKey(model, model.getModelType(), key);
        if (modelKeyInfo.isKeyAlreadyExists()) {
            throw new BadRequestException("Model with provided key already exists " + key);
        }

        boolean newVersion = false;
        if (forceNewVersion) {
            newVersion = true;
            newVersionComment = values.getFirst("comment");
        } else {
            if (isNewVersionString != null) {
                newVersion = "true".equals(isNewVersionString);
                newVersionComment = values.getFirst("comment");
            }
        }

        String json = values.getFirst("json_xml");

        try {
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(json);

            ObjectNode propertiesNode = (ObjectNode) editorJsonNode.get("properties");
            String processId = key;
            propertiesNode.put("process_id", processId);
            propertiesNode.put("name", name);
            if (StringUtils.isNotEmpty(description)) {
                propertiesNode.put("documentation", description);
            }
            editorJsonNode.set("properties", propertiesNode);
            model = modelService.saveModel(model.getId(), name, key, description, editorJsonNode.toString(), newVersion,
                    newVersionComment, SecurityUtils.getCurrentUserObject());
            return new ModelRepresentation(model);

        } catch (Exception e) {

            throw new BadRequestException("Process model could not be saved " + model.getId());
        }
    }
}
