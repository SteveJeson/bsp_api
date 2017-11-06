package com.zdzc.electrocar.util.dynamicDataSource;

import java.lang.annotation.*;


@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}