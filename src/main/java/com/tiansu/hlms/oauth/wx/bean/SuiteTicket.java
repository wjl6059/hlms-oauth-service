package com.tiansu.hlms.oauth.wx.bean;

import java.util.List;

/**
 * @author 王瑞
 * @date 2018-03-16 09:21
 * @description 定时推送suiteTicket所需实体
 */
public class SuiteTicket {

    //服务商提供的token
    private String token;

    //应用id
    private String suiteId;

    //推送地址
    private List<String> uri;

    /**
     * get token
     *
     * @return java.lang.String token
     */
    public String getToken() {
        return token;
    }

    /**
     * set token
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
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
     * get uri
     *
     * @return java.util.List<java.lang.String> uri
     */
    public List<String> getUri() {
        return uri;
    }

    /**
     * set uri
     *
     * @param uri
     */
    public void setUri(List<String> uri) {
        this.uri = uri;
    }
}
