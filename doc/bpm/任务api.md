任务api
=======

# 创建任务
```
POST /api/staff/bpm/task
body
# 参数根据具体流程决定
{
  "key": "",
  "params": {
  }
}

resp
{
  "taskId": "任务id"
}
```

# 获取所有任务
```
GET /api/staff/bpm/tasks?分页信息
# 其他参数根据具体流程决定

```

# 获取未完成任务
```
GET /api/staff/bpm/tasks/unfinish?分页信息
# 其他参数根据具体流程决定

```

# 获取任务表单数据
```
GET /api/staff/bpm/task/last?taskId=
# taskId 任务id

```

# 获取上一个任务
```
GET /api/staff/bpm/task/last?taskId=
# taskId 当前任务id
```

# 完成任务
```
POST /api/staff/bpm/task/complete?taskId=
# 参数根据具体流程决定
body
{
  "taskId": "任务id",
  "params": {
  }
}
```

# 删除任务
```
DELETE /api/staff/bpm/task?taskId=
# taskId 任务id
```