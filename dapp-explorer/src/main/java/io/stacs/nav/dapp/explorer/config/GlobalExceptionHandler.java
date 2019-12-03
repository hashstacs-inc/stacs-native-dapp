package io.stacs.nav.dapp.explorer.config;

import io.stacs.nav.drs.api.exception.DappException;
import io.stacs.nav.drs.api.model.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static io.stacs.nav.drs.api.exception.DappError.DAPP_COMMON_ERROR;
import static io.stacs.nav.drs.api.exception.DappError.PARAM_VALIDATE_ERROR;

/**
 * @author dekuofa <br>
 * @date 2019-12-03 <br>
 */
@RestControllerAdvice @Slf4j public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class) @ResponseBody
    public RespData<?> handlerException(Exception e, HttpServletRequest request) {
        log.error("global exception handler, unknown error, api :{}", request.getRequestURI(), e);
        return RespData.fail(DAPP_COMMON_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class) @ResponseBody
    public RespData<?> handlerValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("global argument not valid, api: {}", request.getRequestURI(), e);
        return RespData.fail(PARAM_VALIDATE_ERROR);
    }

    @ExceptionHandler(value = DappException.class) @ResponseBody
    public RespData<?> handlerStacsException(DappException e, HttpServletRequest request) {
        log.error("dapp error, api: {}", request.getRequestURI(), e);
        return RespData.fail(e.getCode(), e.getMsg());
    }

}
