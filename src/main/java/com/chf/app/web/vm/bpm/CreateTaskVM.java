package com.chf.app.web.vm.bpm;

import java.util.Map;

public class CreateTaskVM {
    // 流程的 key
    private String key;

    Map<String, Object> params;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
