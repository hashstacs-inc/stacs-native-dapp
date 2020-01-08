[![Build Status](https://travis-ci.org/Auroraic/stacs-native-dapp.svg?branch=master)](https://travis-ci.org/Aurorasic/stacs-native-dapp)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.stacs.nav:stacs-native-dapp&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.stacs.nav:stacs-native-dapp)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.stacs.nav/stacs-native-dapp/badge.svg?cache=foo)](https://search.maven.org/search?q=g:%22io.stacs.nav%22%20AND%20a:%22stacs-native-dapp%22)
# 欢迎来到 Stacs-Native-Dapp

## Stacs-Native-Dapp是什么？
Dapp是指去中心化的应用程序。目前传统的区块链DApp是以智能合约为基础，在其之上实现一整套的业务逻辑及数据处理。 而Stacs Dapp是以BD为基础，实现BD的部分或全部业务功能，不同Dapp各自执行BD的一个或多个业务功能，多个Stacs Dapp相互协助执行BD，从而解决在金融业务执行过程中的分权问题。
 
各模块说明：
1. drs-boot: Dapp的运行环境，负责管理dapp的下载、安装、运行、卸载等生命周期
2. drs-service: 提供给Dapp相关接口的实现，实现对区块链的请求、回调的统一处理
3. drs-api: Dapp需要依赖的API接口层
4. dapp-core: 包含Drs与Dapp的事件通信处理
5. dapp-sample: 一个dapp的样例代码

## 关于Stacs-Native-Dapp更详细介绍
[详细介绍](https://stacs-native.netlify.com/design/dapp/)
