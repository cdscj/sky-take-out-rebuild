package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if(message != null && message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            if (split.length >= 3) {
                String username = split[2];
                return Result.error(username + "已存在");
            }
            return Result.error("数据重复");
        }
        log.error("SQL完整性约束异常", ex);
        return Result.error("未知错误");
    }

    /**
     * 参数校验异常
     */
    @ExceptionHandler
    public Result exceptionHandler(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数验证失败");
        log.warn("参数校验失败: {}", msg);
        return Result.error(msg);
    }

    /**
     * 请求体格式错误
     */
    @ExceptionHandler
    public Result exceptionHandler(HttpMessageNotReadableException ex) {
        log.warn("请求体不可读: {}", ex.getMessage());
        return Result.error("请求参数格式错误，请检查JSON格式");
    }

    /**
     * 不支持的HTTP方法
     */
    @ExceptionHandler
    public Result exceptionHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("不支持的请求方法: {}", ex.getMethod());
        return Result.error("不支持的请求方法: " + ex.getMethod());
    }

    /**
     * 缺少必要参数
     */
    @ExceptionHandler
    public Result exceptionHandler(MissingServletRequestParameterException ex) {
        log.warn("缺少必要参数: {}", ex.getParameterName());
        return Result.error("缺少必要参数: " + ex.getParameterName());
    }

    /**
     * 空指针异常（不暴露内部细节）
     */
    @ExceptionHandler
    public Result exceptionHandler(NullPointerException ex) {
        log.error("空指针异常", ex);
        return Result.error("服务器内部错误");
    }

    /**
     * 其他未捕获异常（最终兜底）
     */
    @ExceptionHandler
    public Result exceptionHandler(Exception ex) {
        log.error("未捕获的异常", ex);
        return Result.error("服务器内部错误");
    }
}
