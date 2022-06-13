package com.ice.chatserver.annon;

import java.lang.annotation.*;

/**
* @author ice2020x
* @Date: 2021/12/17
* @Description: 主键自增加策略
**/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoIncKey {

}