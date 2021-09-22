package com.chf.app.web.rest.bpm;

import static com.chf.app.constants.AuthoritiesConstants.ADMIN;

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
public class BpmAdminTaskResource {

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

    @Secured(ADMIN)
    @PostMapping("/task")
    public Map<?, ?> createTask(String key, @RequestParam(name = "assignee", required = false) String assignee) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approveUser", assignee);
        String processInstanceId = runtimeService.startProcessInstanceByKey(key, variables).getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return bpmEventService.convertTask(task);
    }

    @GetMapping("/tasks")
    @Secured(ADMIN)
    public List<?> getTasks(@RequestParam(name = "assignee", required = false) String assignee) {
        TaskQuery taskQuery = taskService.createTaskQuery();
        if (StringUtils.isNotEmpty(assignee)) {
            taskQuery = taskQuery.taskAssignee(assignee);
        }
        return taskQuery.listPage(0, 100).stream().map(bpmEventService::convertTask).collect(Collectors.toList());
    }

    @GetMapping("/task/form/variables")
    @Secured(ADMIN)
    public Map<String, Object> getTaskFormVariables(@RequestParam String taskId) {
        return formService.getTaskFormVariables(taskId);
    }

    @DeleteMapping("/task")
    @Secured(ADMIN)
    public void deleteTask(@RequestParam String taskId) {
        taskService.deleteTask(taskId);
    }

    @PostMapping("/task/complete")
    @Secured(ADMIN)
    public void completeTask(@RequestBody Map<String, Object> body) {
        String taskId = (String) body.get("taskId");
        taskService.complete(taskId, body);
    }

    @GetMapping("/task/count")
    @Secured(ADMIN)
    public long countTask() {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return taskService.createTaskQuery().taskAssignee(assignee).count();
    }

    @GetMapping("/tasks/unfinish")
    @Secured(ADMIN)
    public List<?> getHistory() {
        String login = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        return historyService.createHistoricProcessInstanceQuery().startedBy(login).unfinished().list();
    }

    // 当前task的上一个task
    @GetMapping("/tasks/last")
    @Secured(ADMIN)
    public Map<?, ?> getLastHistory(@RequestParam String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        HistoricTaskInstance lastHisttory = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionId(processDefinitionId).orderByHistoricTaskInstanceEndTime().desc().listPage(0, 1)
                .get(0);
        Task lastTask = taskService.createTaskQuery().taskId(lastHisttory.getId()).singleResult();
        return bpmEventService.convertTask(lastTask);
    }
}
