package com.example.rbacdemo.common.api;

/**
 * 响应数据封装类
 *
 * @author djhaa
 */
public class CommonResult<T> {
    /**
     * code 响应代码
     * message 响应信息
     * data 响应数据
     */
    private long code;
    private String message;
    private T data;

    protected CommonResult() {

    }

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回请求成功结果
     * */
    public static <T> CommonResult<T> success(String message, T data) {
        long responseCode = ResultCode.SUCCESS.getCode();
        String responseMessage = message == null ? ResultCode.SUCCESS.getMessage() : message;
        return new CommonResult<>(responseCode, responseMessage, data);
    }

    public static <T> CommonResult<T> success(T data) {
        return success(null, data);
    }

    /**
     * 返回请求失败结果
     * */
    public static <T> CommonResult<T> failed(ErrorCode errorCode, String message) {
        long responseCode = errorCode == null ? ResultCode.FAILED.getCode() : errorCode.getCode();
        String responseMessage = message == null ? ResultCode.FAILED.getMessage() : message;
        return new CommonResult<>(responseCode, responseMessage, null);
    }

    public static <T> CommonResult<T> failed(String message) {
        return failed(null, message);
    }

    public static <T> CommonResult<T> failed() {
        return failed(null, null);
    }

    /**
     * 返回参数验证失败结果
     * */
    public static <T> CommonResult<T> validateFailed(String message) {
        long responseCode = ResultCode.UNAUTHORIZED.getCode();
        String responseMessage = message == null ? ResultCode.UNAUTHORIZED.getMessage() : message;
        return new CommonResult<>(responseCode, responseMessage, null);
    }

    public static <T> CommonResult<T> validateFailed() {
        return validateFailed(null);
    }

    /**
     * 返回未登录请求结果
     * */
    public static <T> CommonResult<T> unauthorized(String message, T data) {
        long responseCode = ResultCode.UNAUTHORIZED.getCode();
        String responseMessage = message == null ? ResultCode.UNAUTHORIZED.getMessage() : message;
        return new CommonResult<>(responseCode, responseMessage, data);
    }

    public static <T> CommonResult<T> unauthorized(T data) {
        return unauthorized(null, data);
    }

    /**
     * 返回未授权请求结果
     * */
    public static <T> CommonResult<T> forbidden(String message, T data) {
        long responseCode = ResultCode.FORBIDDEN.getCode();
        String responseMessage = message == null ? ResultCode.FORBIDDEN.getMessage() : message;
        return new CommonResult<>(responseCode, responseMessage, data);
    }

    public static <T> CommonResult<T> forbidden(T data) {
        return forbidden(null, data);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
