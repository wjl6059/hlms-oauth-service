package com.tiansu.hlms.oauth.wx.controller;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.utils.RestUtil;
import com.tiansu.hlms.oauth.wx.service.RegisterService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author shufq
 * @description 给其他服务调用的接口
 * @date 2018/2/26 17:04
 */
@RestController
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     *
     * 提供给第三方服务注册时使用的接口
     * @param providerId 第三方服务的id
     * @return provider_id和provider_secret
     */
    @GetMapping("/register/provider")
    @ApiOperation(value = "提供给第三方服务注册时使用的接口", notes = "根据第三方服务的id生成秘钥并返回id和秘钥")
    @ApiImplicitParam(paramType = "query", dataType = "String", name = "providerId", value = "服务id", required = true)
    public Map<String, Object> provider(@RequestParam("providerId") String providerId){
        /*
        生成秘钥并存入数据库
         */
        registerService.insertProvider(providerId);

        /*
        返回providerId和秘钥
         */
        Map<String,Object> map = registerService.getProvider(providerId);
        return map;
    }

    /**
     * 提供给第三方应用注册时使用的接口
     * @param suiteId 第三方应用的id
     * @return suite_id和suite_secret
     */
    @GetMapping("/register/suite")
    @ApiOperation(value = "提供给第三方应用注册时使用的接口", notes = "根据第三方应用的id生成秘钥并返回id和秘钥")
    @ApiImplicitParam(paramType = "query", dataType = "String", name = "suiteId", value = "应用id", required = true)
    public Map<String, Object> suite(@RequestParam("suiteId") String suiteId,
                                     @RequestParam("suite_token") String suite_token){
        /*
        生成秘钥并存入数据库
         */
        registerService.insertSuite(suiteId,suite_token);

        /*
        返回providerId和秘钥
         */
        Map<String,Object> map = registerService.getSuite(suiteId);
        return map;
    }

    /**
     * 获取字段权限
     * @param access_token 鉴权token
     * @param field_owner 字段拥有着，1：部门，2：人员，3：设备，4：空间
     * @return
     */
    @GetMapping("/getFieldByToken")
    @ApiOperation(value = "查询字段权限", notes = "根据鉴权token和字段拥有者field_owner查询字段权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "field_owner", value = "字段拥有着，1：部门，2：人员，3：设备，4：空间", required = true)
    })
    public Result getFiledByToken(@RequestParam("access_token") String access_token,
                                  @RequestParam("field_owner") String field_owner){
        Map<String,Object> map = new HashMap<>();
        map.put("access_token",access_token);
        map.put("field_owner",field_owner);
        return registerService.getFiledByToken(map);
    }
}
