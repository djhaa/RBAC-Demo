package com.example.rbacdemo.common.exception;

import com.example.rbacdemo.common.api.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 *
 * @author djhaa
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    @ResponseBody
    public CommonResult<Object> handler(ApiException e) {
        return e.getErrorCode() == null ? CommonResult.failed(e.getMessage()) : CommonResult.failed(e.getErrorCode(), null);
    }
}
