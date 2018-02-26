package com.qianmi.weidian.domain;

/**
 * HTTP返回基类，返回正确结果status>0，错误结果status<0，
 * message为服务器端返回消息，客户端直接显示
 * T data为返回的json对象或数组，自动解析为Java对象
 * errorCodes为服务器端返回的Dubbo接口错误码，逗号分隔，仅做调试用途
 * Created by zhangxitao on 15/8/17.
 */
public class BaseResponse<T> {

    protected String result;

    protected int rescode;

    protected String msg;

    protected T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
