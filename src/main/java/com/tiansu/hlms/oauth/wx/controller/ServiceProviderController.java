package com.tiansu.hlms.oauth.wx.controller;

import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.constant.WeChatResultCode;
import com.tiansu.hlms.oauth.utils.RandomCharsGenerator;
import com.tiansu.hlms.oauth.wx.bean.*;
import com.tiansu.hlms.oauth.wx.service.ServiceProviderService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author 王瑞
 * @description 第三方应用接口
 * @create 2018-02-28 上午 10:32
 */
@RestController
@RequestMapping("/cgi-bin/service")
@Api(value = "serviceProviderController", description = "第三方对接接口")
public class ServiceProviderController {

    /*
     *第三方对接业务实现
     */
    @Autowired
    private ServiceProviderService providerService;

    /**
     * @param suiteAccessTokenToken 用于接收获取第三方应用凭证接口的包体的参数
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 上午 10:42
     * @description: 该API用于获取第三方应用凭证（suite_access_token）。
     * <p/>
     * 由于第三方服务商可能托管了大量的企业，其安全问题造成的影响会更加严重，故API中除了合法来源IP校验之外，还额外增加了suite_ticket作为安全凭证。
     * <p/>
     * 获取suite_access_token时，需要suite_ticket参数。suite_ticket由企业微信后台定时推送给“指令回调URL”，每十分钟更新一次。
     * <p/>
     * uite_ticket实际有效期为30分钟，可以容错连续两次获取suite_ticket失败的情况，但是请永远使用最新接收到的suite_ticket。
     * <p/>
     * 通过本接口获取的suite_access_token有效期为2小时，开发者需要进行缓存，不可频繁获取。
     */
    @PostMapping("/get_suite_token/v1.01")
    @ApiOperation(value = "获取第三方应用凭证", notes = "该API用于获取第三方应用凭证（suite_access_token）")
    public Map getSuiteToken(@RequestBody SuiteAccessTokenToken suiteAccessTokenToken) {
        Map map = providerService.getSuiteToken(suiteAccessTokenToken);
        return map;

    }


    /**
     * @param suiteAccessToken 第三方应用access_token,最长为512字节
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 上午 10:33
     * @description: 服务商根据suite_access_token获取预授权码--该接口已废弃
     */
    @GetMapping("/get_pre_auth_code/v1.01")
    @ApiOperation(value = "获取预授权码--该接口已废弃", notes = "服务商根据suite_access_token获取预授权码--该接口已废弃")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "String", name = "suite_access_token", value = "第三方应用access_token,最长为512字节", required = true)})
    public Map getPreAuthCode(@RequestParam(value = "suite_access_token", required = true) String suiteAccessToken) {
        Map map = providerService.getPreAuthCode(suiteAccessToken);
        return map;
    }

    /**
     * @param suiteAccessToken 第三方应用access_token,最长为512字节
     * @param preAuthCode    请求包体{ "auth_code": "临时授权码" }
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 0028 上午 10:47
     * @description: 该API用于使用临时授权码换取授权方的永久授权码，并换取授权信息、企业access_token，
     * 临时授权码一次有效。建议第三方优先以userid或手机号为主键、其次以email为主键来建立自己的管理员账号。
     */
    @PostMapping("/get_permanent_code/v1.01")
    @ApiOperation(value = "获取永久授权码", notes = "服务商根据suite_access_token获取永久授权码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "suite_access_token", value = "第三方应用access_token,最长为512字节", required = true)
    })
    public Map getPermanentCode(
            @RequestParam(value = "suite_access_token", required = true) String suiteAccessToken,
            @RequestBody PreAuthCode preAuthCode) {

        Map map = providerService.getPermanentCode(suiteAccessToken, preAuthCode);
        return map;
    }


    /**
     * @param suiteAccessToken 第三方应用access_token,最长为512字节
     * @param corpToken        请求包体
     *                         <p>
     *                         {"auth_corpid": "授权方corpid",
     *                         "permanent_code": "永久授权码，通过get_permanent_code获取"
     *                         }
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 下午 1:59
     * @description: 第三方服务商在取得企业的永久授权码后，通过此接口可以获取到企业的access_token。
     * 获取后可通过通讯录、应用、消息等企业接口来运营这些应用。
     */
    @PostMapping("/get_corp_token/v1.01")
    @ApiOperation(value = "获取access_token", notes = "获取到企业的access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "suite_access_token", value = "第三方应用access_token,最长为512字节", required = true)
    })
    public Map getCorpToken(
            @RequestParam(value = "suite_access_token", required = true) String suiteAccessToken,
            @RequestBody PermanentCode corpToken) {

        Map map = providerService.getCorpToken(suiteAccessToken, corpToken);

        return map;

    }


    /**
     * @param corPid     企业id
     * @param corpSecret 企业秘钥
     * @return 企业身access_token
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:06
     * @description: 企业获取自身access_token
     */
    @GetMapping("/gettoken")
    @ApiOperation(value = "获取access_token", notes = "企业获取自身access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "corpid", value = "企业id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "corpsecret", value = "企业秘钥", required = true)
    })
    public Map getToken(
            @RequestParam(value = "corpid", required = true) String corPid,
            @RequestParam(value = "corpsecret", required = true) String corpSecret) {

        Map map = providerService.getToken(corPid, corpSecret);

        return map;
    }


    /**
     * @param suiteToken 接收服务商token的接口body参数
     * @return
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:27
     * @description: 服务提供商鉴权token接收接口
     */
    @PostMapping("/provider_auth_token/v1.01")
    @ApiOperation(value = "服务提供商鉴权token接收接口", notes = "服务提供商鉴权token接收接口")
    public Map providerAuthToken(@RequestBody SuiteToken suiteToken) {

        Map map = providerService.providerAuthToken(suiteToken);

        return map;
    }


    /**
     * @param accessToken 第三方应用的suie_access_token
     * @param code        通过成员授权获取到的code，最大为512字节。每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 下午 4:18
     * @description: 第三方根据code获取企业成员信息
     */
    @GetMapping("/getuserinfo3rd/v1.01")
    @ApiOperation(value = "获取企业成员信息", notes = "第三方根据code获取企业成员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "第三方应用access_token,最长为512字节", required = true),
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
     * @return
     *
     * @creator 王瑞
     * @createtime 2018/2/28 下午 4:41
     * @description: 根据user_ticket获取用户信息
     */
    @PostMapping("/getuserdetail3rd")
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

