package com.chf.app.aop.logging;

import static com.chf.app.constants.SystemConstants.BASE_PACKAGE;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chf.app.domain.OperationLog;
import com.chf.app.repository.OperationLogRepository;

// 操作日志

@Aspect
public class OperateLoggingAspect {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Pointcut("execution(* " + BASE_PACKAGE + ".web.rest..*(..))")
    public void loggingPointcut() {
    }

    @After("springBeanPointcut()() && loggingPointcut()")
    @Transactional
    public void logAfter(JoinPoint joinPoint) throws Throwable {
        try {
            OperationLog operationLog = new OperationLog();

            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method targetMethod = methodSignature.getMethod();
                addMethodInfo(operationLog, targetMethod);
            }
            if (StringUtils.isNoneEmpty(operationLog.getMethod())) {
                operationLogRepository.save(operationLog);
            }
        } catch (Exception e) {
        }
    }

    private void addMethodInfo(OperationLog operationLog, Method targetMethod) {
        PostMapping postMapping = targetMethod.getAnnotation(PostMapping.class);
        if (postMapping != null) {
            operationLog.setMethod(RequestMethod.POST.name());
            operationLog.setPath(getPath(postMapping.path()));
            return;
        }

        PutMapping putMapping = targetMethod.getAnnotation(PutMapping.class);
        if (putMapping != null) {
            operationLog.setMethod(RequestMethod.PUT.name());
            operationLog.setPath(getPath(putMapping.path()));
            return;
        }

        DeleteMapping deleteMapping = targetMethod.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) {
            operationLog.setMethod(RequestMethod.DELETE.name());
            operationLog.setPath(getPath(deleteMapping.path()));
            return;
        }

        RequestMapping requestMapping = targetMethod.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            String method = getMethod(requestMapping.method());
            if (StringUtils.isNotEmpty(method)) {
                operationLog.setMethod(method);
                operationLog.setPath(getPath(requestMapping.path()));
            }
            return;
        }
    }

    public static List<RequestMethod> Log_Method_list = Arrays.asList(RequestMethod.POST, RequestMethod.PUT,
            RequestMethod.DELETE);

    private String getMethod(RequestMethod[] methods) {
        for (RequestMethod m : methods) {
            if (Log_Method_list.contains(m)) {
                return m.name();
            }
        }
        return "";
    }

    private String getPath(String[] paths) {
        if (ArrayUtils.isEmpty(paths)) {
            return "";
        }
        return paths[0];
    }
}
