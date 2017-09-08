package com.zhon.baselib.mvpbase.baseImpl;


/*
 * 项目名:    BaseLib
 * 包名       com.zhon.baselib.mvpbase.baseImpl
 * 文件名:    BaseBean
 * 创建者:    ZJB
 * 创建时间:  2017/9/7 on 14:17
 * 描述:     TODO 请求结果基础bean；仅用于判断操作是否成功
 */
public class BaseBean<T> {

    private String code;
    private String message;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
