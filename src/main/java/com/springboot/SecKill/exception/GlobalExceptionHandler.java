package com.springboot.SecKill.exception;

import com.springboot.SecKill.result.CodeMsg;
import com.springboot.SecKill.result.Result;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;

import java.util.List;

/**
 * @author WilsonSong
 * @date 2018/8/2/002
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)       //拦截所有的异常
    public Result<String> exceptionHandler(HttpServletRequest httpServletRequest, Exception e){

        if(e instanceof GlobalException){
            GlobalException ex = (GlobalException) e;
            return Result.error(ex.getCm());
        }else if(e instanceof BindException){  // 参数校验异常
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error= errors.get(0);
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            //其他异常
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
