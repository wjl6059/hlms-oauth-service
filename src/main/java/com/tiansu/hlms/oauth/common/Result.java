package com.tiansu.hlms.oauth.common;

import com.tiansu.hlms.oauth.constant.OauthResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 王瑞
 * @description http请求返回最外层对象
 * @date 2018-02-01 15:30
 **/
@ApiModel(value = "结果对象", description = "结果对象信息")
public class Result<T> {

    /**
     * 响应结果码
     */
    @ApiModelProperty(value = "结果码", required = true)
    private String errcode;

    /**
     * 响应结果信息
     */
    @ApiModelProperty(value = "结果信息", required = true)
    private String errmsg;

    /**
     * 响应结果集
     */
    @ApiModelProperty(value = "结果内容")
    private T data;

    public Result(){
        errcode = OauthResultCode.SUCCESS.getCode();
        errmsg = OauthResultCode.SUCCESS.getMsg();
    }

    /**
     * get errorode
     * 获取响应结果码
     * @return java.lang.String errorode 返回结果码
     */
    public String getErrcode() {
        return errcode;
    }

    /**
     * set errorode
     * 设置响应结果码
     * @param errcode 结果码
     */
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    /**
     * get errmsg
     * 获取响应结果信息
     * @return java.lang.String errmsg 返回响应结果消息
     */
    public String getErrmsg() {
        return errmsg;
    }

    /**
     * set errmsg
     * 设置相应结果信息
     * @param errmsg 响应结果消息
     */
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * get data
     * 获取接口响应结果集
     * @return T data 返回响应结果集
     */
    public T getData() {
        return data;
    }

    /**
     * set data
     * 设置响应结果集
     * @param data 响应结果集
     */
    public void setData(T data) {
        this.data = data;
    }
}
