package com.tuanbaol.messageserver.aop;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
@Order(1)
public class ApiLogAop extends BaseAop{
    private final static String DEFAULT_MSG_PATTERN = "【{}】接口进入，方法路径-【{}】";

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void requestMapping() {
    }

    @Around(value = "getMapping()|| postMapping()||requestMapping()")
    public Object restLog(ProceedingJoinPoint pjp) throws Throwable {
        try {
            HttpServletRequest request = getRequest();
            if (request != null) {
                String methodFullPath = methodFullPath(pjp);
                String msgPattern = DEFAULT_MSG_PATTERN;
                MethodSignature signature = signature(pjp);
                String[] parameterNames = signature.getParameterNames();
                //with params
                if (ArrayUtils.isNotEmpty(parameterNames)) {
                    Object[] args = pjp.getArgs();
                    ArrayList<Object> params = CollectionUtil.ofList(args);
                    Class[] parameterTypes = signature.getParameterTypes();
                    //忽略参数偏移量
                    AtomicInteger ignored = new AtomicInteger(0);
                    String paramPattern = IntStream.range(0, parameterNames.length).filter(i -> {
                        if (isIgnoreType(parameterTypes[i])) {
                            params.remove(i - ignored.getAndIncrement());
                            return false;
                        }
                        return true;
                    }).mapToObj(i -> "，" + parameterNames[i] + "-【{}】").collect(Collectors.joining());
                    params.add(0, request.getRequestURL().toString());
                    params.add(1, methodFullPath);
                    log.info(msgPattern + paramPattern, params.toArray());
                } else {
                    log.info(msgPattern, request.getRequestURL().toString(), methodFullPath);
                }
            }
        } catch (Exception e) {
            log.error("error occur when printing api log", e);
        }
       return pjp.proceed();
    }

    private boolean isIgnoreType(Class parameterType) {
        return ServletRequest.class.isAssignableFrom(parameterType)
                || ServletResponse.class.isAssignableFrom(parameterType);
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes != null ? servletRequestAttributes.getRequest() : null;
    }
}
