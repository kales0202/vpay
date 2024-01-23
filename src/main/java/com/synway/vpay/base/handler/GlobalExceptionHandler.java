package com.synway.vpay.base.handler;

import com.synway.vpay.base.bean.Result;
import com.synway.vpay.base.exception.AuthorizedException;
import com.synway.vpay.base.exception.BusinessException;
import com.synway.vpay.base.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NoResourceFoundException.class, NoHandlerFoundException.class})
    public Object businessException(HttpServletRequest request, Exception e) {
        this.commonHandle(request, e);
        if (this.jsonRequest(request)) {
            return this.jsonResult(NotFoundException.CODE, NotFoundException.MESSAGE);
        }
        return "/404.html";
    }

    @ExceptionHandler(value = AuthorizedException.class)
    public Object authorizedException(HttpServletRequest request, AuthorizedException e) {
        this.commonHandle(request, e);
        // 判断accept是否是json请求
        if (this.isInterface(request)) {
            return this.jsonResult(AuthorizedException.CODE, AuthorizedException.MESSAGE);
        }
        return "/login.html";
    }

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Result<Object> businessException(HttpServletRequest request, BusinessException e) {
        this.commonHandle(request, e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result<Object> exception(HttpServletRequest request, Exception e) {
        this.commonHandle(request, e);
        return Result.error(e.getMessage());
    }

    private void commonHandle(HttpServletRequest request, Exception e) {
        log.error(String.format("%s ---> [%s] %s", e.getClass().getSimpleName(), request.getMethod(), request.getRequestURI()));
    }

    private boolean jsonRequest(HttpServletRequest request) {
        return request.getHeader("accept").contains("application/json");
    }

    private ModelAndView jsonResult(int code, String msg) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("code", code);
        modelAndView.addObject("msg", msg);
        return modelAndView;
    }

    private boolean isInterface(HttpServletRequest request) {
        // 通过debug调试可以看出request中含有该对象。
        HandlerMethod handlerMethod = (HandlerMethod) request.
                getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler");
        // getBeanType()拿到发送请求得类模板（Class），getDeclaredAnnotationsByType(指定注解类模板)通过指定得注解，得到一个数组。
        RestController[] annotations1 = handlerMethod.getBeanType().getDeclaredAnnotationsByType(RestController.class);
        ResponseBody[] annotations2 = handlerMethod.getBeanType().getDeclaredAnnotationsByType(ResponseBody.class);
        ResponseBody[] annotations3 = handlerMethod.getMethod().getAnnotationsByType(ResponseBody.class);
        // 判断当类上含有@RestController或是@ResponseBody或是方法上有@ResponseBody时，则表明该异常是一个接口请求发生的
        return annotations1.length > 0 || annotations2.length > 0 || annotations3.length > 0;
    }
}
