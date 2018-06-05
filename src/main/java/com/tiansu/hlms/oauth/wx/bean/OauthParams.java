package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 王瑞
 * @description 鉴权参数实体类
 * @date 2018-03-05 17:18
 */
@ApiModel(value = "鉴权参数", description = "鉴权参数实体信息")
public class OauthParams implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    @ApiModelProperty(value = "主键")
    private Long id;

    //租户鉴权标志
    @ApiModelProperty(value = "租户鉴权标志",required = true)
    private String corpId;

    //服务商应用id
    @ApiModelProperty(value = "服务商应用id",required = true)
    private String suiteId;

    //平台向应用推送的suitTicket
    @ApiModelProperty(value = "平台向应用推送的suitTicket")
    private String suitTicket;

    //suitTicket过期时间
    @ApiModelProperty(value = "suitTicket过期时间")
    private Date suitTicketExpireTime;

    //服务提供商应用和租户通讯的鉴权
    @ApiModelProperty(value = "服务提供商应用和租户通讯的鉴权")
    private String suitAccessToken;

    //suit_access_token超期时间
    @ApiModelProperty(value = "suit_access_token超期时间")
    private Date suitAccessTokenExpireTime;

    //预授权码
    @ApiModelProperty(value = "预授权码")
    private String preAuthCode;

    //预授权码超期时间
    @ApiModelProperty(value = "预授权码超期时间")
    private Date preAuthCodeExpireTime;

    //永久授权码
    @ApiModelProperty(value = "永久授权码")
    private String permanentCode;

    //平台开放接口鉴权token
    @ApiModelProperty(value = "平台开放接口鉴权token")
    private String accessToken;

    //AccessToken过期时间
    @ApiModelProperty(value = "AccessToken过期时间")
    private Date accessTokenExpireTime;

    //查询用户详情时的成员票据
    @ApiModelProperty(value = "查询用户详情时的成员票据")
    private String userTicket;

    //是否授权成功
    private int isAuthorized;

    //创建时间
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

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
    public String getSuitTicket() {
        return suitTicket;
    }

    /**
     * set suitTicket
     *
     * @param suitTicket
     */
    public void setSuitTicket(String suitTicket) {
        this.suitTicket = suitTicket;
    }

    /**
     * get suitTicketExpireTime
     *
     * @return java.lang.String suitTicketExpireTime
     */
    public Date getSuitTicketExpireTime() {
        if (this.suitTicketExpireTime == null) {
            return null;
        }
        return (Date) suitTicketExpireTime.clone();
    }

    /**
     * set suitTicketExpireTime
     *
     * @param suitTicketExpireTime
     */
    public void setSuitTicketExpireTime(Date suitTicketExpireTime) {
        if (suitTicketExpireTime == null)
            this.suitTicketExpireTime = null;
        else
            this.suitTicketExpireTime = (Date) suitTicketExpireTime.clone();
    }

    /**
     * get suitAccessToken
     *
     * @return java.lang.String suitAccessToken
     */
    public String getSuitAccessToken() {
        return suitAccessToken;
    }

    /**
     * set suitAccessToken
     *
     * @param suitAccessToken
     */
    public void setSuitAccessToken(String suitAccessToken) {
        this.suitAccessToken = suitAccessToken;
    }

    /**
     * get suitAccessTokenExpireTime
     *
     * @return java.util.Date suitAccessTokenExpireTime
     */
    public Date getSuitAccessTokenExpireTime() {
        if (this.suitAccessTokenExpireTime == null) {
            return null;
        }
        return (Date) suitAccessTokenExpireTime.clone();
    }

    /**
     * set suitAccessTokenExpireTime
     *
     * @param suitAccessTokenExpireTime
     */
    public void setSuitAccessTokenExpireTime(Date suitAccessTokenExpireTime) {
        if (suitAccessTokenExpireTime == null)
            suitAccessTokenExpireTime = null;
        else
            this.suitAccessTokenExpireTime = (Date) suitAccessTokenExpireTime.clone();
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
     * get preAuthCodeExpireTime
     *
     * @return java.util.Date preAuthCodeExpireTime
     */
    public Date getPreAuthCodeExpireTime() {
        if (this.preAuthCodeExpireTime == null) {
            return null;
        }
        return (Date) preAuthCodeExpireTime.clone();
    }

    /**
     * set preAuthCodeExpireTime
     *
     * @param preAuthCodeExpireTime
     */
    public void setPreAuthCodeExpireTime(Date preAuthCodeExpireTime) {
        if (preAuthCodeExpireTime == null)
            preAuthCodeExpireTime = null;
        else
            this.preAuthCodeExpireTime = (Date) preAuthCodeExpireTime.clone();
    }

    /**
     * get permanentCode
     *
     * @return java.lang.String permanentCode
     */
    public String getPermanentCode() {
        return permanentCode;
    }

    /**
     * set permanentCode
     *
     * @param permanentCode
     */
    public void setPermanentCode(String permanentCode) {
        this.permanentCode = permanentCode;
    }

    /**
     * get accessToken
     *
     * @return java.lang.String accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * set accessToken
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * get accessTokenExpireTime
     *
     * @return java.util.Date accessTokenExpireTime
     */
    public Date getAccessTokenExpireTime() {
        if (this.accessTokenExpireTime == null) {
            return null;
        }
        return (Date) accessTokenExpireTime.clone();
    }

    /**
     * set accessTokenExpireTime
     *
     * @param accessTokenExpireTime
     */
    public void setAccessTokenExpireTime(Date accessTokenExpireTime) {
        if (accessTokenExpireTime == null)
            accessTokenExpireTime = null;
        else
            this.accessTokenExpireTime = (Date) accessTokenExpireTime.clone();
    }

    /**
     * get userTicket
     *
     * @return java.lang.String userTicket
     */
    public String getUserTicket() {
        return userTicket;
    }

    /**
     * set userTicket
     *
     * @param userTicket
     */
    public void setUserTicket(String userTicket) {
        this.userTicket = userTicket;
    }

    /**
     * get createTime
     *
     * @return java.util.Date createTime
     */
    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return (Date) createTime.clone();
    }

    /**
     * set createTime
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        if (createTime == null)
            this.createTime = null;
        else
            this.createTime = (Date) createTime.clone();
    }

    /**
     * get isAuthorized
     *
     * @return int isAuthorized
     */
    public int getIsAuthorized() {
        return isAuthorized;
    }

    /**
     * set isAuthorized
     *
     * @param isAuthorized
     */
    public void setIsAuthorized(int isAuthorized) {
        this.isAuthorized = isAuthorized;
    }
}
