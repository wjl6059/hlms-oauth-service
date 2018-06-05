package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 用于接收获取企业永久授权码接口的包体参数
 * @create 2018-03-08 15:44
 */
@ApiModel(value = "获取企业永久授权码接口body参数", description = "用于接收获取企业永久授权码接口的包体参数")
public class PreAuthCode implements Serializable {
    private static final long serialVersionUID = 1L;

    //临时授权码，会在授权成功时附加在redirect_uri中跳转回第三方服务商网站，或通过回调推送给服务商。长度为64至512个字节
    @ApiModelProperty(value = "临时授权码，会在授权成功时附加在redirect_uri中跳转回第三方服务商网站，或通过回调推送给服务商。长度为64至512个字节",required = true)
    private String auth_code;

    /**
     * get auth_code
     *
     * @return java.lang.String auth_code
     */
    public String getAuth_code() {
        return auth_code;
    }

    /**
     * set auth_code
     *
     * @param auth_code
     */
    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }
}
