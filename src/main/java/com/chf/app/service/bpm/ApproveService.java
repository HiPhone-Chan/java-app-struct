package com.chf.app.service.bpm;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

// 审批业务

@Service
public class ApproveService {

    private static final Logger log = LoggerFactory.getLogger(ApproveService.class);

    @Autowired
    private RepositoryService repositoryService;

    @EventListener
    public void notify(final PostDeployEvent unused) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Approve").latestVersion().singleResult();
        log.info("Found deployed Approve: {}", processDefinition);
        Assert.notNull(processDefinition, "process 'Approve' should be deployed!");
    }

}
