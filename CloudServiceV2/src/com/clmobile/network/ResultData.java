package com.clmobile.network;

public class ResultData<T> {
    public static final int STATUS_FAIL = -1;
    public static final int STATUS_SUCCEED = 0;
    public int Code;
    public String Msg;
    public T result;

    public int getCode() {
        return this.Code;
    }

    public String getMsg() {
        return this.Msg;
    }

    public T getResult() {
        return this.result;
    }

    public boolean isSuccess() {
        return this.Code == 1;
    }

    public void setCode(int status) {
        this.Code = status;
    }

    public void setMsg(String msg) {
        this.Msg = msg;
    }

    public void setResult(T result) {
        this.result = result;
    }
}