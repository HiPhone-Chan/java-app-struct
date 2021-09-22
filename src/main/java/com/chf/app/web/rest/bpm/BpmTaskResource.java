package com.chf.app.web.rest.bpm;

import static com.chf.app.constants.AuthoritiesConstants.STAFF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
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
import com.chf.app.service.bpm.BpmEventService;

@RestController
@RequestMapping("/api/admin/bpm")
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
    private BpmEventService bpmEventService;

    @Secured(STAFF)
    @PostMapping("/task")
    public Map<?, ?> createTask(@RequestBody Map<String, String> body) {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        String key = body.get("key");

        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", assignee);
        String processInstanceId = runtimeService.startProcessInstanceByKey(key, variables).getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return bpmEventService.convertTask(task);
    }

    @GetMapping("/tasks")
    @Secured(STAFF)
    public List<?> getTasks(@RequestParam Map<String, String> param) {
        // TODO 检查taskId
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtils.isNotEmpty(assignee)) {
            taskQuery = taskQuery.taskAssignee(assignee);
        }
        return taskQuery.listPage(0, 100).stream().map(bpmEventService::convertTask).collect(Collectors.toList());
    }

    @GetMapping("/task/form/variables")
    @Secured(STAFF)
    public Map<String, Object> getTaskFormVariables(@RequestParam String taskId) {
        // TODO 检查taskId
        return formService.getTaskFormVariables(taskId);
    }

    @DeleteMapping("/task")
    @Secured(STAFF)
    public void deleteTask(@RequestParam String taskId) {
        // 检查taskId
        getTask(taskId);
        taskService.deleteTask(taskId);
    }

    @PostMapping("/task/complete")
    @Secured(STAFF)
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
    @Secured(STAFF)
    public long countTask() {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return taskService.createTaskQuery().taskAssignee(assignee).count();
    }

    @GetMapping("/tasks/unfinish")
    @Secured(STAFF)
    public List<?> getHistory() {
        String login = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return historyService.createHistoricProcessInstanceQuery().startedBy(login).unfinished().list();
    }

    // 当前task的上一个task
    @GetMapping("/tasks/last")
    @Secured(STAFF)
    public Map<?, ?> getLastHistory(@RequestParam String taskId) {
        Task task = getTask(taskId);
        String processDefinitionId = task.getProcessDefinitionId();
        HistoricTaskInstance lastHisttory = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionId(processDefinitionId).orderByHistoricTaskInstanceEndTime().desc().listPage(0, 1)
                .get(0);
        Task lastTask = taskService.createTaskQuery().taskId(lastHisttory.getId()).singleResult();
        return bpmEventService.convertTask(lastTask);
    }

    private Task getTask(String taskId) {

        Task task = getTask(taskId);
        if (task != null) {
            String assignee = task.getAssignee();
            String login = SecurityUtils.getCurrentUserLogin().orElseThrow(
                    () -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
            if (login.equals(assignee)) {
                throw new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Task not be found");
            }
        }
        return task;
    }

}
