package com.example.user.entity.dto;

public class RequestResultDto {
    /**
     * 返回状态码
     */
    private int msgCode;

    /**
     * 结果描述
     */
    private String msgDesc;

    /**
     * Token
     */
    private String token;

    /**
     * 验证码
     */
    private String captchaCode;

    /**
     * 数据对象
     */
    private Object resultData;

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public void setMsgDesc(String msgDesc) {
        this.msgDesc = msgDesc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
