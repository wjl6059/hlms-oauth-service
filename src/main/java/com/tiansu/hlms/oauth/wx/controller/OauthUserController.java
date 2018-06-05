package com.tiansu.hlms.oauth.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.constant.WeChatResultCode;
import com.tiansu.hlms.oauth.utils.RandomCharsGenerator;
import com.tiansu.hlms.oauth.wx.bean.UserDetail;
import com.tiansu.hlms.oauth.wx.service.ServiceProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author 王瑞
 * @description
 * @date 2018-03-15 15:05
 */
@RestController
@RequestMapping("/cgi-bin/user")
@Api(value = "oauthUserController", description = "获取用户信息")
public class OauthUserController {
    /*
     *第三方对接业务实现
     */
    @Autowired
    private ServiceProviderService providerService;

    /**
     * @param accessToken 第三方应用的suie_access_token
     * @param code        通过成员授权获取到的code，最大为512字节。每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 下午 4:18
     * @description: 第三方根据code获取企业成员信息
     */
    @GetMapping("/getuserinfo")
    @ApiOperation(value = "获取成员信息", notes = "根据code获取成员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "调用接口凭证", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "code", value = "通过成员授权获取到的code，最大为512字节。每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期", required = true)
    })
    public Map getUserInfo3rd(
            @RequestParam(value = "access_token", required = true) String accessToken,
            @RequestParam(value = "code", required = true) String code) {

        Map map = providerService.getuserinfo3rd(accessToken, code);

        return map;
    }


    /**
     * @param accessToken
     * @param userDetail
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 下午 4:41
     * @description: 根据user_ticket获取用户信息
     */
    @PostMapping("/getuserdetail")
    public JSONObject getUserDetail3rd(
            @RequestParam(value = "access_token", required = true) String accessToken,
            @RequestBody UserDetail userDetail) {


        JSONObject resultJson = new JSONObject();

        resultJson.put("errcode", WeChatResultCode.SUCCESS.getCode());
        resultJson.put("errmsg", WeChatResultCode.SUCCESS.getMsg());
        resultJson.put("corpid", RandomCharsGenerator.uuid(16));
        resultJson.put("userid", "zhangsan");
        resultJson.put("name", "张三");
        resultJson.put("department", "[3]");
        resultJson.put("position", "张三");
        resultJson.put("mobile", "15913215421");
        resultJson.put("gender", "1");
        resultJson.put("email", "zhangsan@xx.com");
        resultJson.put("gender", "http://www.tiansu.cn/");
        return resultJson;
    }
}
