package com.chf.app.web.vm.bpm;

import java.util.Map;

// 任务操作
public class OprTaskVM {
    // 任务id
    private String taskId;

    Map<String, Object> params;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
