package com.tiansu.hlms.oauth.wx.bean;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 应用鉴权参数（企业微信版）
 * @create 2018-03-12 14:36
 */
public class OauthSuite implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long id;

    //服务提供商提供的系统应用id
    private Long suiteId;

    //服务提供商应用鉴权密钥
    private String suiteSecret;

    //向服务商推送的suiteTicket
    private String suiteTicket;

    //提供给服务商的鉴权token
    private String suiteAccessToken;

    //服务提供商推送的suiteToken
    private String suiteToken;

    /**
     * get id
     *
     * @return java.lang.Long id
     */
    public Long getId() {
        return id;
    }

    /**
     * set id
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get suiteId
     * 获取服务提供商提供的系统应用id
     *
     * @return java.lang.Long suiteId
     */
    public Long getSuiteId() {
        return suiteId;
    }

    /**
     * set suiteId
     * 设置服务提供商提供的系统应用id
     *
     * @param suiteId
     */
    public void setSuiteId(Long suiteId) {
        this.suiteId = suiteId;
    }

    /**
     * get suiteSecret
     * 获取服务提供商应用鉴权密钥
     *
     * @return java.lang.String suiteSecret
     */
    public String getSuiteSecret() {
        return suiteSecret;
    }

    /**
     * set suiteSecret
     * 设置服务提供商应用鉴权密钥
     *
     * @param suiteSecret
     */
    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
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
     * get suiteTicket
     *
     * @return java.lang.String suiteTicket
     */
    public String getSuiteTicket() {
        return suiteTicket;
    }

    /**
     * set suiteTicket
     *
     * @param suiteTicket
     */
    public void setSuiteTicket(String suiteTicket) {
        this.suiteTicket = suiteTicket;
    }

    /**
     * get suiteAccessToken
     *
     * @return java.lang.String suiteAccessToken
     */
    public String getSuiteAccessToken() {
        return suiteAccessToken;
    }

    /**
     * set suiteAccessToken
     *
     * @param suiteAccessToken
     */
    public void setSuiteAccessToken(String suiteAccessToken) {
        this.suiteAccessToken = suiteAccessToken;
    }
}
