package io.stacs.nav.drs.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author liuyu
 * @description
 * @date 2019-12-12
 */
@Configuration @Slf4j public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override public void registerErrorPages(ErrorPageRegistry registry) {
        log.info("[registerErrorPages] web has error");
        ErrorPage error404Page = new ErrorPage(NOT_FOUND, "/index.html");
        registry.addErrorPages(error404Page);
    }
}
