package com.chf.app.service.bpm;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.exception.ServiceException;
import com.chf.app.security.SecurityUtils;

// 审批业务

@Service
public class ApproveService implements ProcessService {

    private static final Logger log = LoggerFactory.getLogger(ApproveService.class);

    @Autowired
    private RepositoryService repositoryService;

    @EventListener
    public void notify(final PostDeployEvent unused) {
        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(BpmKey.APPROVE).latestVersion().singleResult();
        log.info("Found deployed {}: {}", processDefinition.getKey(), processDefinition);
        Assert.notNull(processDefinition, "process 'Approve' should be deployed!");
    }

    @Override
    public Map<String, Object> getCreateParam(Map<String, Object> request) {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", assignee);
        return variables;
    }

    // 自己审批自己则为确认提交
    @Override
    public Map<String, Object> checkCompleteParam(Map<String, Object> request) {
        String approveUser = (String) request.get("approveUser");
        String isApproved = (String) request.get("isApproved");

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", approveUser);
        variables.put("isApproved", Boolean.valueOf(isApproved));
        return variables;
    }

}
