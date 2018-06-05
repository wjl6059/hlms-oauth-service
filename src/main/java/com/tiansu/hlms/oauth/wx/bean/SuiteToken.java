package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 接收服务商token的接口body参数
 * @date 2018-03-26 18:42
 */
@ApiModel(value = "接收服务商token的接口body参数", description = "用于接收服务商token的接口body参数")
public class SuiteToken implements Serializable {
    private static final long serialVersionUID = 1L;

    //租户id
    @ApiModelProperty(value = "租户id" ,required = false)
    private String corp_id;

    //应用id
    @ApiModelProperty(value = "应用id" ,required = true)
    private String suite_id;

    //服务商token
    @ApiModelProperty(value = "服务商token",required = true)
    private String token;

    /**
     * get corp_id
     *
     * @return java.lang.String corp_id
     */
    public String getCorp_id() {
        return corp_id;
    }

    /**
     * set corp_id
     *
     * @param corp_id
     */
    public void setCorp_id(String corp_id) {
        this.corp_id = corp_id;
    }

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
}
