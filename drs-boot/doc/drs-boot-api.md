#接口文档

#### Dapp下载
##### 接口地址：/dapp/download
请求方式：GET
##### 请求参数

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| filePath       | `String`        | 64       | Y    | dapp文件地址路径(支持网络和本地路径)

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据 Dapp

##### Dapp data
| 属性          | 类型          | 最大长度 | 必填 | 说明                           |
| ------------- | ------------- | -------- | ---- | ------------------------------ |
| name          | `String`      |          | Y    | Dapp name
| version       | `String`      |          | Y    | Dapp version
| contextPath   | `String`      |          | N    | web context path
| status        | `String`      |          | Y    | DOWNLOAD、INITIALIZED、RUNNING、STOPPED
| runError      | `String`      |          | N    | 运行错误信息
| fileName      | `String`      |          | Y    | 文件名
| icon          | `String`      |          | N    | 图标地址
| author        | `String`      |          | N    | 发布人
| remark        | `String`      |          | N    | 备注


#### Dapp初始化
##### 接口地址：/dapp/init/{appName}
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据


#### Dapp安装
##### 接口地址：/dapp/install/{appName}
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据



#### Dapp卸载/停止
##### 接口地址：/dapp/uninstall/{appName}
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据



#### Dapp 配置信息查询
##### 接口地址：/dapp/config/query/{appName}
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据


#### Dapp 配置
##### 接口地址：/dapp/config/{appName}
请求方式：POST
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name
| {}             | `Json String`   |          | Y    | Dapp 配置参数，key-value的 json对象

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据

#### Dapp Store列表查询
##### 接口地址：/dapp/queryAppStore
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<AppProfileVO>

###### 返回参数样例:
```
[
  {

    "showName":"测试App1",
    "name":"dapp-sample",
    "icon":"appstore/img/icon1.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"http://www.appstore.cn",
    "status":"STOPPED"
  },
  {

    "showName":"测试App2",
    "name":"dapp-sample",
    "icon":"appstore/img/icon2.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"http://www.appstore.cn",
    "status":"DOWNLOAD"
  }
]
```

#### Dapp 已下载列表查询
##### 接口地址：/dapp/installList
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |

##### 返回值：

| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<AppProfileVO>

###### 返回参数样例:
```
[
  {

    "showName":"测试App1",
    "name":"dapp-sample",
    "icon":"appstore/img/icon1.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"",
    "status":""
  },
  {

    "showName":"测试App2",
    "name":"dapp-sample",
    "icon":"appstore/img/icon2.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"",
    "status":"DOWNLOAD"
  }
]
```

#### Policy 信息查询
##### 接口地址：/drs/queryAllPolicy
请求方式：GET
##### 参数列表

无

###### 请求参数样例:
```
无
```
##### 返回值：
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<Policy>
#####RsDomain
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| policyId       | `String`        | 32       | Y    | id
| policyName     | `String`        | 24       | Y    | name
###### 返回参数样例:
```
[
    {"policyId":"policyId-0","policyName":"policy name-0"},
    {"policyId":"policyId-1","policyName":"policy name-1"},
    {"policyId":"policyId-2","policyName":"policy name-2"}
]
```
#### Permission 信息查询
##### 接口地址：/drs/queryPermissionList
请求方式：GET
##### 参数列表

无

###### 请求参数样例:
```
无
```
##### 返回值：
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<Policy>
#####RsDomain
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| permissionName | `String`        | 32       | Y    | 权限名称
###### 返回参数样例:
```
[
    {"permissionName":"name-0"},
    {"permissionName":"name-1"},
    {"permissionName":"name-2"}
]
```

#### Domain 信息查询
##### 接口地址：/drs/queryAllDomain
请求方式：GET
##### 参数列表

无

###### 请求参数样例:
```
无
```
##### 返回值：
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<RsDomain>
#####RsDomain
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| domainId       | `String`        | 64       | Y    | 返回码 '000000'表示成功
| desc           | `String`        | 1024     | N    | 描述
###### 返回参数样例:
```
[
    {"desc":"test desc","domainId":"domainId-0"},
    {"desc":"test desc","domainId":"domainId-1"},
    {"desc":"test desc","domainId":"domainId-2"}
]
```
#### BD(Business Define) 查询
##### 接口地址：/drs/bd/query
请求方式：GET
##### 参数列表

|     属性     | 类型     | 最大长度 | 必填 | 说明                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| bdCode       | `String` | 32       | N    | 要查询的BD code,允许为空
###### 请求参数样例:
```
1.无参：http://domain/drs/bd/query
2.有参：http://domain/drs/bd/query?bdCode=SysBD
```
##### 返回值：
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | 返回码 '000000'表示成功
| msg            | `String`        | 64       | Y    | 消息信息
| data           | `Object`        |          | N    | 返回数据，List<BusinessDefine>
#####BusinessDefine
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 64       | Y    | bd code
| name           | `String`        | 64       | Y    | bd 名称
| bdType         | `String`        | 32       | Y    | 类型
| desc           | `String`        | 1024     | N    | 描述
| initPermission | `String`        | 64       | Y    | 初始权限
| initPolicy     | `String`        | 32       | Y    | 初始policy
| functions      | `List<FunctionDefine>`     |      | Y    | 拥有的function
| bdVersion      | `String`        |  4       | Y    | 版本号
######FunctionDefine
| 属性            | 类型            | 最大长度 | 必填 | 说明                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| name           | `String`        | 64       | Y    | bd 名称
| type           | `String`        | 32       | Y    | 类型
| desc           | `String`        | 256      | N    | 描述
| methodSign     | `String`        | 256      | Y    | 方法
| execPermission | `String`        | 64       | Y    | 执行权限
| execPolicy     | `String`        | 32       | Y    | 执行policy
###### 返回参数样例:
```
{
    "code":"000000",
    "data":[
        {
            "bdCode":"SysBD",
            "bdType":"system",
            "bdVersion":"1.0",
            "functionName":"BD_PUBLISH",
            "initPermission":"SLAVE",
            "initPolicy":"INIT_BD",
            "functions":[
                {
                    "desc":"Identity设置",
                    "execPermission":"RS",
                    "execPolicy":"IDENTITY_SETTING",
                    "methodSign":"IDENTITY_SETTING",
                    "name":"IDENTITY_SETTING",
                    "type":"SyetemAction"
                },
                {
                    "desc":"BD发布",
                    "execPermission":"RS",
                    "execPolicy":"BD_PUBLISH",
                    "methodSign":"BD_PUBLISH",
                    "name":"BD_PUBLISH",
                    "type":"SyetemAction"
                },
                {
                    "desc":"Permission注册",
                    "execPermission":"RS",
                    "execPolicy":"PERMISSION_REGISTER",
                    "methodSign":"PERMISSION_REGISTER",
                    "name":"PERMISSION_REGISTER",
                    "type":"SyetemAction"
                },
                {
                    "desc":"Permission授权",
                    "execPermission":"RS",
                    "execPolicy":"AUTHORIZE_PERMISSION",
                    "methodSign":"AUTHORIZE_PERMISSION",
                    "name":"AUTHORIZE_PERMISSION",
                    "type":"SyetemAction"
                },
                {
                    "desc":"Permission撤销授权",
                    "execPermission":"RS",
                    "execPolicy":"CANCEL_PERMISSION",
                    "methodSign":"CANCEL_PERMISSION",
                    "name":"CANCEL_PERMISSION",
                    "type":"SyetemAction"
                },
                {
                    "desc":"注册policy",
                    "execPermission":"RS",
                    "execPolicy":"REGISTER_POLICY",
                    "methodSign":"REGISTER_POLICY",
                    "name":"REGISTER_POLICY",
                    "type":"SyetemAction"
                },
                {
                    "desc":"修改policy",
                    "execPermission":"RS",
                    "execPolicy":"MODIFY_POLICY",
                    "methodSign":"MODIFY_POLICY",
                    "name":"MODIFY_POLICY",
                    "type":"SyetemAction"
                },
                {
                    "desc":"注册RS",
                    "execPermission":"RS",
                    "execPolicy":"REGISTER_RS",
                    "methodSign":"REGISTER_RS",
                    "name":"REGISTER_RS",
                    "type":"SyetemAction"
                },
                {
                    "desc":"撤销RS",
                    "execPermission":"RS",
                    "execPolicy":"CANCEL_RS",
                    "methodSign":"CANCEL_RS",
                    "name":"CANCEL_RS",
                    "type":"SyetemAction"
                },
                {
                    "desc":"CA认证",
                    "execPermission":"RS",
                    "execPolicy":"CA_AUTH",
                    "methodSign":"CA_AUTH",
                    "name":"CA_AUTH",
                    "type":"SyetemAction"
                },
                {
                    "desc":"CA撤销",
                    "execPermission":"RS",
                    "execPolicy":"CA_CANCEL",
                    "methodSign":"CA_CANCEL",
                    "name":"CA_CANCEL",
                    "type":"SyetemAction"
                },
                {
                    "desc":"CA更新",
                    "execPermission":"RS",
                    "execPolicy":"CA_UPDATE",
                    "methodSign":"CA_UPDATE",
                    "name":"CA_UPDATE",
                    "type":"SyetemAction"
                },
                {
                    "desc":"节点加入",
                    "execPermission":"RS",
                    "execPolicy":"NODE_JOIN",
                    "methodSign":"NODE_JOIN",
                    "name":"NODE_JOIN",
                    "type":"SyetemAction"
                },
                {
                    "desc":"节点退出",
                    "execPermission":"RS",
                    "execPolicy":"NODE_LEAVE",
                    "methodSign":"NODE_LEAVE",
                    "name":"NODE_LEAVE",
                    "type":"SyetemAction"
                },
                {
                    "desc":"系统属性配置",
                    "execPermission":"RS",
                    "execPolicy":"SYSTEM_PROPERTY",
                    "methodSign":"SYSTEM_PROPERTY",
                    "name":"SYSTEM_PROPERTY",
                    "type":"SyetemAction"
                },
                {
                    "desc":"Identity BD 管理（froze/unfroze）",
                    "execPermission":"RS",
                    "execPolicy":"IDENTITY_BD_MANAGE",
                    "methodSign":"IDENTITY_BD_MANAGE",
                    "name":"IDENTITY_BD_MANAGE",
                    "type":"SyetemAction"
                },
                {
                    "desc":"identity kyc 设置",
                    "execPermission":"RS",
                    "execPolicy":"KYC_SETTING",
                    "methodSign":"KYC_SETTING",
                    "name":"KYC_SETTING",
                    "type":"SyetemAction"
                },
                {
                    "desc":"手续费设置：合约地址 & 收取地址",
                    "execPermission":"RS",
                    "execPolicy":"SET_FEE_CONFIG",
                    "methodSign":"SET_FEE_CONFIG",
                    "name":"SET_FEE_CONFIG",
                    "type":"SyetemAction"
                },
                {
                    "desc":"手续费费率配置",
                    "execPermission":"RS",
                    "execPolicy":"SET_FEE_RULE",
                    "methodSign":"SET_FEE_RULE",
                    "name":"SET_FEE_RULE",
                    "type":"SyetemAction"
                },
                {
                    "desc":"保存存证",
                    "execPermission":"RS",
                    "execPolicy":"SAVE_ATTESTATION",
                    "methodSign":"SAVE_ATTESTATION",
                    "name":"SAVE_ATTESTATION",
                    "type":"SyetemAction"
                },
                {
                    "desc":"打快照",
                    "execPermission":"RS",
                    "execPolicy":"BUILD_SNAPSHOT",
                    "methodSign":"BUILD_SNAPSHOT",
                    "name":"BUILD_SNAPSHOT",
                    "type":"SyetemAction"
                }
            ]
        }
    ],
    "msg":"SUCCESS"
}
```