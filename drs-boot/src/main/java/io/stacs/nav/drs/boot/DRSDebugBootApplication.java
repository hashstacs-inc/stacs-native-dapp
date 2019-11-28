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

import org.springframework.boot.SpringApplication;

import static io.stacs.nav.drs.service.constant.Constants.DRS_VERSION_KEY;
import static io.stacs.nav.drs.service.utils.ResourceLoader.getManifest;

/**
 * A sample spring boot web project repackage as ark fat jar.
 *
 * @author suimi
 * @since 1.0.0
 */
public class DRSDebugBootApplication {
    // todo 代码整理 创建新的Application专用配置
    private static final String DRS_CONFIG_PREFIX = "-stacs.";

    public static void main(String[] args) {

        getManifest(DRSDebugBootApplication.class)
            .ifPresent(manifest -> System.out.println(manifest.getMainAttributes().getValue(DRS_VERSION_KEY)));
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith(DRS_CONFIG_PREFIX)) {
                args[i] = String.format("--%s", arg.substring(DRS_CONFIG_PREFIX.length()));
            }
        }

        SpringApplication.run(DRSBootApplication.class, args);
    }
}