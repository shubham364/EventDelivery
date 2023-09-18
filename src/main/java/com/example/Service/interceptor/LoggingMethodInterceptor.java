package com.example.Service.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.MDC;

import java.util.UUID;

import static com.example.model.Constants.X_TRACE_ID;

public class LoggingMethodInterceptor implements MethodInterceptor {

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
