package com.tiansu.hlms.oauth.wx.bean;

/**
 * @author 王瑞
 * @description 版本
 * @date 2018-03-20 10:51
 */
public class Version {

    //服务商提供的token
    private String token;

    //租户id
    private String tenantId;

    //应用id
    private String suiteId;

    //数据版本号
    private Integer dataModule;

    //推送地址
    private String uri;

    //同步版本号
    private String synchroVersion;

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
     * get tenantId
     *
     * @return java.lang.String tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * set tenantId
     *
     * @param tenantId
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
     * get dataModule
     *
     * @return java.lang.Integer dataModule
     */
    public Integer getDataModule() {
        return dataModule;
    }

    /**
     * set dataModule
     *
     * @param dataModule
     */
    public void setDataModule(Integer dataModule) {
        this.dataModule = dataModule;
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

    /**
     * get synchroVersion
     *
     * @return java.lang.String synchroVersion
     */
    public String getSynchroVersion() {
        return synchroVersion;
    }

    /**
     * set synchroVersion
     *
     * @param synchroVersion
     */
    public void setSynchroVersion(String synchroVersion) {
        this.synchroVersion = synchroVersion;
    }
}
