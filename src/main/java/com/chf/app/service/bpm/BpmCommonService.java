package com.chf.app.service.bpm;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.exception.ServiceException;
import com.chf.app.security.SecurityUtils;

@Service
public class BpmCommonService {

//    private static final Logger log = LoggerFactory.getLogger(BpmEventService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    public boolean isProcessInstanceFinished(String processInstanceId) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return historicProcessInstance != null && historicProcessInstance.getEndTime() != null;
    }

    public ProcessService getProcessService(String key) {
        String serviceName = StringUtils.uncapitalize(key) + "Service";
        ProcessService processService = applicationContext.getBean(serviceName, ProcessService.class);
        return processService;
    }

    public Task getCurrentTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Task not be found");
        }
        String assignee = task.getAssignee();
        String login = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        if (!login.equals(assignee)) {
            throw new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Task not be found");
        }
        return task;
    }

    public String getProcessDefinitionKey(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        if (task == null) {
            throw new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Task not be found");
        }
        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        return processDefinition.getKey();
    }

    public Map<?, ?> convertTask(Task task) {
        Map<String, String> m = new HashMap<>();
        m.put("id", task.getId());
        m.put("assignee", task.getAssignee());
        m.put("name", task.getName());
        m.put("owner", task.getOwner());
        m.put("taskKey", task.getTaskDefinitionKey());
        String processDefinitionId = task.getProcessDefinitionId();
        String key = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
                .singleResult().getKey();
        m.put("processKey", key);
        return m;
    }
}
