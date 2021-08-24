package com.chf.app.web.rest;

import static com.chf.app.constants.AuthoritiesConstants.MANAGER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.exception.ServiceException;
import com.chf.app.security.SecurityUtils;

@RestController
@RequestMapping("/api/bpm")
public class BpmTaskResource {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Secured(MANAGER)
    @PostMapping("/task")
    public Map<?, ?> createTask(@RequestBody Map<String, String> body) {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        String key = body.get("key");

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", assignee);
        String processInstanceId = runtimeService.startProcessInstanceByKey(key, variables).getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return this.convertTask(task);
    }

    @GetMapping("/tasks")
    @Secured(MANAGER)
    public List<?> getTasks(@RequestParam Map<String, String> param) {
        // TODO 检查taskId
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtils.isNotEmpty(assignee)) {
            taskQuery = taskQuery.taskAssignee(assignee);
        }
        return taskQuery.listPage(0, 100).stream().map(this::convertTask).collect(Collectors.toList());
    }

    @GetMapping("/task/form/variables")
    @Secured(MANAGER)
    public Map<String, Object> getTaskFormVariables(@RequestParam String taskId) {
        // TODO 检查taskId
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return formService.getTaskFormVariables(taskId);
    }

    @DeleteMapping("/task")
    @Secured(MANAGER)
    public void deleteTask(@RequestParam String taskId) {
        // TODO 检查taskId
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        taskService.deleteTask(taskId);
    }

    @PostMapping("/task/complete")
    @Secured(MANAGER)
    public void completeTask(@RequestBody Map<String, String> body) {
        String taskId = body.get("taskId");
        String approveUser = body.get("approveUser");
        String isApproved = body.get("isApproved");
        String confirm = body.get("confirm");

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", approveUser);
        variables.put("isApproved", Boolean.valueOf(isApproved));
        variables.put("confirm", Boolean.valueOf(confirm));
        taskService.complete(taskId, variables);
    }

    @GetMapping("/task/count")
    @Secured(MANAGER)
    public long countTask() {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return taskService.createTaskQuery().taskAssignee(assignee).count();
    }

    @GetMapping("/tasks/unfinish")
    @Secured(MANAGER)
    public List<?> getHistory() {
        String login = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return historyService.createHistoricProcessInstanceQuery().startedBy(login).unfinished().list();
    }

    // 当前task的上一个task
    @GetMapping("/tasks/last")
    @Secured(MANAGER)
    public Map<?, ?> getLastHistory(@RequestParam String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        HistoricTaskInstance lastHisttory = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionId(processDefinitionId).orderByHistoricTaskInstanceEndTime().desc().listPage(0, 1)
                .get(0);
        Task lastTask = taskService.createTaskQuery().taskId(lastHisttory.getId()).singleResult();
        return convertTask(lastTask);
    }

    private Map<?, ?> convertTask(Task task) {
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
