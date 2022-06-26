package com.ice.chatserver.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
* @author ice2020x
* @Date: 2021/12/18
* @Description: 统一返回结果的类
**/
@Data
public class R {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 构造函数私有，防止造出对象
    **/
    protected R() {
    }

    /**
    * @author ice2020x
    * @Date: 2021/12/18
    * @Description: 返回r 或者this是因为可以链式编程，比较方便
    **/
    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.resultEnum(ResultEnum.SUCCESS);
        return r;
    }

    //失败静态方法
    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.resultEnum(ResultEnum.ERROR);
        return r;
    }

    public R resultEnum(ResultEnum resultEnum) {
        this.code(resultEnum.getCode());
        this.message(resultEnum.getMessage());
        return this;
    }

    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }
}
