package com.ssn.ws.rest.response;

/**
 *
 * @author vkvarma
 */
public class SSNLoginResponse {
    
    private boolean success;
    private String code;
    private String msg;
    private SSNUserDataResponse data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public SSNUserDataResponse getData() {
        return data;
    }

    public void setData(SSNUserDataResponse data) {
        this.data = data;
    }    
}