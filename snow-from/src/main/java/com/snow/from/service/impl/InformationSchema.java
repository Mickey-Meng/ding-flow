package com.snow.from.service.impl;

import lombok.Data;

/**
 * @ClassName InformationSchema
 * @Description TODO
 * @Author Karl
 * @Date 2022/6/15 10:28
 * @Version 1.0
 */
@Data
public class InformationSchema {

    private String tableSchema;
    private String tableName;
    private String columnName;
    private String dataType;
    private String columnType;
    private String columnKey;
    private String columnComment;


    public InformationSchema(String tableSchema, String tableName) {
        this.tableSchema = tableSchema;
        this.tableName = tableName;
    }
}
