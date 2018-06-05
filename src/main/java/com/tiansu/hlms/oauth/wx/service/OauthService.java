package com.tiansu.hlms.oauth.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.common.ResultUtil;
import com.tiansu.hlms.oauth.constant.OauthResultCode;
import com.tiansu.hlms.oauth.utils.EncryptUtil;
import com.tiansu.hlms.oauth.utils.RandomCharsGenerator;
import com.tiansu.hlms.oauth.wx.bean.*;
import com.tiansu.hlms.oauth.wx.dao.OauthDao;
import com.tiansu.hlms.oauth.wx.dao.ServiceProviderDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 王瑞
 * @description 鉴权参数业务类
 * @create 2018-02-26 上午 11:33
 * @modifier 王瑞
 * @modifytime 2018/4/9 21:06
 * @modifyDec: oauth页面登录免鉴权
 */
@Service
public class OauthService {

    private Logger logger = LoggerFactory.getLogger(OauthService.class);

    /*
     * 鉴权持久化操作
     */
    @Autowired
    private OauthDao oauthDao;

    /*
     * 第三方接口dao
     */
    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private RestTemplate restTemplate;

    //获取配置文件中租户接口服务地址
    @Value("${tenant.uri}")
    private String tenantUri;

    //获取配置文件中根据token获取用户userId接口路由
    @Value("${tenant.userIdByToken}")
    private String userIdByToken;

    //获取suite模块的地址
    @Value("${suite.uri}")
    private String suiteUri;

    //suite模块的根据应用id和租户id获取应用详情接口路由
    @Value("${suite.suiteConfigByTenantId}")
    private String providerApiByTenantId;

    //获取配置文件中根据应用id获取应用详情接口路由
    @Value("${suite.suiteInfoById}")
    private String suiteInfoById;

    @Value("${oauth.synchroOauthParams}")
    private String synchroOauthParams;

    @Value("${suite.synchroSuiteParams}")
    private String synchroSuiteParams;

    /**
     * @param tenantId    租户id
     * @param tenantDeploy 是否是SaaS租户
     * @param tenantUri 本地部署的租户uri
     * @param tenantSuite 租户应用
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/5 20:14
     * @description: 新增鉴权参数
     */
    public Result createOauthParams(String tenantId, String tenantDeploy, String tenantUri, String tenantSuite) {
        logger.info("------ begin /hlms/oauth/model/create_oauth_params ");
        logger.info("------ 参数*** tenantId:{},tenantSuite:{}",tenantId,tenantSuite);

        //参数非空判断
        if(StringUtils.isBlank(tenantId) || StringUtils.isBlank(tenantSuite)){
            logger.info("------ end /hlms/oauth/model");
            return ResultUtil.error(OauthResultCode.NULL_PARAM.getCode(),OauthResultCode.NULL_PARAM.getMsg());
        }

        //字符json转化为JSONArray集合
        JSONArray array = JSONObject.parseArray(tenantSuite);

        //通知租户开户失败集合
        List list = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            try {
                //非系统级的第三方应用不进行新增鉴权参数
                String suiteType = array.getJSONObject(i).getString("suiteType");
                Integer source = array.getJSONObject(i).getInteger("source");
                if( !"1".equals(suiteType) || source == null || source != 2 ){
                    continue;
                }

                //获取应用id
                String suiteId = array.getJSONObject(i).getString("id");
                String suiteTicket = "";

                //该应用是否生成suite_ticket
                OauthSuite oauthSuite = getOauthSuite(suiteId);
                if(oauthSuite == null ) {
                    logger.debug(OauthResultCode.INVALID_SUITE_ID.getMsg());
                    continue;
                } else if(StringUtils.isNotBlank(oauthSuite.getSuiteTicket())) {
                    suiteTicket = oauthSuite.getSuiteTicket();
                } else {
                    //更新鉴权应用
                    suiteTicket = updateOauthSuite(suiteId);
                }
                //新增鉴权参数 获取返回的预授权码
                OauthParams oauthParams = insertOauthParam(tenantId,suiteId);

                //本地部署的应用开始同步本地平台数据
                if("2".equals(tenantDeploy)){

                    //调用云端suite获取数据 同步至本地化suite表
                    startSynchroSuiteParams(tenantUri);

                    //同步oauth模块数据
                    startSynchroOauthParams(suiteId, tenantUri, oauthParams);
                }

                //根据应用id获取第三方uri
                String uri = getProviderApi(suiteId, tenantId);
                String token = "";
                if(StringUtils.isNotBlank(oauthSuite.getSuiteToken())){
                    token = oauthSuite.getSuiteToken();
                }

                //通知第三方服务开户
                corpAccess(token, suiteTicket, tenantId, suiteId, uri, oauthParams.getPreAuthCode());

            } catch (Exception e) {
                list.add(array.getJSONObject(i).getString("id"));
                logger.error("通知租户开户出错！{},suiteId:{},tenantId:{}", e, array.getJSONObject(i).getString("id"), tenantId);
            }
        }
        if(list.size() > 0){
            logger.info("失败的tenantId:{},suiteId:{}",tenantId, JSON.toJSONString(list));
        }
        logger.info("------ end /hlms/oauth/model");
        return ResultUtil.success(list);
    }

    /**
     * @param token 第三方鉴权用token
     * @param suiteTicket 定时推送ticket所用
     * @param tenantId 租户id
     * @param suiteId 应用id
     * @param uri 第三方服务的uri
     *
     * @creator 王瑞
     * @createtime 2018/4/23 16:12
     * @description: 通知第三方服务开户
     */
    public void corpAccess(String token, String suiteTicket, String tenantId, String suiteId ,String uri, String preAuthCode){
        logger.info("------ 通知第三方服务开户开始");
        //通知第三方服务开户
        //设置请求列表参数
        Map uriParams = new HashMap();
        uriParams.put("token", token);
        //设置请求body参数

        Map bodyParams = new HashMap();
        bodyParams.put("suiteTicket", suiteTicket);
//        bodyParams.put("tenantId", tenantId);
        bodyParams.put("suiteId", suiteId);
        bodyParams.put("preAuthCode",preAuthCode);
        bodyParams.put("timeStamp", System.currentTimeMillis());

        logger.info("------ 参数 restService:{},suiteTicket:{},tenantId:{},suiteId:{},token:{},preAuthCode:{}",
                uri, suiteTicket, tenantId, suiteId, token, preAuthCode);
        String resultStr = restTemplate.postForObject(uri, bodyParams, String.class, uriParams);
        logger.info("------ 通知第三方服务开户结束 返回结果：{}",resultStr);
    }

    /**
     * @param suiteId 应用id
     * @return 第三方用于接收开户回调通知的uri
     *
     * @creator 王瑞
     * @createtime 2018/4/23 15:35
     * @description: 获取第三方用于接收开户回调通知的uri
     * <br/>新增鉴权参数-->suite应用注册模块-->根据应用id获取第三方uri
     */
    public String getProviderApi(String suiteId, String tenantId){

        //参数赋值
        Map param = new HashMap<>();
        param.put("suite_id", suiteId);
        param.put("tenant_id" ,tenantId);
        param.put("business", "RestCorpAccess");
        logger.info("------ 从suite应用注册模块获取通知第三方服务开户的uri开始");
        JSONObject json = restTemplate.getForObject(suiteUri + providerApiByTenantId, JSONObject.class, param);
        JSONArray data = json.getJSONArray("data");
        String restService = "";
        if ("0".equals(json.getString("errcode")) && data != null) {
            restService = data.getJSONObject(0).get("restService").toString();
        }
        logger.info("------ 从suite应用注册模块获取通知第三方服务开户的uri结束语 返回结果：{}",json.toJSONString());
        return restService;
    }


    /**
     * @return 所有授权失败的鉴权参数的集合
     *
     * @creator 王瑞
     * @createtime 2018/4/24 16:54
     * @description: 获取所有授权失败的鉴权参数
     */
    public List<Authorized> getAuthorized(){
        logger.info("------ 获取所有授权失败的鉴权参数开始");
        //获取所有授权失败的鉴权参数
        List<Authorized> list = oauthDao.getAuthorized();
        for (int i = 0; i < list.size(); i++){
            try {
                //获取授权失败的第三方授权回调通知uri
                String uri = getProviderApi(list.get(i).getSuiteId(), list.get(i).getCorpId());
                list.get(i).setUri(uri);
            } catch (Exception e){
                logger.error("获取授权失败的第三方授权回调通知的uri失败:{}", e.getMessage());
                logger.debug("失败的tenantId:{},suiteId:{}", list.get(i).getCorpId(), list.get(i).getSuiteId());
            }
        }
        logger.info("------ 获取所有授权失败的鉴权参数结束 返回结果：{}", JSON.toJSONString(list));
        return  list;
    }

    /**
     * @param suiteId 应用id
     * @return 应用鉴权信息
     *
     * @creator 王瑞
     * @createtime 2018/4/23 14:41
     * @description: 根据应用id查询应用鉴权信息
     */
    public OauthSuite getOauthSuite(String suiteId) {
        OauthSuite oauthSuite = serviceProviderDao.getOauthSuiteBySuiteId(suiteId);
        return oauthSuite;
    }

    /**
     * @param suiteId 应用id
     *
     * @creator 王瑞
     * @createtime 2018/4/23 14:27
     * @description:  更新鉴权应用表
     */
    public String updateOauthSuite(String suiteId) {
        logger.info("------ 更新鉴权应用表开始");
        Map param = new HashMap();
        String suiteTicket = RandomCharsGenerator.uuid(16);
        param.put("suiteId", suiteId);
        param.put("suiteTicket", suiteTicket);
        param.put("suiteAccessToken", RandomCharsGenerator.uuid(16));
        serviceProviderDao.updateSuite(param);
        logger.info("------ 更新鉴权应用表结束");
        return suiteTicket;
    }

    /**
     * @param tenantId 租户id
     * @param suiteId 应用id
     *
     * @creator 王瑞
     * @createtime 2018/4/23 14:37
     * @description: 新增鉴权参数
     */
    public OauthParams insertOauthParam(String tenantId, String suiteId){
        logger.info("------ 新增鉴权参数开始：tenantId:{},suiteId:{}", tenantId, suiteId);

        //设置新增鉴权参数
        OauthParams oauthParam = new OauthParams();
        oauthParam.setId(RandomCharsGenerator.idGen());
        oauthParam.setCorpId(tenantId);
        oauthParam.setSuiteId(suiteId);
        oauthParam.setPreAuthCode(RandomCharsGenerator.uuid(16));
        oauthParam.setPreAuthCodeExpireTime(new Date());
        oauthParam.setPermanentCode(RandomCharsGenerator.uuid(16));
        oauthParam.setAccessToken(RandomCharsGenerator.uuid(16));
        oauthParam.setAccessTokenExpireTime(new Date());

        //持久化
        int result = oauthDao.createOauthParams(oauthParam);
        logger.info("------ 新增鉴权参数结束 返回结果：{}", result);
        //是否新增成功
        return oauthParam;
    }

    /**
     * @param corpId      企业的CorpID
     * @param suiteId     企业应用的id
     * @param redirectUri 授权后重定向的回调链接地址，注意域名需要设置为第三方应用的可信域名
     * @param state       重定向后会带上state参数
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/6 上午 10:08
     * @description: 校验corpId, agentId, redirect_uri的合法性
     */
    public Result authorize(String corpId, String suiteId, String redirectUri,
                            String state, HttpServletRequest request) {
        logger.info("------ begin /connect/oauth2/authorize");
        logger.info("------ 参数*** corpId:{},suiteId:{},redirectUri:{},state:{}",
                corpId,suiteId,redirectUri,state);
        Map map = new HashMap();
        Result result = new Result();
        try {

            //获取头部的请求token
            String token = request.getHeader("token");
            logger.info("用户登录token:{}", token);

            //是否获取到登录token
            if(StringUtils.isBlank(token)){
                result.setErrcode(OauthResultCode.NULL_PARAM_TOKEN.getCode());
                result.setErrmsg(OauthResultCode.NULL_PARAM_TOKEN.getMsg());
                return result;
            }

            //处理并返回回调地址
            //处理回调地址中有#的特殊字符进行转译还原
            redirectUri = redirectUri.replace("@", "#");
            if(StringUtils.lastIndexOf(redirectUri,"?")>-1)
                redirectUri += "&state=" + state;
            else
                redirectUri += "?state=" + state;

            //设置查询参数
            map.put("corpId", corpId);
            map.put("suiteId", suiteId);

            //根据应用id 查询应用详情依赖应用注册模块suite的接口
            //依赖关系：网页鉴权功能-->调用suite应用注册模块-->根据应用id获取应用详情
            logger.debug("------ begin 根据应用id获取应用详情");
            JSONObject suiteJson = restTemplate.getForObject(suiteUri + suiteInfoById, JSONObject.class, map);
            logger.debug("------ end 根据应用id获取应用详情 返回结果：{}", suiteJson.toJSONString());
            JSONObject suite = suiteJson.getJSONObject("data");
            //平台内部子系统
            if( "1".equals(suite.getString("iomsSubsystem"))){

                //为eam修改 原来的 code 替换成token 只在这里用
                redirectUri += "&token="+ EncryptUtil.encrypt64(token);
                return ResultUtil.success(redirectUri);

            } else if ("1".equals(suite.getString("source")) ||
                    "1".equals(suite.getString("authorized"))){     //免鉴权判断
                redirectUri += "&code="+ token;
                return ResultUtil.success(redirectUri);
            }

            //根据租户鉴权id 查询鉴权参数
            OauthParams oauthParams = oauthDao.getOauthParam(map);

            //校验corpId
            if (oauthParams == null) {
                return ResultUtil.error(OauthResultCode.INVALID_APPID.getCode(), OauthResultCode.INVALID_APPID.getMsg());
            }
            redirectUri += "&corpid="+corpId+"&code="+ token;

            //第三方服务的token
            String suiteToken = serviceProviderDao.getSuiteTokenBySuiteId(suiteId);
            logger.debug("第三方服务的鉴权token:{}", suiteToken);
            if (StringUtils.isNotBlank(suiteToken)) {
                redirectUri += "&token=" + suiteToken;
            }
            result.setData(redirectUri);
        } catch (Exception e) {
            logger.error("鉴权参数校验业务报错！", e.getMessage());
            result.setErrcode(OauthResultCode.UNKNOW_ERROR.getCode());
            result.setErrmsg(OauthResultCode.UNKNOW_ERROR.getMsg());
        }
        return result;
    }

    /**
     * @param map 参数
     * @return 是否免鉴权 1 是
     *
     * @creator 王瑞
     * @createtime 2018/4/23 18:55
     * @description: 是否免鉴权查询
     */
    public int  isAuthorized(Map map){
        //根据应用id 查询应用详情依赖应用注册模块suite的接口
        //依赖关系：网页鉴权功能-->调用suite应用注册模块-->根据应用id获取应用详情
        logger.debug("------ begin 根据应用id获取应用详情");
        JSONObject suiteJson = restTemplate.getForObject(suiteUri + suiteInfoById, JSONObject.class, map);
        logger.debug("------ end 根据应用id获取应用详情 返回结果：{}", suiteJson.toJSONString());
        JSONObject suite = suiteJson.getJSONObject("data");
        return suite.getIntValue("authorized");
    }

    /**
     * @param accessToken 鉴权token
     * @return 返回结果对象
     *
     * @creator 舒方强
     * @createtime 2018/3/19 09:54
     * @description: 校验token是否合法
     */
    public Result checkToken(String accessToken) {
        Map map = oauthDao.checkToken(accessToken);
        if (map == null) {
            return ResultUtil.error(OauthResultCode.INVALID_ACCESS_TOKEN.getCode(), OauthResultCode.INVALID_ACCESS_TOKEN.getMsg());
        } else {
            return ResultUtil.success(map);
        }
    }

    /**
     * @param suiteSecret 应用秘钥
     * @return 返回结果对象
     *
     * @creator 舒方强
     * @createtime 2018/3/31 09:54
     * @description: 校验应用秘钥是否合法
     */
    public Result checkSuiteSecret(String suiteSecret) {
        Map map = serviceProviderDao.checkSuiteSecret(suiteSecret);
        if (map == null) {
            return ResultUtil.error("300580", "不合法的suite_secret");
        } else {
            return ResultUtil.success();
        }
    }

    /**
     * @param suiteId 应用id
     * @return 服务商提供的token
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 根据应用id获取服务商提供的token
     */
    public Result getSuiteTokenBySiteId(String suiteId) {
        String suiteToken = serviceProviderDao.getSuiteTokenBySuiteId(suiteId);
        if (StringUtils.isBlank(suiteToken)) {
            return ResultUtil.error(OauthResultCode.INVALID_SUITE_ID.getCode(), OauthResultCode.INVALID_SUITE_ID.getMsg());
        }
        return ResultUtil.success(suiteToken);
    }

    /**
     * @param versions 版本
     * @return 应用id
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 封装返回要推送的数据版本号信息
     */
    public Result getSuiteIdByCorpId(List<Version> versions) {
        //获取要推送的数据版本号信息
        List<Version> newVersion = new LinkedList<>();
        for (int i = 0; i < versions.size(); i++) {

            //获取数据版本信息缺少的应用id和token
            List<Version> versionsList = oauthDao.getSuiteIdByCorpId(versions.get(i).getTenantId());
            if (versionsList != null) {
                for (int j = 0; j < versionsList.size(); j++) {
                    versionsList.get(j).setTenantId(versions.get(i).getTenantId());
                    versionsList.get(j).setDataModule(versions.get(i).getDataModule());
                    versionsList.get(j).setSynchroVersion(versions.get(i).getSynchroVersion());
                }
                //整合新的集合
                newVersion.addAll(versionsList);
            }
        }
        return ResultUtil.success(newVersion);
    }

    /**
     * @param oauthParams 鉴权参数
     * @param tenantUri   本地部署租户的uri
     * @param oauthParams 鉴权应用
     *
     * @creator 王瑞
     * @createtime 2018/4/27 16:04
     * @description: 调用同步鉴权参数接口
     */
    public void startSynchroOauthParams(String suiteId, String tenantUri, OauthParams oauthParams){
        try {
            logger.info("------ 调用同步鉴权参数接口开始");
            //根据应用id获取应用详情
            OauthSuite newOauthSuite = serviceProviderDao.getOauthSuiteBySuiteId(suiteId);

            //参数赋值
            SynchroOauthParams synchroOauthParamsEntity = new SynchroOauthParams();
            synchroOauthParamsEntity.setOauthParams(oauthParams);
            synchroOauthParamsEntity.setOauthSuite(newOauthSuite);

            String uri = synchroOauthParams.replace("{domain}", tenantUri);
            //开始通用接口
            logger.info("------ 调用同步鉴权参数接口 uri:{}", uri);
            String result = restTemplate.postForObject(uri, synchroOauthParamsEntity, String.class);
            logger.info("------ 调用同步鉴权参数接口结束 返回结果：{}", result);
        } catch (Exception e){
            logger.error("oauth模块云端本地数据同步出错：{}", e.getMessage());
        }
    }

    /**
     * @param tenantUri 本地部署租户的uri
     *
     * @creator 陳瑞龍
     * @createtime 2018/4/27 16:04
     * @description: 调用云端suite获取数据 同步至本地化suite表
     */
    public void startSynchroSuiteParams(String tenantUri){
        try {
            logger.info("------ 调用云端suite获取数据接口开始 uri:{}", suiteUri + synchroSuiteParams);
            String result = restTemplate.postForObject(suiteUri + synchroSuiteParams + "?tenantUri=" + tenantUri, null, String.class);
            logger.info("------ 调用云端suite获取数据接口结束 返 回结果：{}", result);
        } catch (Exception e){
            logger.error("suite模块云端本地数据同步出错：{}", e.getMessage());
        }
    }

    /**
     * @param synchroOauthParams 鉴权参数
     * @return 同步结果
     *
     * @creator 王瑞
     * @createtime 2018/4/27 15:59
     * @description: 同步鉴权参数
     */
    public Result synchroOauthParams(SynchroOauthParams synchroOauthParams){
        logger.info("------ begin 本地环境同步鉴权参数 参数:{}", JSON.toJSONString(synchroOauthParams));
        Result result = new Result();
        OauthParams oauthParams = synchroOauthParams.getOauthParams();
        OauthSuite oauthSuite = synchroOauthParams.getOauthSuite();

        try {
            //查询本地环境是否已经有了鉴权参数
            Map param = new HashMap();
            param.put("corpId", oauthParams.getCorpId());
            param.put("suiteId", oauthParams.getSuiteId());
            OauthParams newOauthParams = oauthDao.getOauthParam(param);
            if(newOauthParams == null){
                oauthDao.createOauthParams(oauthParams);
            }

            //查询本地环境是否已经有了鉴权应用信息
            OauthSuite newOauthSuite = serviceProviderDao.getOauthSuiteBySuiteId(oauthSuite.getSuiteId().toString());
            if(newOauthSuite == null){
                serviceProviderDao.createSuite(oauthSuite);
            }
        } catch (Exception e){
            logger.error("------ 同步失败:{}", e.getMessage());
            result.setErrcode(OauthResultCode.UNKNOW_ERROR.getCode());
            result.setErrmsg(OauthResultCode.UNKNOW_ERROR.getMsg());
        }
        logger.info("------ end 本地环境同步鉴权参数");
        return result;
    }

}