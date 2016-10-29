package com.qican.ygj.bean;

public class PostResult<T> {
    private T t;//请求结果
    private Exception e;//异常状态

    public T getResult() {
        return t;
    }

    public void setResult(T t) {
        this.t = t;
    }

    public Exception getException() {
        return e;
    }

    public void setException(Exception e) {
        this.e = e;
    }
}
