package cn.cnnic.plan.exception.handler;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnnic.plan.exception.ErrPageException;
import cn.cnnic.plan.exception.InvalidParamException;
import cn.cnnic.plan.exception.model.ExceptionResponse;
import cn.cnnic.plan.exception.model.ExceptionStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
 
    @ExceptionHandler(InvalidParamException.class)
    @ResponseBody
    public ExceptionResponse handleParamException(HttpServletRequest request, InvalidParamException ex) {
        return new ExceptionResponse().fail(ex.getCode(),ex.getMessage());
    }
    
    @ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public ExceptionResponse handleUnsupportedEncodingException(HttpServletRequest request, UnsupportedEncodingException ex) {
        return new ExceptionResponse().fail(ExceptionStatus.UNSUPPORTED_ENCODING.value(),ex.getMessage());
    }
    @ExceptionHandler(ParseException.class)
    @ResponseBody
    public ExceptionResponse handleUnparsedDateException(HttpServletRequest request, ParseException ex) {
        return new ExceptionResponse().fail(ExceptionStatus.UNPARSED_DATE.value(),ex.getMessage());
    }
    
    @ExceptionHandler(ErrPageException.class)
    @ResponseBody
    public ExceptionResponse handleErrPageException(HttpServletRequest request, ErrPageException ex) {
        return new ExceptionResponse().fail(ex.getCode(),ex.getMessage());
    }
}
