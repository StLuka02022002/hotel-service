package com.example.hotel_service.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@within(Log) || @annotation(Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Log classLog = joinPoint.getTarget().getClass().getAnnotation(Log.class);
        Log methodLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Log.class);
        Log currentLog = methodLog != null ? methodLog : classLog;

        if (currentLog == null) {
            return joinPoint.proceed();
        }

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        StopWatch stopWatch = null;
        if (currentLog.logExecutionTime()) {
            stopWatch = new StopWatch();
            stopWatch.start();
        }

        if (isLogLevelEnabled(currentLog.level())) {

            String message = "";

            boolean rightMessage = currentLog.message() == null || currentLog.message().isBlank();

            if (rightMessage || currentLog.logMethod()) {
                String methodName = signature.getMethod().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();
                message = String.format("Вызов метода %s.%s", className, methodName);
            }

            if (currentLog.logParams()) {
                String params = getMethodParameters(signature, joinPoint.getArgs());
                if (!message.isEmpty()) {
                    message = message.concat(" ");
                }
                message = message.concat("Параметры: ").concat(params);
            }

            logMessage(currentLog.level(), message);
            if (!rightMessage) {
                logMessage(currentLog.level(), currentLog.message().trim());
            }
        }

        try {
            Object result = joinPoint.proceed();
            logMessageResult(result, currentLog);
            return result;
        } catch (Throwable exception) {
            logMessageError(exception);
            throw exception;
        } finally {
            logExecutionTime(stopWatch);
        }
    }

    private void logMessageResult(Object result, Log loggable) {
        if (isLogLevelEnabled(loggable.level()) && loggable.logResult()) {
            logMessage(loggable.level(), "Результат: " + (result != null ? result.toString() : "null"));
        }
    }

    private void logMessageError(Throwable ex) {
        if (isLogLevelEnabled("ERROR")) {
            log.error("Ошибка в методе: {}", ex.getMessage(), ex);
        }

    }

    private void logExecutionTime(StopWatch stopWatch) {
        if (stopWatch != null) {
            stopWatch.stop();
            long executionTime = stopWatch.getTime(TimeUnit.MILLISECONDS);
            if (isLogLevelEnabled("INFO")) {
                log.info("Время выполнения: {} мс", executionTime);
            }
        }
    }

    private String getMethodParameters(MethodSignature signature, Object[] args) {
        String[] paramNames = signature.getParameterNames();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < paramNames.length; i++) {
            sb.append(paramNames[i]).append("=");
            sb.append(args[i] != null ? args[i] : "null");
            if (i < paramNames.length - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    private boolean isLogLevelEnabled(String level) {
        return switch (level.toUpperCase()) {
            case "TRACE" -> log.isTraceEnabled();
            case "DEBUG" -> log.isDebugEnabled();
            case "WARN" -> log.isWarnEnabled();
            case "ERROR" -> log.isErrorEnabled();
            default -> log.isInfoEnabled();
        };
    }

    private void logMessage(String level, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        switch (level.toUpperCase()) {
            case "TRACE" -> log.trace(message);
            case "DEBUG" -> log.debug(message);
            case "WARN" -> log.warn(message);
            case "ERROR" -> log.error(message);
            default -> log.info(message);
        }
    }
}
