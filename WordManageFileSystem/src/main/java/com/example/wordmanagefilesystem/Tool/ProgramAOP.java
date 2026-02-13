package com.example.wordmanagefilesystem.Tool;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class ProgramAOP {

    //打印每一个Implement文件里的函数的用时
    @Around("execution(* com.example.wordmanagefilesystem.Service.Implement.*.*(..))")
    public Object showFunctionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        long result = (end - start);
        log.info("方法耗时{}ms" , result);
        return proceed;
    }
}
