package org.lychee.config;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.lychee.web.controller.SupperController;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.UnexpectedTypeException;
import java.util.List;

/**
 * 全局异常
 *
 * @author lizhixiao
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobleExceptionHandler extends SupperController {

    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        if(e instanceof ConstraintViolationException){
            log.warn(e.toString());
            return callbackFail(e + "");
        }
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError error = allErrors.get(0);
            String defaultMessage = error.getDefaultMessage();
            log.warn(defaultMessage);
            return callbackFail(defaultMessage);
        } else {
            log.error(e.toString());
            return callbackFail(e + "");
        }
    }

}

