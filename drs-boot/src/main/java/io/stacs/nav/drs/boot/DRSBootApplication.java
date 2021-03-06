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
package io.stacs.nav.drs.boot;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * A sample spring boot web project repackage as ark fat jar.
 *
 * @author suimi
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = "io.stacs.nav.drs") @Slf4j
@EnableTransactionManagement
@MapperScan({ "io.stacs.nav.drs.*.dao"})
public class DRSBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(DRSBootApplication.class, args);
    }
}