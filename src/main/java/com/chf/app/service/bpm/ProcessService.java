package com.chf.app.service.bpm;

import java.util.Map;

// 流程处理
public interface ProcessService {

    Map<String, Object> getCreateParam(Map<String, Object> request);

    Map<String, Object> checkCompleteParam(Map<String, Object> request);

}
