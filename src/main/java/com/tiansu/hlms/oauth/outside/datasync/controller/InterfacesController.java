package com.tiansu.hlms.oauth.outside.datasync.controller;

import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.outside.datasync.service.InterfacesService;
import com.tiansu.hlms.oauth.wx.service.OauthService;
import com.tiansu.hlms.oauth.wx.service.ServiceProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

/**
 * @author shufq
 * @description 第三方调用平台接口之前的鉴权
 * @date 2018/3/20 10:34
 */

@RestController
@Api(value = "interfacesController", description = "第三方调用平台接口之前的鉴权")
public class InterfacesController {


    //第三方调用平台之前鉴权的service层
    @Autowired
    private InterfacesService interfacesService;

    //鉴权token：主要调用里面的鉴别token是否合法的接口
    @Autowired
    private OauthService oauthService;

    //第三方对接业务实现
    @Autowired
    private ServiceProviderService providerService;
    /**
     * @author shufq
     * @description 获取部门列表
     * @date 2018/3/21 10:34
     * @param id 部门id,获取部门及其下的子部门，不填则全量获取
     * @param access_token 鉴权token
     * @return
     */
    @GetMapping(value = "/cgi-bin/department/list/v1.01" ,produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "获取部门列表", notes = "根据acess_token鉴权，然后根据权限返回部门列表相应的字段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "id", value = "部门id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map department(@RequestParam(name ="id",required = false) String id,
                          @RequestParam("access_token") String access_token){

        return interfacesService.getdepartment(id,access_token);

    }

    /**
     * @author shufq
     * @description 获取部门列表v1.02
     * @date 2018/5/17 10:34
     * @param access_token 鉴权token
     * @return
     */
    @GetMapping(value = "/cgi-bin/department/list/v1.02" ,produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "获取部门列表v1.02", notes = "根据acess_token鉴权，然后根据权限返回部门列表相应的字段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map departments(@RequestParam("access_token") String access_token){
        return interfacesService.getdepartments(access_token);
    }

    /**
     * @author shufq
     * @description 获取部门列表
     * @date 2018/3/21 10:34
     * @param department_id 部门id,获取部门及其下的子部门，不填则全量获取
     * @param access_token 鉴权token
     * @param fetch_child 是否递归获取子部门下面的成员（0：否 1：是）
     * @return
     */
    @GetMapping("/cgi-bin/user/simplelist/v1.01")
    @ApiOperation(value = "获取部门成员", notes = "根据acess_token鉴权，然后根据权限返回部门成员相应的字段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "department_id", value = "部门id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "fetch_child", value = "是否递归获取子部门下面的成员（0：否 1：是）", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map getUser(@RequestParam(name ="department_id",required = false) String department_id,
                       @RequestParam("access_token") String access_token,
                       @RequestParam(name ="fetch_child",required = true) String fetch_child){
        return interfacesService.getUser(department_id,access_token,fetch_child);
    }

    /**
     * @author shufq
     * @description 获取部门成员列表v1.02
     * @date 2018/5/17 10:34
     * @param access_token 鉴权token
     * @return
     */
    @GetMapping("/cgi-bin/user/simplelist/v1.02")
    @ApiOperation(value = "获取部门成员v1.02", notes = "根据acess_token鉴权，然后根据权限返回部门成员相应的字段")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map getUsers(@RequestParam("access_token") String access_token){
        return interfacesService.getUsers(access_token);
    }

    /**
     * @author shufq
     * @description 菜单按钮注册
     * @date 2018/3/29 10:34
     * @param permission 注册数据
     * @param suiteSecret 应用秘钥
     * @return
     */
    @PostMapping("/hlms/oauth/permission")
    @ApiOperation(value = "批量注册权限", notes = "json", produces = "application/json")
    public Result permission(@RequestBody JSONObject permission,
                             @RequestParam("suiteSecret") String suiteSecret){
//        String permissions = permission.getString("permissions");
        String permissions = permission.toString();
        //鉴权
        Result res = oauthService.checkSuiteSecret(suiteSecret);
        if(res.getErrcode()!="0"){
            return res;
        }
        return interfacesService.permission(permissions);

    }

    /**
     * @author shufq
     * @description 全量同步结果回调
     * @date 2018/5/17 10:34
     * @param body 请求包体
     * @param access_token 鉴权token
     * @return
     */
    @PostMapping("cgi-bin/basedata/version/pushresult/v1.02")
    @ApiOperation(value = "全量同步结果回调", notes = "json", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Result pushresult(@RequestBody JSONObject body,
                             @RequestParam("access_token") String access_token){
        return interfacesService.pushresult(body,access_token);
    }

    /**
     * @author shufq
     * @description 增量同步结果回调
     * @date 2018/5/17 10:34
     * @param body 请求包体
     * @param access_token 鉴权token
     * @return
     */
    @PostMapping("/cgi-bin/basedata/postincrementdataresult/v1.02")
    @ApiOperation(value = "增量同步结果回调", notes = "json", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Result postincrementdataresult(@RequestBody JSONObject body,
                                          @RequestParam("access_token") String access_token){
        return interfacesService.postincrementdataresult(body,access_token);
    }

    /**
     * @author shufq
     * @description 服务商获取平台增量数据
     * @date 2018/5/17 10:34
     * @param suiteid 应用id
     * @param corpid 租户id
     * @param datamodule 数据类型（101：部门信息，201：用户信息）
     * @param access_token 鉴权token
     * @return
     */
    @GetMapping("/cgi-bin/basedata/version/getincrementdata/v1.02")
    @ApiOperation(value = "服务商获取平台增量数据", notes = "服务商获取平台增量数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "suiteid", value = "应用id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "corpid", value = "租户id", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "datamodule", value = "数据类型（101：部门信息，201：用户信息）", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map getincrementdata(@RequestParam("suiteid") String suiteid,
                                @RequestParam("corpid") String corpid,
                                @RequestParam("datamodule") String datamodule,
                                @RequestParam("access_token") String access_token) {
        return interfacesService.getincrementdata(suiteid,corpid,datamodule,access_token);
    }
	    /**
     * @param access_token     平台鉴权token
     * @param body 请求包体
     * @return 返回验证成功失败
     *
     * @creator 陈旭
     * @createtime 2018/3/14 10:06
     * @description: 第三方服务APP登陆验证
     */
    @PostMapping("/cgi-bin/service/checklogin")
    @ApiOperation(value = "第三方服务APP登陆验证",  notes = "json", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map checkLogin(
            @RequestBody JSONObject body,
            @RequestParam("access_token") String access_token) {

        String loginname = (String)body.get("loginname");
        String password =  (String)body.get("password");
        Map map = providerService.checkLogin4App(access_token,loginname,password);

        return map;
    }

    /**
     * @author chenrl
     * @description 获取空间信息列表v1.02
     * @date 2018/5/17 10:34
     * @param access_token 鉴权token
     * @return
     */
    @GetMapping("/cgi-bin/location/simplelist/v1.02")
    @ApiOperation(value = "获取空间信息列表v1.02", notes = "根据acess_token鉴权，然后根据权限返回空间信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "access_token", value = "鉴权token", required = true)
    })
    public Map getLocationList(@RequestParam("access_token") String access_token){
        return interfacesService.getLocationList(access_token);
    }
}
