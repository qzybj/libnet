package com.brady.libnet.bean;


import com.brady.libnet.interfaces.IErrorInfo;

/**
 * Created by ZhangYuanBo on 2016/8/30.
 */
public class ErrorInfo implements IErrorInfo {
    private int code;
    private String message;

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
