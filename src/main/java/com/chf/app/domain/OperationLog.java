package com.chf.app.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

//操作日志
@Entity
@Table(name = "operation_log")
public class OperationLog extends AbstractAuditingEntity {

    @Id
    private String id;

    // 操作方法 post put delete
    @Column(name = "method", nullable = false, length = 31)
    private String method;

    // 接口路径
    @Column(name = "path", length = 63)
    private String path;

    // 操作输入数据
    @Column(name = "input_data", length = 1023)
    private String inputData;

    // 操作输出数据
    @Lob
    @Column(name = "output_data")
    private String outputData;

    public OperationLog() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }

    public String getOutputData() {
        return outputData;
    }

    public void setOutputData(String outputData) {
        this.outputData = outputData;
    }

}
