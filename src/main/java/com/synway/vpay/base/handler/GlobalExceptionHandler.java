package com.synway.vpay.base.handler;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.base.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NoResourceFoundException.class)
    public String businessException(HttpServletRequest request) {
        log.error(String.format("request error, [%s] %s", request.getMethod(), request.getRequestURI()));
        return "404";
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result<Object> businessException(HttpServletRequest request, BusinessException e) {
        this.commonHandle(request, e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> exception(HttpServletRequest request, Exception e) {
        this.commonHandle(request, e);
        return Result.error(e.getMessage());
    }

    private void commonHandle(HttpServletRequest request, Exception e) {
        log.error(String.format("request error, url: %s", request.getRequestURI()), e);
    }
}
