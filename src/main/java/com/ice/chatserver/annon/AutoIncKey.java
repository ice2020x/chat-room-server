package com.ice.chatserver.annon;

import java.lang.annotation.*;

//主键自增加策略
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoIncKey {

}