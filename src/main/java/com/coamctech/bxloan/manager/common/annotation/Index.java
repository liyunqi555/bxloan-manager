package com.coamctech.bxloan.manager.common.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao on 15/8/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Index {
    public String name();
}
