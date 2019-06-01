package com.tl666.loginregister.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理器，能捕获该工程所以类所发生的异常
 * @author 19760
 *
 */
@ControllerAdvice
public class TLExceptionHandler {
    @ExceptionHandler({Exception.class})//捕获所有类的所有异常 相当于catch
    public @ResponseBody Map exceptionTL(Exception e) {
        //redirect   forward
        Map<String,String> map = new HashMap<>();
        map.put("exception",e.toString());
        return map;
    }
//		@RequestMapping("exception")
//		@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE,reason="e")
//		public void exceptionTL2() {
//
//		}



}
