package com.tiansu.hlms.oauth.wx.controller;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.wx.bean.SynchroOauthParams;
import com.tiansu.hlms.oauth.wx.bean.Version;
import com.tiansu.hlms.oauth.wx.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王瑞
 * @description 鉴权参数相关接口类控制类
 * @create 2018-03-05 下午 7:43
 */
@RestController
@RequestMapping("/hlms/oauth/model")
@Api(value = "oauthParamsController", description = "鉴权参数相关接口类")
public class OauthParamsController {

    /*
     *鉴权参数业务类
     */
    @Autowired
    private OauthService oauthService;

    /**
     * @param tenantId    租户id
     * @param tenantDeploy 是否是SaaS租户
     * @param tenantUri 本地部署的租户uri
     * @param tenantSuite 租户应用
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/5 18:54
     * @description: 对外开放的鉴权参数新增接口 (依赖关系：提供给系统模块创建租户的时候后调用)
     */
    @PostMapping("/create_oauth_params")
    @ApiOperation(value = "创建鉴权参数", notes = "创建鉴权参数，并将信息保存到数据库中")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "tenantId", value = "租户id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "tenantSuite", value = "租户应用json格式", required = true)
    })
    public Result createOauthParams(@RequestParam("tenantId") String tenantId,
                                    @RequestParam("tenantDeploy") String tenantDeploy,
                                    @RequestParam("tenantUri") String tenantUri,
                                    @RequestParam("tenantSuite") String tenantSuite) {
        return oauthService.createOauthParams(tenantId, tenantDeploy, tenantUri, tenantSuite);
    }

    /**
     * @param accessToken 鉴权token
     * @return 返回结果对象
     *
     * @creator 舒方强
     * @createtime 2018/3/19 09:54
     * @description: 校验token是否合法
     */
    @PostMapping("/checktoken")
    @ApiOperation(value = "校验token是否合法", notes = "校验token是否合法")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "accessToken", value = "鉴权token", required = true)
    })
    public Result checktoken(@RequestParam("accessToken") String accessToken) {
        return oauthService.checkToken(accessToken);
    }

    /**

     * @param versions 版本
     * @return 应用id
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 封装返回要推送的数据版本号信息
     *               依赖关系：提供获取应用id和对应的服务商鉴权token的接口-->suite应用注册模块-->数据同步
     *               增量接口调通之后删除
     */
    @PostMapping("/get_suiteId")
    @ApiOperation(value = "封装返回要推送的数据版本号信息", notes = "封装返回要推送的数据版本号信息")
    public Result getSuiteIdByCorpId(@RequestBody List<Version> versions) {
        return oauthService.getSuiteIdByCorpId(versions);
    }

    /**
     * @param suiteId 应用id
     * @return 服务商提供的token
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 根据应用id获取服务商提供的token (目前没有被调用)
     */
    @GetMapping("/get_suiteToken")
    @ApiOperation(value = "获取服务商提供的token", notes = "根据应用id获取服务商提供的token")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "suiteId", value = "应用id", required = true)
    })
    public Result getSuiteToken(@RequestParam(value = "suiteId",required = true) String suiteId) {
        return oauthService.getSuiteTokenBySiteId(suiteId);
    }

    /**
     * @param synchroOauthParams 鉴权参数
     * @return 同步结果
     *
     * @creator 王瑞
     * @createtime 2018/4/27 15:59
     * @description: 同步鉴权参数
     */
    @PostMapping("/synchronous_oauth_param")
    @ApiOperation(value = "同步鉴权参数", notes = "同步鉴权参数")
    public Result synchroOauthParams(@RequestBody SynchroOauthParams synchroOauthParams){
        return oauthService.synchroOauthParams(synchroOauthParams);
    }

}
