package com.csion.myIoc.annotation;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/3/2 19:12.
 * 用于表示某个类是一个toy
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AToy {
}
