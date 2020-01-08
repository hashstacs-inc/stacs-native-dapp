#Interface documentation

#### Download Dapp
##### API：/dapp/download
Request Method：GET
##### Request parameters

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| filePath       | `String`        | 64       | Y    | Dapp file address path (support network and local path)

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data Dapp

##### Dapp data
| Attribute          | Type          | The maximum length | Required | Instructions                           |
| ------------- | ------------- | -------- | ---- | ------------------------------ |
| name          | `String`      |          | Y    | Dapp name
| version       | `String`      |          | Y    | Dapp version
| contextPath   | `String`      |          | N    | web context path
| status        | `String`      |          | Y    | DOWNLOAD、INITIALIZED、RUNNING、STOPPED
| runError      | `String`      |          | N    | Run error message
| fileName      | `String`      |          | Y    | The file name
| icon          | `String`      |          | N    | Icon url
| author        | `String`      |          | N    | Release people
| remark        | `String`      |          | N    | remark


#### Initialize the Dapp
##### API：/dapp/init/{appName}
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data


#### Installed Dapp
##### API：/dapp/install/{appName}
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data



#### Dapp uninstall/stop
##### API：/dapp/uninstall/{appName}
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data



#### Dapp 配置信息查询
##### API：/dapp/config/query/{appName}
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data


#### Dapp 配置
##### API：/dapp/config/{appName}
Request Method：POST
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| appName        | `String`        | 64       | Y    | Dapp name
| {}             | `Json String`   |          | Y    | Dapp Configuration parameters，The json object of key-value

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data

#### Dapp Store list query
##### API：/dapp/queryAppStore
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<AppProfileVO>

###### Sample return parameters:
```
[
  {

    "showName":"App1",
    "name":"dapp-sample",
    "icon":"appstore/img/icon1.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"http://www.appstore.cn",
    "status":"STOPPED"
  },
  {

    "showName":"App2",
    "name":"dapp-sample",
    "icon":"appstore/img/icon2.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"http://www.appstore.cn",
    "status":"DOWNLOAD"
  }
]
```

#### Dapp has downloaded the list query
##### API：/dapp/installList
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |

##### The return value：

| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<AppProfileVO>

###### Sample return parameters:
```
[
  {

    "showName":"App1",
    "name":"dapp-sample",
    "icon":"appstore/img/icon1.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"",
    "status":""
  },
  {

    "showName":"App2",
    "name":"dapp-sample",
    "icon":"appstore/img/icon2.ong",
    "author":"test",
    "remark":"this is a test app",
    "downloadUrl":"",
    "status":"DOWNLOAD"
  }
]
```

#### Policy information query
##### API：/drs/queryAllPolicy
Request Method：GET
##### Parameter list

NO DATA

###### Sample Request parameters:
```
NO DATA
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<Policy>
#####RsDomain
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| policyId       | `String`        | 32       | Y    | id
| policyName     | `String`        | 24       | Y    | name
###### Sample return parameters:
```
[
    {"policyId":"policyId-0","policyName":"policy name-0"},
    {"policyId":"policyId-1","policyName":"policy name-1"},
    {"policyId":"policyId-2","policyName":"policy name-2"}
]
```
#### Permission information query
##### API：/drs/queryPermissionList
Request Method：GET
##### Parameter list

NO DATA

###### Sample Request parameters:
```
NO DATA
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<Policy>
#####RsDomain
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| permissionName | `String`        | 32       | Y    | Permission to name
###### Sample return parameters:
```
[
    {"permissionName":"name-0"},
    {"permissionName":"name-1"},
    {"permissionName":"name-2"}
]
```

#### Domain information query
##### API：/drs/queryAllDomain
Request Method：GET
##### Parameter list

NO DATA

###### Sample Request parameters:
```
NO DATA
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<RsDomain>
#####RsDomain
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| domainId       | `String`        | 64       | Y    | The return code '000000' indicates success
| desc           | `String`        | 1024     | N    | 描述
###### Sample return parameters:
```
[
    {"desc":"test desc","domainId":"domainId-0"},
    {"desc":"test desc","domainId":"domainId-1"},
    {"desc":"test desc","domainId":"domainId-2"}
]
```

#### Contract information query
##### API：/drs/queryContract
Request Method：GET
##### Parameter list

NO DATA

###### Sample Request parameters:
```
NO DATA
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<RsDomain>
#####RsDomain
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| address        | `String`        | 64       | Y    | Contract address
| name           | `String`        | 1024     | N    | The name of the contract
| symbol         | `String`        | 1024     | N    | token
| extension      | `String`        | 1024     | N    | Extension field
| bdCode         | `String`        | 1024     | N    | bd code
| status         | `String`        | 1024     | N    | state
| blockHeight    | `String`        | 1024     | N    | Block height
| txId           | `String`        | 1024     | N    | Transaction id
| actionIndex    | `String`        | 1024     | N    | action index
| version        | `String`        | 1024     | N    | The version number
| code           | `String`        | 1024     | N    | The source code
| createTime     | `String`        | 1024     | N    | data time
| bdType         | `String`        | 1024     | N    | bd Type

###### Sample return parameters:
```
[
    {"address":"test desc","name":"domainId-0"},
    {"address":"test desc","name":"domainId-0"},
]
```


#### Method information query in contract
##### API：/drs/queryMethodParam
Request Method：POST
##### Parameter list

##### The return value：
| Attribute               | Type            | The maximum length | Required | Instructions                           |
| ------------------| -------------   | -------- | ---- | -------------------------------- |
| contractAddress   | `String`        | 32       | Y    | Contract address
| methodSign        | `String`        | 128      | Y    | Method name:transfer(address,uint256)

###### Sample Request parameters:
```
{
"contractAddress":"177f03aefabb6dfc07f189ddf6d0d48c2f60cdbf",
"methodSign":"transfer(address,uint256)"
}
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，Map<String,String>

###### Sample return parameters:
```
{
  "data":{
    "_to":"address",
    "_value":"uint256"
  }
} 
```


#### 获取签名原值
##### API：/bd/getSignValue
Request Method：POST
##### Parameter list

##### The return value：
| Attribute               | Type            | The maximum length | Required | Instructions                           |
| ------------------| -------------   | -------- | ---- | -------------------------------- |
| functionName      | `String`        | 32       | Y    | Method name
| param             | `JSONObject`    | 1024     | Y    | Parameter object

###### Sample Request parameters:
```
{
"functionName":"IDENTITY_SETTING",
"param":{}
}
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data

###### Sample return parameters:
```
{
"txId":"tx_id_abc_123","sign":"xxxx"
}
```



#### 根据私钥获取签名
##### API：/bd/getSignature
Request Method：POST
##### Parameter list

##### The return value：
| Attribute               | Type            | The maximum length | Required | Instructions                           |
| ------------------| -------------   | -------- | ---- | -------------------------------- |
| priKey            | `String`        | 64       | Y    | The private key
| signValue         | `String`        | 1024     | Y    | To be signed value

###### Sample Request parameters:
```
{
"priKey":"xxx",
"signValue":"xxxxSystemBD"
}
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `String`        |          | N    | Return signature data

###### Sample return parameters:
```
{
"data":"xxxxxxxxxxx"
"code":"000000"
"msg":"SUCCESS"
}
```


#### Obtain transaction information

##### API：/bd/queryTxs
Request Method：POST
##### Parameter list

##### The return value：
| Attribute               | Type            | The maximum length | Required | Instructions                           |
| ------------------| -------------   | -------- | ---- | -------------------------------- |
| blockHeight       | `Long`          | 20       | N    | block height
| txId              | `String`        | 64       | N    | Transaction id
| submitter         | `String`        | 32       | N    | Transaction submitter
| pageNum           | `int`           | 10       | Y    | Number of pages
| pageSize          | `int`           | 104      | Y    | Number each page

###### Sample Request parameters:
```
{
"pageNum":1,
"pageSize":10
}
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                           |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `PageInfo`      |          | N    | Return the data


###### Sample return parameters:
```
NO DATA

```

#### BD(Business Define)  
##### API：/drs/bd/query
Request Method：GET
##### Parameter list

|     Attribute     | Type     | The maximum length | Required | Instructions                                              |
| :----------: | -------- | -------- | ---- | ------------------------------------------------- |
| bdCode       | `String` | 32       | N    | The BD code to query is allowed to be null
###### Sample Request parameters:
```
1.NO DATA：http://domain/drs/bd/query
2.HAS DATA：http://domain/drs/bd/query?bdCode=SysBD
```
##### The return value：
| Attribute            | Type            | The maximum length | Required | Instructions                         |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| code           | `int`           | 6        | Y    | The return code '000000' indicates success
| msg            | `String`        | 64       | Y    | Message information
| data           | `Object`        |          | N    | Return the data，List<BusinessDefine>
#####BusinessDefine
| Attribute            | Type                | The maximum length | Required | Instructions                      |
| -------------  | ----------------------    | ------------------- | -------- | -------------------------------- |
| code           | `int`                     | 64                       | Y    | bd code
| name           | `String`                  | 64                       | Y    | bd name
| bdType         | `String`                  | 32                       | Y    | Type
| desc           | `String`                  | 1024                     | N    | Description
| initPermission | `String`                  | 64                       | Y    | The initial permissions
| initPolicy     | `String`                  | 32                       | Y    | The original policy
| functions      | `List<FunctionDefine>`    |                          | Y    | Has the function of
| bdVersion      | `String`                  |  4                       | Y    | version
######FunctionDefine
| Attribute            | Type            | The maximum length | Required | Instructions                        |
| -------------  | -------------   | -------- | ---- | -------------------------------- |
| name           | `String`        | 64       | Y    | bd name
| Type           | `String`        | 32       | Y    | Type
| desc           | `String`        | 256      | N    | Description
| methodSign     | `String`        | 256      | Y    | method 
| execPermission | `String`        | 64       | Y    | Execute permissions
| execPolicy     | `String`        | 32       | Y    | Implement the policy
###### Sample return parameters:
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
                    "desc":"Identity Setting",
                    "execPermission":"RS",
                    "execPolicy":"IDENTITY_SETTING",
                    "methodSign":"IDENTITY_SETTING",
                    "name":"IDENTITY_SETTING",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"BD Publish",
                    "execPermission":"RS",
                    "execPolicy":"BD_PUBLISH",
                    "methodSign":"BD_PUBLISH",
                    "name":"BD_PUBLISH",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Permission Register",
                    "execPermission":"RS",
                    "execPolicy":"PERMISSION_REGISTER",
                    "methodSign":"PERMISSION_REGISTER",
                    "name":"PERMISSION_REGISTER",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Permission Auth",
                    "execPermission":"RS",
                    "execPolicy":"AUTHORIZE_PERMISSION",
                    "methodSign":"AUTHORIZE_PERMISSION",
                    "name":"AUTHORIZE_PERMISSION",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Permission Cancel",
                    "execPermission":"RS",
                    "execPolicy":"CANCEL_PERMISSION",
                    "methodSign":"CANCEL_PERMISSION",
                    "name":"CANCEL_PERMISSION",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"policy Register",
                    "execPermission":"RS",
                    "execPolicy":"REGISTER_POLICY",
                    "methodSign":"REGISTER_POLICY",
                    "name":"REGISTER_POLICY",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"policy Modify",
                    "execPermission":"RS",
                    "execPolicy":"MODIFY_POLICY",
                    "methodSign":"MODIFY_POLICY",
                    "name":"MODIFY_POLICY",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"RS Register",
                    "execPermission":"RS",
                    "execPolicy":"REGISTER_RS",
                    "methodSign":"REGISTER_RS",
                    "name":"REGISTER_RS",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"RS Cancel",
                    "execPermission":"RS",
                    "execPolicy":"CANCEL_RS",
                    "methodSign":"CANCEL_RS",
                    "name":"CANCEL_RS",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"CA Auth",
                    "execPermission":"RS",
                    "execPolicy":"CA_AUTH",
                    "methodSign":"CA_AUTH",
                    "name":"CA_AUTH",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"CA Cancel",
                    "execPermission":"RS",
                    "execPolicy":"CA_CANCEL",
                    "methodSign":"CA_CANCEL",
                    "name":"CA_CANCEL",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"CA Update",
                    "execPermission":"RS",
                    "execPolicy":"CA_UPDATE",
                    "methodSign":"CA_UPDATE",
                    "name":"CA_UPDATE",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Node Join",
                    "execPermission":"RS",
                    "execPolicy":"NODE_JOIN",
                    "methodSign":"NODE_JOIN",
                    "name":"NODE_JOIN",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Node leave",
                    "execPermission":"RS",
                    "execPolicy":"NODE_LEAVE",
                    "methodSign":"NODE_LEAVE",
                    "name":"NODE_LEAVE",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"System Attribute Setting",
                    "execPermission":"RS",
                    "execPolicy":"SYSTEM_PROPERTY",
                    "methodSign":"SYSTEM_PROPERTY",
                    "name":"SYSTEM_PROPERTY",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Identity BD Management（froze/unfroze）",
                    "execPermission":"RS",
                    "execPolicy":"IDENTITY_BD_MANAGE",
                    "methodSign":"IDENTITY_BD_MANAGE",
                    "name":"IDENTITY_BD_MANAGE",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"identity kyc Setting",
                    "execPermission":"RS",
                    "execPolicy":"KYC_SETTING",
                    "methodSign":"KYC_SETTING",
                    "name":"KYC_SETTING",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Fee setting",
                    "execPermission":"RS",
                    "execPolicy":"SET_FEE_CONFIG",
                    "methodSign":"SET_FEE_CONFIG",
                    "name":"SET_FEE_CONFIG",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Fee Rule Setting",
                    "execPermission":"RS",
                    "execPolicy":"SET_FEE_RULE",
                    "methodSign":"SET_FEE_RULE",
                    "name":"SET_FEE_RULE",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Save Attestation",
                    "execPermission":"RS",
                    "execPolicy":"SAVE_ATTESTATION",
                    "methodSign":"SAVE_ATTESTATION",
                    "name":"SAVE_ATTESTATION",
                    "Type":"SyetemAction"
                },
                {
                    "desc":"Build Snapshot",
                    "execPermission":"RS",
                    "execPolicy":"BUILD_SNAPSHOT",
                    "methodSign":"BUILD_SNAPSHOT",
                    "name":"BUILD_SNAPSHOT",
                    "Type":"SyetemAction"
                }
            ]
        }
    ],
    "msg":"SUCCESS"
}
```