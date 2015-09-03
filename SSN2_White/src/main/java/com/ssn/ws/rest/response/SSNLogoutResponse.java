package com.ssn.ws.rest.response;

/**
 *
 * @author vkvarma
 */
public class SSNLogoutResponse {

    private boolean success;   
    private String  msg;
    private String  code;
    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}