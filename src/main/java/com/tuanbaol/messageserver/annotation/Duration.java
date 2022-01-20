package com.tuanbaol.messageserver.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Duration {
    boolean printParam() default true;
}
