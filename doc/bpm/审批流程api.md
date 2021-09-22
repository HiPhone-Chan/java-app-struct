审批api
========

# 创建任务
```
req body
{
  "key": "Approve",
  "params": {
  }
}
```

# 完成任务
```
POST /api/staff/bpm/task/complete?taskId=
body
{
  "taskId": "任务id",
  "params": {
    "approveUser": "审批人",
    "isApproved": "是否批准", # true/false
  }
}
```