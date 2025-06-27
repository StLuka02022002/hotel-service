package com.example.hotel_service.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String level() default "INFO";

    boolean logParams() default true;

    boolean logMethod() default true;

    boolean logResult() default true;

    boolean logExecutionTime() default false;

    String message() default "";
}