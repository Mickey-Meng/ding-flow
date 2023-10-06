package com.snow.flowable;

public enum NodeTypeEnum {
    StartEvent("开始节点", "start_event"), UserTask("用户任务", "user_task"), EndEvent("结束节点", "end_event");

    private String name;

    private String type;

    NodeTypeEnum(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
