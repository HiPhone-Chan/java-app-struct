package com.chf.app.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// 这个在bpmn文件里用到
@Service
public class SayHelloDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(SayHelloDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("executed sayHelloDelegate: {}", execution);
    }

}
