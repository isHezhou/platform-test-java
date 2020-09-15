package com.jc.test11.platform.config;

import com.jc.platform.common.result.ResultCodeEnum;
import com.jc.platform.common.result.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Set;

/**
 * ClassName GlobalExceptionHandler
 * Description 全局异常处理
 *
 * @author 平台管理员
 * @version 7.0.0
 * @date 
 */
@Slf4j
@RestControllerAdvice
public class ResponseExceptionHandle {

    /**
     * Controller 异常拦截处理
     *
     * @param  e 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public ResultModel validationBodyException(Exception e) {
        // spring validation 校验异常
        if (e instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) e).getConstraintViolations();
            ConstraintViolation constr = (ConstraintViolation) new ArrayList(constraintViolations).get(0);
            String message = constr.getMessageTemplate();
            return ResultModel.failed(ResultCodeEnum.REQUEST_PARAMS_ERROR.code(), "".equals(message) ? "请填写正确信息" : message);
        // 其他异常
        }else{
            return ResultModel.failed();
        }
    }

}
