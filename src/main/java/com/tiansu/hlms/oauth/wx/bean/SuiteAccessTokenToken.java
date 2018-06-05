package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 用于接收获取第三方应用凭证接口的包体的参数
 * @create 2018-03-08 14:43
 */
@ApiModel(value = "获取第三方应用凭证接口body参数", description = "用于接收获取第三方应用凭证接口的包体的参数")
public class SuiteAccessTokenToken implements Serializable {
    private static final long serialVersionUID = 1L;

    //第三方应用id
    @ApiModelProperty(value = "第三方应用id",required = true)
    private String suite_id;

    //应用secret
    @ApiModelProperty(value = "应用secret",required = true)
    private String suite_secret;

    //后台推送的ticket
    @ApiModelProperty(value = "后台推送的ticket",required = true)
    private String suite_ticket;

    /**
     * get suite_id
     *
     * @return java.lang.String suite_id
     */
    public String getSuite_id() {
        return suite_id;
    }

    /**
     * set suite_id
     *
     * @param suite_id
     */
    public void setSuite_id(String suite_id) {
        this.suite_id = suite_id;
    }

    /**
     * get suite_secret
     *
     * @return java.lang.String suite_secret
     */
    public String getSuite_secret() {
        return suite_secret;
    }

    /**
     * set suite_secret
     *
     * @param suite_secret
     */
    public void setSuite_secret(String suite_secret) {
        this.suite_secret = suite_secret;
    }

    /**
     * get suite_ticket
     *
     * @return java.lang.String suite_ticket
     */
    public String getSuite_ticket() {
        return suite_ticket;
    }

    /**
     * set suite_ticket
     *
     * @param suite_ticket
     */
    public void setSuite_ticket(String suite_ticket) {
        this.suite_ticket = suite_ticket;
    }
}
