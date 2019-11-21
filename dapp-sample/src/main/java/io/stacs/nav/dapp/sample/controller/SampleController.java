/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.stacs.nav.dapp.sample.controller;

import io.stacs.nav.dapp.sample.service.SampleService;
import io.stacs.nav.drs.api.model.SampleRequest;
import io.stacs.nav.drs.api.model.SampleResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyu
 * @description
 * @date 2019-10-18
 */
@RequestMapping("/sample") @RestController @Slf4j public class SampleController {
    @Value("${spring.application.name}") private String appName;
    @Value("${server.port}") private int port;
    @Autowired private SampleService sampleService;

    /**
     * api test
     *
     * @return
     */
    @GetMapping("/service/{request}") public SampleResult service(@PathVariable("request") String request) {
        log.info("appName:{}, port:{}", appName, port);
        return sampleService.service(new SampleRequest(request));
    }

}
