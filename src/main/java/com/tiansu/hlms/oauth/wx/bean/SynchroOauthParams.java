package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;

/**
 * @author 王瑞
 * @description
 * @date 2018-04-27 14:33
 */
@ApiModel(value = "同步鉴权参数", description = "同步鉴权参数实体信息")
public class SynchroOauthParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private OauthParams oauthParams;

    private OauthSuite oauthSuite;

    /**
     * get oauthParams
     *
     * @return com.tiansu.hlms.oauth.wx.bean.OauthParams oauthParams
     */
    public OauthParams getOauthParams() {
        return oauthParams;
    }

    /**
     * set oauthParams
     *
     * @param oauthParams
     */
    public void setOauthParams(OauthParams oauthParams) {
        this.oauthParams = oauthParams;
    }

    /**
     * get oauthSuite
     *
     * @return com.tiansu.hlms.oauth.wx.bean.OauthSuite oauthSuite
     */
    public OauthSuite getOauthSuite() {
        return oauthSuite;
    }

    /**
     * set oauthSuite
     *
     * @param oauthSuite
     */
    public void setOauthSuite(OauthSuite oauthSuite) {
        this.oauthSuite = oauthSuite;
    }
}
