package com.chf.app.service;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class BpmEventService {

    private static final Logger log = LoggerFactory.getLogger(BpmEventService.class);

    boolean processApplicationStopped;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @EventListener
    public void onPostDeploy(PostDeployEvent event) {
        log.info("postDeploy: {}", event);
        processApplicationStopped = false;
    }

    @EventListener
    public void onPreUndeploy(PreUndeployEvent event) {
        log.info("preUndeploy: {}", event);
        processApplicationStopped = true;
    }

    public boolean isProcessInstanceFinished(String processInstanceId) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        return historicProcessInstance != null && historicProcessInstance.getEndTime() != null;
    }
}
