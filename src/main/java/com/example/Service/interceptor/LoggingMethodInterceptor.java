package com.example.Service.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;

import java.util.UUID;

public class LoggingMethodInterceptor implements MethodInterceptor {

    private static final String X_TRACE_ID = "x-trace-id";

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String traceId = UUID.randomUUID().toString();
        traceId = traceId.substring(0, 13);
        if(MDC.get(X_TRACE_ID) == null) {
            MDC.put(X_TRACE_ID, traceId);
        }
        return methodInvocation.proceed();
    }

}
