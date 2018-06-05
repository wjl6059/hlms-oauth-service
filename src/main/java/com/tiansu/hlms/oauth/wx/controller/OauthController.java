package com.tiansu.hlms.oauth.wx.controller;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.wx.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王瑞
 * @description 网页授权登录第三方
 * @date 2018-02-26 上午 11:24
 */
@RestController
@RequestMapping("/connect/oauth2")
@Api(value = "oauthController", description = "授权登录第三方")
public class OauthController {

    /*
     *鉴权参数业务类
     */
    @Autowired
    private OauthService oauthService;

    /**
     * 网页鉴权登录
     *
     * @param appid         企业的CorpID
     * @param redirect_uri  授权后重定向的回调链接地址，注意域名需要设置为第三方应用的可信域名
     * @param response_type 返回类型，此时固定为：code
     * @param scope         应用授权作用域。snsapi_base：暂为静默授权
     * @param agentid       企业应用的id。
     * @param state         重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/27 9:16
     * @description: 校验corpId, agentId, redirect_uri的合法性
     */
    @CrossOrigin(origins = "*",maxAge = 60)
    @GetMapping("/authorize")
    @ApiOperation(value = "鉴权参数校验", notes = "校验corpId,agentId,redirect_uri的合法性")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "appid", value = "企业的CorpID", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "redirect_uri", value = "授权后重定向的回调链接地址，注意域名需要设置为第三方应用的可信域名", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "response_type", value = "返回类型，此时固定为：code", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "scope", value = "应用授权作用域。snsapi_base：暂为静默授权", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "agentid", value = "企业应用的id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "state", value = "重定向后会带上state参数，企业可以填写a-zA-Z0-9的参数值，长度不可超过128个字节", required = false),
    })
    public Result authorize(
            @RequestParam(value = "appid", required = true) String appid,
            @RequestParam(value = "redirect_uri", required = true) String redirect_uri,
            @RequestParam(value = "response_type", required = false) String response_type,
            @RequestParam(value = "scope", required = false) String scope,
            @RequestParam(value = "agentid", required = true) String agentid,
            @RequestParam(value = "state", required = false) String state,
            HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");

        return oauthService.authorize(appid, agentid, redirect_uri, state, request);
    }


}
