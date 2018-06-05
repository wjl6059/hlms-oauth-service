package com.tiansu.hlms.oauth.wx.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 用于接收获取企业access_token的接口的包体参数
 * @create 2018-03-08 15:46
 */
@ApiModel(value = "取企业access_token的接口body参数", description = "用于接收获取企业access_token的接口的包体参数")
public class PermanentCode implements Serializable {
    private static final long serialVersionUID = 1L;

    //授权方corpid
    @ApiModelProperty(value = "授权方corpid" ,required = true)
    private String auth_corpid;

    //永久授权码，通过get_permanent_code获取
    @ApiModelProperty(value = "永久授权码",required = true)
    private String permanent_code;

    /**
     * get auth_corpid
     * 获取授权方corpid
     *
     * @return java.lang.String auth_corpid
     */
    public String getAuth_corpid() {
        return auth_corpid;
    }

    /**
     * set auth_corpid
     * 设置授权方corpid
     *
     * @param auth_corpid
     */
    public void setAuth_corpid(String auth_corpid) {
        this.auth_corpid = auth_corpid;
    }

    /**
     * get permanent_code
     * 获取永久授权码
     *
     * @return java.lang.String permanent_code
     */
    public String getPermanent_code() {
        return permanent_code;
    }

    /**
     * set permanent_code
     * 设置永久授权码
     *
     * @param permanent_code
     */
    public void setPermanent_code(String permanent_code) {
        this.permanent_code = permanent_code;
    }
}
