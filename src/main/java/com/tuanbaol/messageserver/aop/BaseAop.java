package com.tuanbaol.messageserver.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

public abstract class BaseAop {
    protected String methodFullPath(ProceedingJoinPoint pjp) {
        MethodSignature signature = signature(pjp);
        String name = signature.getName();
        String declaringTypeName = signature.getDeclaringTypeName();
        return declaringTypeName + "." + name;
    }

    protected MethodSignature signature(ProceedingJoinPoint pjp) {
        MethodInvocationProceedingJoinPoint mpjp = (MethodInvocationProceedingJoinPoint) pjp;
        return (MethodSignature) mpjp.getSignature();
    }
}
