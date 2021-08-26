package com.chf.app.web.vm;

import java.util.Optional;

import com.chf.app.domain.OperationLog;
import com.chf.app.utils.JsonUtil;

public class OperationLogVM {

    private String id;
    private String createdBy;

    private String method;

    private String path;

    private Object inputData;

    private Object outputData;

    private long createdDate;

    public OperationLogVM() {
    }

    public OperationLogVM(OperationLog operationLog) {
        super();
        this.id = operationLog.getId();
        this.createdBy = operationLog.getCreatedBy();
        this.method = operationLog.getMethod();
        this.path = operationLog.getPath();
        this.inputData = JsonUtil.readValue(operationLog.getInputData());
        this.outputData = JsonUtil.readValue(operationLog.getOutputData());
        this.createdDate = Optional.ofNullable(operationLog.getCreatedDate()).map(date -> date.toEpochMilli())
                .orElse(0l);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Object getInputData() {
        return inputData;
    }

    public void setInputData(Object inputData) {
        this.inputData = inputData;
    }

    public Object getOutputData() {
        return outputData;
    }

    public void setOutputData(Object outputData) {
        this.outputData = outputData;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

}
