API定义
=======

  使用rest接口，请求和响应如无特殊说明body使用json格式

# 请求

  /api 如无特殊说明请求时请带上token,  /resource 则不用
```
  放在header位置
  Authorization : Bearer {token} # 注意Bearer后的空格
```

## 增 POST
## 删 DELETE
## 改 PUT
## 查 GET
### 分页查询
```
GET   http://domain:port?page=?&size=?&sort=??,asc&sort=??,desc
page : 第几页，默认0
size : 每页个数，默认20
sort : 需要排序的字段，默认asc

return 200
body
[{
  # 数据内容
},...]
header
x-total-count: 总数
link: 链接相关内容
```


# 响应

## 成功

  返回2xx

## 失败

  返回4xx，并附带body

```
{
    "code": "错误码",
    "description" : “错误信息”
}
```