package com.tuanbaol.messageserver.aop;

import com.tuanbaol.messageserver.annotation.Duration;
import com.tuanbaol.messageserver.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
@Order(2)
public class ExecuteDurationAop extends BaseAop {
    private final static String DEFAULT_MSG_PATTERN = "【{}】方法执行，耗时【{}】毫秒";

    @Pointcut("@annotation(com.tuanbaol.messageserver.annotation.Duration)")
    public void durationPointcut() {
    }

    @Around(value = "durationPointcut()")
    public Object restLog(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            try {
                long duration = System.currentTimeMillis() - start;
                String msgPattern = DEFAULT_MSG_PATTERN;
                MethodSignature signature = signature(pjp);
                String[] parameterNames = signature.getParameterNames();
                Duration annotation = signature.getMethod().getAnnotation(Duration.class);
                ArrayList params = CollectionUtil.ofList(methodFullPath(pjp), duration);
                //with params
                if (ArrayUtils.isNotEmpty(parameterNames) && annotation.printParam()) {
                    Object[] args = pjp.getArgs();
                    params.addAll(CollectionUtil.ofList(args));
                    String paramPattern = IntStream.range(0, parameterNames.length).mapToObj(i -> "，" + parameterNames[i] + "-【{}】").collect(Collectors.joining());
                    log.info(msgPattern + paramPattern, params.toArray());
                } else {
                    log.info(msgPattern, params.toArray());
                }
            } catch (Exception e) {
                log.error("error occur when printing duration log", e);
            }
        }
    }

}
