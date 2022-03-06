package com.csion.myIoc.annotation;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/3/2 19:12.
 * 组装-对toy的依赖注入（属性填充）
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Assemble {
}
