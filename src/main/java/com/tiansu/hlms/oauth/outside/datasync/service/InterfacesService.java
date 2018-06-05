package com.tiansu.hlms.oauth.outside.datasync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.common.ResultUtil;
import com.tiansu.hlms.oauth.constant.OauthResultCode;
import com.tiansu.hlms.oauth.outside.datasync.bean.SpaceInfo;
import com.tiansu.hlms.oauth.utils.RestUtil;
import com.tiansu.hlms.oauth.wx.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.tiansu.hlms.oauth.wx.service.RegisterService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author shufq
 * @description
 * @date 2018/3/21 10:14
 */
@Service
public class InterfacesService {

    private Logger logger = LoggerFactory.getLogger(InterfacesService.class);

    @Autowired
    private RestTemplate restTemplate;

    //主要调用里面的根据token获取字段权限的接口
    @Autowired
    private RegisterService registerService;

    //鉴权token：主要调用里面的鉴别token是否合法的接口
    @Autowired
    private OauthService oauthService;

    //获取tenant模块的地址
    @Value("${tenant.uri}")
    private String tenantUri;

    //数据同步结果回调
    @Value("${tenant.collectors}")
    private String collectors;

    //tenant模块的获取部门数据接口路由
    @Value("${tenant.department}")
    private String department;

    //tenant模块的获取部门数据接口路由v1.02
    @Value("${tenant.departments}")
    private String departments;

    //tenant模块的获取部门用户数据接口路由
    @Value("${tenant.user}")
    private String user;

    //tenant模块的获取部门用户数据接口路由v1.02
    @Value("${tenant.users}")
    private String users;

    //tenant模块的获取空间信息列表数据接口路由v1.02
    @Value("${tenant.location}")
    private String location;

    //tenant模块的人员管理注册的接口路由
    @Value("${tenant.permission}")
    private String permission;

    /**
     * @author shufq
     * @description 调用租户服务中的接口获取部门列表
     * @date 2018/3/21 11:34
     * @param stringParam 参数
     * @return
     */
    public List<Map> getDepartment(String stringParam) {
        String departmentUrl = tenantUri + department +stringParam;
        logger.info("-----调用租户服务中的接口获取部门列表{}",departmentUrl);
        Map map = new HashMap();
        Result result = RestUtil.getForEntity(departmentUrl,map,Result.class);
        logger.info("-----调用租户服务中的接口获取部门列表返回值{}",JSON.toJSONString(result));
        return (List)result.getData();
    }


    /**
     * @author shufq
     * @description 调用租户服务中的接口获取部门成员列表
     * @date 2018/3/21 11:34
     * @param stringParam 参数
     * @return
     */
    public List<Map> getUesr(String stringParam) {
        String departmentUrl = tenantUri + user +stringParam;
        logger.info("-----调用租户服务中的接口获取部门成员列表{}",departmentUrl);
        Map map = new HashMap();
        Result result = RestUtil.getForEntity(departmentUrl,map,Result.class);
        logger.info("-----调用租户服务中的接口获取部门成员列表{}",JSON.toJSONString(result));
        return (List)result.getData();
    }

    /**
     * @author shufq
     * @description 获取部门列表
     * @date 2018/3/21 11:34
     * @param id 部门id,获取部门及其下的子部门，不填则全量获取
     * @param access_token 鉴权token
     * @return
     */
    public Map getdepartment(String id, String access_token) {
        logger.info("-----begin /cgi-bin/department/list/v1.01");
        logger.info("-----数据同步：获取部门列表");
        logger.info("-----参数部门id:{},access_token:{}",id,access_token);
        Result res = oauthService.checkToken(access_token);
        Map result = new HashMap();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/department/list/v1.01 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        Map param = (Map)res.getData();

        /*
        拼接参数
         */
        String stringParam ="?corp_id="+param.get("corp_id");
        if(id!=null){
            stringParam+="&id="+id;
        }
        //调用租户服务中的接口获取部门列表
        List<Map> department = getDepartment(stringParam);

        if(department.size()==0){
            result.put("department","暂无数据！");
        }else {
            result.put("department",department);
        }
        result.put("errcode","0");
        result.put("errmsg","ok");

        logger.info("-----end /cgi-bin/department/list/v1.01 返回结果:{}", JSON.toJSONString(result));
        return result;
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
    public Map getUser(String department_id, String access_token, String fetch_child) {
        logger.info("-----begin /cgi-bin/user/simplelist/v1.01");
        logger.info("-----数据同步：获取部门列表");
        logger.info("-----参数部门id:{},access_token:{},fetch_child{}",department_id,access_token,fetch_child);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        Map result = new HashMap();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/user/simplelist/v1.01 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        Map param = (Map)res.getData();

        /*
        拼接参数
         */
        String stringParam ="?corp_id="+param.get("corp_id");
        if(department_id!=null){
            stringParam+="&department_id="+department_id;
        }
        if(fetch_child.equals("1")||fetch_child.equals("0")){
            stringParam+="&fetch_child="+fetch_child;
        }else {
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg","不合法的fetch_child！");
            return result;
        }
        //调用租户服务中的接口获取部门成员列表
        List<Map> department = getUesr(stringParam);

        if(department.size()==0){
            result.put("userlist","暂无数据！");
        }else {
            result.put("userlist",department);
        }
        result.put("errcode","0");
        result.put("errmsg","ok");

        logger.info("-----end /cgi-bin/user/simplelist/v1.01 返回结果:{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @author shufq
     * @description 菜单按钮注册
     * @date 2018/3/29 10:34
     * @param permissions 注册数据
     * @return
     */
    public Result permission(String permissions) {
        String departmentUrl = tenantUri + permission;
        Result result = RestUtil.postForEntity(departmentUrl,permissions,null,Result.class);
        return result;
    }

    /**
     * @author shufq
     * @description 获取增量数据
     * @date 2018/5/18 10:34
     * @param suiteid 应用id
     * @param corpid 租户id
     * @param datamodule 数据类型（101：部门信息，201：用户信息）
     * @param access_token 鉴权token
     * @return
     */
    public Map getincrementdata(String suiteid, String corpid, String datamodule, String access_token) {

        logger.info("-----begin /cgi-bin/basedata/version/getincrementdata/v1.02");
        logger.info("-----数据同步：服务商获取平台增量数据");
        logger.info("-----参数应用id:{},租户id：{},数据类型{},access_token:{}",suiteid,corpid,datamodule,access_token);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        Map result = new HashMap();
        Map param = (Map)res.getData();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()||param.get("corp_id")!=corpid||param.get("suite_id")!=suiteid){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/user/simplelist/v1.01 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        //调用其他服务的接口获取增量数据
        Map params = new HashMap();
        try {
            params.put("tenant_id",corpid);
            params.put("suite_id",suiteid);
            params.put("table_name",getTablename(datamodule));
            res = restTemplate.postForObject(tenantUri,params,Result.class);
            logger.info("-----调用获取增量数据的接口 返回结果：{}",JSON.toJSONString(res));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("errcode",OauthResultCode.UNKNOW_ERROR.getCode());
            result.put("errmsg","参数有误或者系统繁忙");
            logger.info("-----end /cgi-bin/user/simplelist/v1.01 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        if(!res.getErrcode().equals("0")){
            result.put("errcode",0);
            result.put("errmsg",res.getErrmsg());
            return result;
        }

        //调获取增量数据接口后返回的data
        Map resData = (Map) res.getData();
        //data中的所有变化的数据
        List<Map> changeData = (List) resData.get("change_data");
        //特定数据类型的变化数据
        Map changDataMap = new HashMap();

        //遍历变化的所有数据，获取特定数据类型的变化数据
        for(Map map:changeData){
            if(map.get("table_name").equals(getTablename(datamodule))&&map.get("tenant_id").equals(corpid)){
                changDataMap = map;
            }
        }

        //返回给第三方的新增数据
        List<Map> addlist = new ArrayList<>();

        if(changDataMap.containsKey("added")) {
            for (Map map : (List<Map>) changDataMap.get("added")) {
                Map addmap = (Map) map.get("row_data");
                addmap.put("index", map.get("id"));
                addlist.add(addmap);
            }
        }

        //返回给第三方的修改数据
        List<Map> updatelist = new ArrayList<>();

        if(changDataMap.containsKey("updated")) {
            for (Map map : (List<Map>) changDataMap.get("updated")) {
                Map updatedmap = (Map) map.get("row_data");
                updatedmap.put("index", map.get("id"));
                addlist.add(updatedmap);
            }
        }

        //返回给第三方的删除数据
        List<Map> deletelist = new ArrayList<>();


        result.put("errcode",0);
        result.put("errmsg","ok");
        result.put("indexs",resData.get("change_ids"));

        result.put("add",addlist);
        result.put("update",updatelist);
        result.put("delete",deletelist);
        result.put("datamodule",datamodule);

        return result;
    }

    /**
     * @author shufq
     * @description 获取部门列表v1.02
     * @date 2018/5/19 10:34
     * @param access_token 鉴权token
     * @return
     */
    public Map getdepartments(String access_token) {
        logger.info("-----begin /cgi-bin/department/list/v1.02");
        logger.info("-----数据同步：获取部门列表v1.02");
        logger.info("-----参数access_token:{}",access_token);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        Map result = new HashMap();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/department/list/v1.02 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        Map param = (Map)res.getData();

        String departmentUrl = tenantUri + departments +"?corp_id="+param.get("corp_id");
        logger.info("-----调用租户服务中的接口获取部门列表{}",departmentUrl);
        Map map = new HashMap();
        Result resultUtil = RestUtil.getForEntity(departmentUrl,map,Result.class);
        logger.info("-----调用租户服务中的接口获取部门列表返回值{}",JSON.toJSONString(result));
        Map resmap = new HashMap();

        List departmentlist= (List)resultUtil.getData();
        if(departmentlist.size()==0){
            resmap.put("department","暂无数据！");
        }else {
            resmap.put("department",departmentlist);
        }
        resmap.put("errcode","0");
        resmap.put("errmsg","ok");
        resmap.put("dataversion",System.currentTimeMillis());

        logger.info("-----end /cgi-bin/department/list/v1.02 返回结果:{}", JSON.toJSONString(resmap));
        return resmap;
    }

    /**
     * @author shufq
     * @description 获取部门成员v1.02
     * @date 2018/5/19 10:34
     * @param access_token 鉴权token
     * @return
     */
    public Map getUsers(String access_token) {
        logger.info("-----begin /cgi-bin/user/simplelist/v1.02");
        logger.info("-----数据同步：获取部门成员v1.02");
        logger.info("-----参数access_token:{}",access_token);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        Map result = new HashMap();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/user/simplelist/v1.02 返回结果:{}", JSON.toJSONString(result));
            return result;
        }

        Map param = (Map)res.getData();

        String departmentUrl = tenantUri + users +"?corp_id="+param.get("corp_id");
        logger.info("-----调用租户服务中的接口获取部门成员{}",departmentUrl);
        Map map = new HashMap();
        Result resultUtil = RestUtil.getForEntity(departmentUrl,map,Result.class);
        logger.info("-----调用租户服务中的接口获取部门成员返回值{}",JSON.toJSONString(result));
        Map resmap = new HashMap();

        List userlist= (List)resultUtil.getData();
        if(userlist.size()==0){
            resmap.put("userlist","暂无数据！");
        }else {
            resmap.put("userlist",userlist);
        }
        resmap.put("errcode","0");
        resmap.put("errmsg","ok");
        resmap.put("dataversion",System.currentTimeMillis());

        logger.info("-----end /cgi-bin/user/simplelist/v1.01 返回结果:{}", JSON.toJSONString(resmap));
        return resmap;
    }

    /**
     * @author shufq
     * @description 全量同步结果回调
     * @date 2018/5/17 10:34
     * @param body 请求包体
     * @param access_token 鉴权token
     * @return
     */
    public Result pushresult(JSONObject body, String access_token) {
        logger.info("-----begin /cgi-bin/basedata/version/pushresult/v1.02");
        logger.info("-----数据同步：全量同步结果回调v1.02");
        logger.info("-----参数access_token:{}",access_token);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            logger.info("-----end /cgi-bin/basedata/version/pushresult/v1.02 返回结果:{}",
                    JSON.toJSONString(ResultUtil.error(OauthResultCode.INVALID_ACCESS_TOKEN.getCode(),OauthResultCode.INVALID_ACCESS_TOKEN.getMsg())));
            return ResultUtil.error(OauthResultCode.INVALID_ACCESS_TOKEN.getCode(),OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
        }

        return ResultUtil.success();

//        Map params = new HashMap();
//
//        try {
//            params.put("change_time",stampToDate(body.get("dataversion").toString()));
//            params.put("tenant_id",body.get("corpid"));
//            params.put("suite_id",body.get("suiteid"));
//            params.put("table_name",getTablename(body.get("datamodule")));
//            params.put("sync_time",stampToDate(System.currentTimeMillis()+""));
//            Result re = restTemplate.postForObject(tenantUri+collectors,params,Result.class);
//            logger.info("-----end /cgi-bin/basedata/version/pushresult/v1.02 返回结果:{}",JSON.toJSONString(re));
//            return re;
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("-----end /cgi-bin/basedata/version/pushresult/v1.02 返回结果:{}",
//                    ResultUtil.error(OauthResultCode.UNKNOW_ERROR.getCode(),"参数有误或者网络繁忙！"));
//            return ResultUtil.error(OauthResultCode.UNKNOW_ERROR.getCode(),"参数有误或者网络繁忙！");
//        }

    }

    /**
     * @author shufq
     * @description 增量同步结果回调
     * @date 2018/5/17 10:34
     * @param body 请求包体
     * @param access_token 鉴权token
     * @return
     */
    public Result postincrementdataresult(JSONObject body, String access_token) {
        logger.info("-----begin /cgi-bin/basedata/postincrementdataresult/v1.02");
        logger.info("-----数据同步：增量同步结果回调v1.02");
        logger.info("-----参数access_token:{}",access_token);
        Result res = oauthService.checkToken(access_token);

        /*
        token不合法
         */
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            logger.info("-----end /cgi-bin/basedata/postincrementdataresult/v1.02 返回结果:{}",
                    JSON.toJSONString(ResultUtil.error(OauthResultCode.INVALID_ACCESS_TOKEN.getCode(),OauthResultCode.INVALID_ACCESS_TOKEN.getMsg())));
            return ResultUtil.error(OauthResultCode.INVALID_ACCESS_TOKEN.getCode(),OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
        }

        Map params = new HashMap();

        try {
            params.put("change_ids",body.get("indexs"));
            params.put("suite_id",body.get("suiteid"));
            params.put("table_name",getTablename(body.get("datamodule")));
            params.put("sync_time",stampToDate(System.currentTimeMillis()+""));
//            Result re = RestUtil.postForEntity(tenantUri,params,null,null,Result.class);
            Result re = restTemplate.postForObject(tenantUri+collectors,params,Result.class);
            logger.info("-----end /cgi-bin/basedata/postincrementdataresult/v1.02 返回结果:{}",JSON.toJSONString(re));
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(OauthResultCode.UNKNOW_ERROR.getCode(),"参数有误或者网络繁忙！");
        }
    }

    /**
     * @author chenrl
     * @description 获取空间信息列表
     * @date 2018/5/17 10:34
     * @param access_token 鉴权token
     * @return
     */
    public Map getLocationList(String access_token) {
        logger.info("-----begin /cgi-bin/location/simplelist/v1.02");
        logger.info("-----数据同步：获取空间信息列表v1.02");
        logger.info("-----参数access_token:{}",access_token);
        Result res = oauthService.checkToken(access_token);
        //校验token是否合法
        Map result = new HashMap();
        if(res.getErrcode()==OauthResultCode.INVALID_ACCESS_TOKEN.getCode()){
            result.put("errcode",OauthResultCode.INVALID_ACCESS_TOKEN.getCode());
            result.put("errmsg",OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
            logger.info("-----end /cgi-bin/location/simplelist/v1.02 返回结果:{}", result.toString());
            return result;
        }
        //获取根据access_token查询得到的结果集
        Map param = (Map)res.getData();
        //拼接调用查询空间信息列表的url
        String locationUrl = tenantUri + location +"?tenantId="+param.get("corp_id");
        logger.info("-----调用租户服务中的接口获取空间信息列表url",locationUrl);
        try
        {
            //调用空间信息列表查询接口
            Map map = new HashMap();
            Result resultDate = RestUtil.getForEntity(locationUrl,map,Result.class);
            logger.info("-----调用租户服务中的接口获取空间信息列表返回值{}",resultDate.toString());
            Map resmap = covLocationInfo(resultDate);
            logger.info("-----end /cgi-bin/location/simplelist/v1.02 返回结果:{}", resmap.toString());
            return resmap;
        }
        catch(Exception e)
        {
            Map resMap = new HashMap();
            resMap.put("errcode",OauthResultCode.UNKNOW_ERROR.getCode());
            resMap.put("errmsg",OauthResultCode.UNKNOW_ERROR.getMsg());
            logger.info("-----end /cgi-bin/location/simplelist/v1.02 exception is:", e.toString());
            return resMap;
        }
    }


    /**
     * @param result
     * @return Map
     * @creator chenruilong
     * @createtime 2018/5/24 19:12
     * @description: 将查询结果进行封装返回
     */
    public Map covLocationInfo(Result result)
    {
        //将获取到的查询结果转换成为LocationInfo列表
        List<Map> locationList= (List<Map>)result.getData();
        //将空间信息详细列表对象封装为空间信息简单列表对象
        List<SpaceInfo> spaceInfos = new ArrayList<SpaceInfo>();
        for(Map locationInfo : locationList)
        {
            SpaceInfo spaceInfo = new SpaceInfo();
            spaceInfo.setId((Long) locationInfo.get("id"));
            if(locationInfo.get("locCode") == null)
            {
                spaceInfo.setCode("");
            }
            else
            {
                spaceInfo.setCode(String.valueOf(locationInfo.get("locCode")));
            }

            //是否有效：0有效；1无效
            String disable = String.valueOf(locationInfo.get("locStatus")).equals("1") ? "0":"1";
            spaceInfo.setDisable(disable);

            spaceInfo.setLevel((Integer)locationInfo.get("locLevel"));
            spaceInfo.setName(String.valueOf(locationInfo.get("locName")));
            spaceInfo.setParentid((Long) locationInfo.get("parentId"));
            spaceInfo.setLocType(String.valueOf(locationInfo.get("locType")));
            spaceInfo.setLevelCode(String.valueOf(locationInfo.get("levelCode")));
            spaceInfos.add(spaceInfo);
        }
        //定义返回对象
        Map resmap = new HashMap();
        if(CollectionUtils.isEmpty(spaceInfos)){
            resmap.put("locations","暂无数据！");
        }else {
            //String jsonStr = JSON.toJSONString(spaceInfos);
            resmap.put("locations",spaceInfos);
        }
        resmap.put("errcode","0");
        resmap.put("errmsg","ok");
        resmap.put("dataversion",System.currentTimeMillis());
        return resmap;
    }

    //时间戳转换为时间
    public String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    //根据数据类型获取表名
    public String getTablename(Object datamodule){
        Map moduleToTableName = new HashMap();
        moduleToTableName.put("101","hlms_tenant_dept");
        moduleToTableName.put("201","hlms_tenant_user");
        return moduleToTableName.get(datamodule).toString();
    }


}
