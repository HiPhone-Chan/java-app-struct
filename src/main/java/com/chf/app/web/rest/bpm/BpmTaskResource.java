package com.chf.app.web.rest.bpm;

import static com.chf.app.constants.AuthoritiesConstants.STAFF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.chf.app.constants.ErrorCodeContants;
import com.chf.app.exception.ServiceException;
import com.chf.app.security.SecurityUtils;
import com.chf.app.service.bpm.BpmCommonService;
import com.chf.app.service.bpm.ProcessService;
import com.chf.app.web.util.PaginationUtil;
import com.chf.app.web.vm.bpm.CreateTaskVM;
import com.chf.app.web.vm.bpm.OprTaskVM;

@RestController
@RequestMapping("/api/staff/bpm")
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
    private BpmCommonService bpmCommonService;

    @Secured(STAFF)
    @PostMapping("/task")
    public Map<?, ?> createTask(@RequestBody CreateTaskVM createTaskVM) {
        String key = createTaskVM.getKey();
        ProcessService processService = bpmCommonService.getProcessService(key);
        Map<String, Object> variables = processService.getCreateParam(createTaskVM.getParams());

        String processInstanceId = runtimeService.startProcessInstanceByKey(key, variables).getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        return bpmCommonService.convertTask(task);
    }

    @GetMapping("/tasks")
    @Secured(STAFF)
    public ResponseEntity<?> getTasks(Pageable pageable) {
        String assignee = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));
        TaskQuery taskQuery = taskService.createTaskQuery();
        taskQuery = taskQuery.taskAssignee(assignee);

        long count = taskQuery.count();
        List<?> list = taskQuery.listPage(pageable.getPageNumber(), pageable.getPageSize()).stream()
                .map(bpmCommonService::convertTask).collect(Collectors.toList());
        return genResponseEntity(pageable, count, list);
    }

    @GetMapping("/tasks/unfinish")
    @Secured(STAFF)
    public ResponseEntity<List<HistoricProcessInstance>> getHistory(Pageable pageable) {
        String login = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ServiceException(ErrorCodeContants.LACK_OF_DATA, "Login could not be found"));

        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery().startedBy(login)
                .unfinished();
        List<HistoricProcessInstance> result = query.listPage(pageable.getPageNumber(), pageable.getPageSize());
        long count = query.count();
        return genResponseEntity(pageable, count, result);
    }

    // 当前task的上一个task
    @GetMapping("/task/last")
    @Secured(STAFF)
    public Map<?, ?> getLastHistory(@RequestParam String taskId) {
        Task task = bpmCommonService.getCurrentTask(taskId);
        String processDefinitionId = task.getProcessDefinitionId();
        HistoricTaskInstance lastHisttory = historyService.createHistoricTaskInstanceQuery()
                .processDefinitionId(processDefinitionId).orderByHistoricTaskInstanceEndTime().desc().listPage(0, 1)
                .get(0);
        Task lastTask = taskService.createTaskQuery().taskId(lastHisttory.getId()).singleResult();
        return bpmCommonService.convertTask(lastTask);
    }

    @GetMapping("/task/form/variables")
    @Secured(STAFF)
    public Map<String, Object> getTaskFormVariables(@RequestParam String taskId) {
        bpmCommonService.getCurrentTask(taskId);
        return formService.getTaskFormVariables(taskId);
    }

    @PostMapping("/task/complete")
    @Secured(STAFF)
    public void completeTask(@RequestBody OprTaskVM oprTaskVM) {
        String taskId = oprTaskVM.getTaskId();
        Task task = bpmCommonService.getCurrentTask(taskId);
        String key = bpmCommonService.getProcessDefinitionKey(task.getId());
        ProcessService processService = bpmCommonService.getProcessService(key);
        Map<String, Object> variables = processService.checkCompleteParam(oprTaskVM.getParams());
        taskService.complete(taskId, variables);
    }

    @DeleteMapping("/task")
    @Secured(STAFF)
    public void deleteTask(@RequestParam String taskId) {
        // 检查taskId
        bpmCommonService.getCurrentTask(taskId);
        taskService.deleteTask(taskId);
    }

    private <T> ResponseEntity<List<T>> genResponseEntity(Pageable pageable, long total, List<T> result) {
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
                ServletUriComponentsBuilder.fromCurrentRequest(), new PageImpl<>(new ArrayList<>(), pageable, total));
        return ResponseEntity.ok().headers(headers).body(result);
    }

}
