package com.cn.entity;

import java.io.Serializable;

/**
 * Created by will wu on 2016/7/26.
 */

public class BaseBean <T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5047830257368039672L;

    private String code;
    private String msg;
    private T result;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }
    @Override
    public String toString() {
        return "BaseBean [msg=" + msg + ", code=" + code + ", result=" + result + "]";
    }


}