package com.ly.storeserver.common.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ly.storeserver.common.bean.R;
import com.ly.storeserver.common.enums.RStatus;
import com.ly.storeserver.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 11:16
 * @Version V1.0.0
 **/
@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public R validationExceptionHandler(BindException ex) throws JsonProcessingException {
        //1.此处先获取BindingResult
        BindingResult bindingResult = ex.getBindingResult();
        //2.获取错误信息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        //3.组装异常信息
        Map<String,String> message = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            message.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        //4.将map转换为JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(message);
        //5.返回错误信息
        log.error(json);
        return new R<>(RStatus.PARAMS_ERROR, json);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public R serviceExceptionHandler(ServiceException e) {
        log.error(e.getMsg());
        Integer code = e.getCode();
        String msg = e.getMsg();
        return new R<>(code, msg);
    }

}
