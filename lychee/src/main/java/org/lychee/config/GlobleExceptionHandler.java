package org.lychee.config;

import lombok.extern.slf4j.Slf4j;
import org.lychee.web.controller.BaseController;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常
 *
 * @author lizhixiao
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobleExceptionHandler extends BaseController {

    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(HttpServletRequest request, Exception e) {
        log.info(e.toString());
        if (e instanceof ConstraintViolationException) {
            return callbackFail(e + "");
        }
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> allErrors = ex.getAllErrors();
            ObjectError error = allErrors.get(0);
            String defaultMessage = error.getDefaultMessage();
            return callbackFail(defaultMessage);
        } else {
            return callbackFail(e + "");
        }
    }

}

