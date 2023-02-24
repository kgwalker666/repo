package com.kgwalker.common;

import lombok.Getter;

@Getter
public class R {
    private Integer code;
    private String msg;
    private Object data;

    public static R status() {
        R r = new R();
        r.code = 20000;
        r.msg = "成功";
        return r;
    }

    public R data(Object data) {
        this.data = data;
        return this;
    }
}
