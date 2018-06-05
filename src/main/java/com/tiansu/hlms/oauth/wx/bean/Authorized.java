package com.tiansu.hlms.oauth.wx.bean;


/**
 * @author 王瑞
 * @description 推送授权失败所需的实体
 * @date 2018-04-24 16:22
 */
public class Authorized {

    //租户鉴权标志
    private String corpId;

    //服务商应用id
    private String suiteId;

    //平台向应用推送的suiteTicket
    private String suiteTicket;

    //预授权码
    private String preAuthCode;

    //第三方鉴权token
    private String suiteToken;

    //第三方接收uri
    private String uri;

    /**
     * get corpId
     *
     * @return java.lang.String corpId
     */
    public String getCorpId() {
        return corpId;
    }

    /**
     * set corpId
     *
     * @param corpId
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    /**
     * get suiteId
     *
     * @return java.lang.String suiteId
     */
    public String getSuiteId() {
        return suiteId;
    }

    /**
     * set suiteId
     *
     * @param suiteId
     */
    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    /**
     * get suitTicket
     *
     * @return java.lang.String suitTicket
     */
    public String getSuiteTicket() {
        return suiteTicket;
    }

    /**
     * set suitTicket
     *
     * @param suitTicket
     */
    public void setSuitTicket(String suiteTicket) {
        this.suiteTicket = suiteTicket;
    }

    /**
     * get preAuthCode
     *
     * @return java.lang.String preAuthCode
     */
    public String getPreAuthCode() {
        return preAuthCode;
    }

    /**
     * set preAuthCode
     *
     * @param preAuthCode
     */
    public void setPreAuthCode(String preAuthCode) {
        this.preAuthCode = preAuthCode;
    }

    /**
     * get suiteToken
     *
     * @return java.lang.String suiteToken
     */
    public String getSuiteToken() {
        return suiteToken;
    }

    /**
     * set suiteToken
     *
     * @param suiteToken
     */
    public void setSuiteToken(String suiteToken) {
        this.suiteToken = suiteToken;
    }

    /**
     * get uri
     *
     * @return java.lang.String uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * set uri
     *
     * @param uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
