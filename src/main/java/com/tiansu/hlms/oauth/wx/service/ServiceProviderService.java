package com.tiansu.hlms.oauth.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.constant.WeChatResultCode;
import com.tiansu.hlms.oauth.utils.DESUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王瑞
 * @description 第三方对接业务实现
 * @date 2018-03-06 16:16
 */
@Service
public class ServiceProviderService {
    private Logger logger = LoggerFactory.getLogger(ServiceProviderService.class);

    /*鉴权参数dao*/
    @Autowired
    private OauthDao oauthDao;

    /*
    * 第三方对接业务dao
    */
    @Autowired
    private ServiceProviderDao serviceProviderDao;

    @Autowired
    private RestTemplate restTemplate;

    //获取配置文件中租户接口服务地址
    @Value("${tenant.uri}")
    private String tenantUri;

    //获取配置文件中租户接口服务地址
    @Value("${suite.uri}")
    private String suiteUri;

    //获取配置文件中系统接口服务地址
    @Value("${system.uri}")
    private String systemUri;

    //获取配置文件中根据token获取用户userId接口路由
    @Value("${tenant.userIdByToken}")
    private String userIdByToken;

    //获取配置文件中根据应用id获取应用详情接口路由
    @Value("${suite.suiteById}")
    private String suiteById;

    //获取配置文件中根据租户id获取租户详情接口路由
    @Value("${system.tenantById}")
    private String tenantById;

    //获取配置文件中租户服务登陆验证接口路由地址
    @Value("${suite.checkLogin4AppApi}")
    private String checkLogin4AppApi;

    /**
     * @param suiteAccessTokenToken 用于接收获取第三方应用凭证接口的包体的参数
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/12 15:34
     * @description: 该API用于获取第三方应用凭证（suite_access_token）。
     */
    public Map getSuiteToken(SuiteAccessTokenToken suiteAccessTokenToken) {
        logger.info("------ begin  /cgi-bin/service/get_suite_token");
        logger.info("------ 参数*** suite_id:{},suite_secret:{},suite_ticket:{}",
                suiteAccessTokenToken.getSuite_id(), suiteAccessTokenToken.getSuite_secret(), suiteAccessTokenToken.getSuite_ticket());

        //校验参数是否是空
        if (checkObjFieldIsNotNull(suiteAccessTokenToken)) {
            return resultFailMap(WeChatResultCode.NULL_PARAM.getCode(), WeChatResultCode.NULL_PARAM.getMsg());
        }

        //根据suite_id查询鉴权应用信息
        OauthSuite oauthSuite = serviceProviderDao.getOauthSuiteBySuiteId(suiteAccessTokenToken.getSuite_id());

        //校验该应用是否正确
        if (oauthSuite == null || StringUtils.isBlank(oauthSuite.getSuiteTicket())) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_ID.getCode(), WeChatResultCode.INVALID_SUITE_ID.getMsg());
        }

        //该应用的秘钥是否正确
        if (!suiteAccessTokenToken.getSuite_secret().equals(oauthSuite.getSuiteSecret())) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_SECRET.getCode(), WeChatResultCode.INVALID_SUITE_SECRET.getMsg());
        }

        //校验该应用的suite_ticket是否正确
        if (!suiteAccessTokenToken.getSuite_ticket().equals(oauthSuite.getSuiteTicket())) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_TICEKT.getCode(), WeChatResultCode.INVALID_SUITE_TICEKT.getMsg());
        }

        //设置返回值
        Map map = new HashMap();
        try {
            //获取第三方应用凭证(suiteAccessToken)
            String suiteAccessToken = oauthSuite.getSuiteAccessToken();

            //第三方应用凭证(suiteAccessToken)是否生成
            if (StringUtils.isBlank(suiteAccessToken)) {
                //生成第三方应用凭证(suiteAccessToken)并入库
                Map updateMap = new HashMap();
                suiteAccessToken = RandomCharsGenerator.uuid(16);
                updateMap.put("suiteId", oauthSuite.getSuiteId());
                updateMap.put("suitAccessToken", suiteAccessToken);
                oauthDao.updateOauthParams(updateMap);
            }

            resultSuccessMap(map);
            map.put("suite_access_token", suiteAccessToken);
            map.put("expires_in", 7200);
        } catch (Exception e) {
            logger.error("更新数据库出错！{}", e.getMessage());
            resultFailMap(map);
        }
        logger.info("------ end /cgi-bin/service/get_suite_token 返回结果：{}", JSON.toJSONString(map));
        return map;
    }

    /**
     * @param suiteAccessToken 第三方应用access_token,最长为512字节
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/12 16:31
     * @description: 服务商根据suite_access_token获取预授权码--该接口已废弃
     */
    public Map getPreAuthCode(String suiteAccessToken) {
        logger.info("------ begin  /cgi-bin/service/get_pre_auth_code");
        logger.info("------ 参数***  suite_access_token:{}",suiteAccessToken);
        //设置返回值
        Map map = new HashMap();
        resultSuccessMap(map);
        map.put("pre_auth_code", RandomCharsGenerator.uuid(16));
        map.put("expires_in", 1800);
        logger.info("------ end /cgi-bin/service/get_pre_auth_code 返回结果：{}", JSON.toJSONString(map));
        return map;
    }

    /**
     * @param suiteAccessToken 第三方应用access_token,最长为512字节
     * @param preAuthCode        请求包体{ "auth_code": "临时授权码" }
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/2/28 0028 上午 10:47
     * @description: 该API用于使用临时授权码换取授权方的永久授权码，并换取授权信息、企业access_token，
     * 临时授权码一次有效。建议第三方优先以userid或手机号为主键、其次以email为主键来建立自己的管理员账号。
     */
    @Transactional
    public Map getPermanentCode(String suiteAccessToken, PreAuthCode preAuthCode) {
        logger.info("------ begin  /cgi-bin/service/get_permanent_code");
        logger.info("------ 参数**** suite_access_token:{},auth_code:{}",suiteAccessToken,preAuthCode.getAuth_code());

        //校验参数是否为空
        if (StringUtils.isBlank(suiteAccessToken) || checkObjFieldIsNotNull(preAuthCode)) {
            return resultFailMap(WeChatResultCode.NULL_PARAM.getCode(),WeChatResultCode.NULL_PARAM.getMsg());
        }

        //校验 临时授权码
        OauthParams oauthParams = oauthDao.getOauthParamByPreAuthCode(preAuthCode.getAuth_code());
        if (oauthParams == null || StringUtils.isBlank(oauthParams.getSuiteId())) {
            return resultFailMap(WeChatResultCode.INVALID_AUTH_CODE.getCode(),WeChatResultCode.INVALID_AUTH_CODE.getMsg());
        }

        //校验 suite_access_token
        String suiteId = serviceProviderDao.getSuiteIdBySuiteAccessToken(suiteAccessToken);
        if (StringUtils.isBlank(suiteId) || !suiteId.equals(oauthParams.getSuiteId())) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getCode(),WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getMsg());
        }

        //设置该方法返回的值
        Map map = new HashMap();

        try {
            //根据租户id获取租户名称
            Map tenantName = getCorpInfo(oauthParams.getCorpId());

            //获取应用名称
            JSONObject suite = getSuiteInfo(oauthParams.getCorpId(),oauthParams.getSuiteId());

            //重新生成预授权码
            Map preAuthCodeMap = new HashMap();
            preAuthCodeMap.put("id", oauthParams.getId());
            preAuthCodeMap.put("preAuthCode", RandomCharsGenerator.uuid(16));
            preAuthCodeMap.put("preAuthCodeExpireTime", new Date());
            preAuthCodeMap.put("isAuthorized", 1);
            oauthDao.updateOauthParams(preAuthCodeMap);

            String accessToken = oauthParams.getAccessToken();
            String permanentCode = oauthParams.getPermanentCode();

            //设置永久授权码 和 access_token
            if (StringUtils.isBlank(permanentCode) && StringUtils.isBlank(accessToken)) {

                permanentCode = RandomCharsGenerator.uuid(16);
                accessToken = RandomCharsGenerator.uuid(16);

                //生成永久授权码 和 access_token
                Map paramMap = new HashMap();
                paramMap.put("id", oauthParams.getId());
                paramMap.put("permanentCode", permanentCode);
                paramMap.put("accessToken", accessToken);
                paramMap.put("accessTokenExpireTime", new Date());
                oauthDao.updateOauthParams(paramMap);
            }
            resultSuccessMap(map);
            map.put("access_token", accessToken);
            map.put("permanent_code", permanentCode);
            map.put("auth_corp_info",tenantName);
            map.put("auth_info",suite);
            map.put("expires_in", 7200);
        } catch (Exception e) {
            logger.error("重新生成预授权码出错:{}", e.getMessage());
            resultFailMap(map);
        }
        logger.info("------ end /cgi-bin/service/get_permanent_code 返回结果：{}", JSON.toJSONString(map));
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
    public Map getCorpToken(String suiteAccessToken, PermanentCode corpToken) {
        logger.info("------ begin  /cgi-bin/service/get_corp_token");
        logger.info("------ 参数****  suite_access_token:{},auth_corpid:{},permanent_code:{}",
                suiteAccessToken, corpToken.getAuth_corpid(), corpToken.getPermanent_code());

        //校验参数是否为空
        if (StringUtils.isBlank(suiteAccessToken) || checkObjFieldIsNotNull(corpToken)) {
            return resultFailMap(WeChatResultCode.NULL_PARAM.getCode(), WeChatResultCode.NULL_PARAM.getMsg());
        }

        //校验 永久授权码
        OauthParams oauthParams = oauthDao.getOauthParamByPermanentCode(corpToken.getPermanent_code());
        if (oauthParams == null || StringUtils.isBlank(oauthParams.getSuiteId())) {
            return resultFailMap(WeChatResultCode.INVALID_PERMANENT_CODE.getCode(), WeChatResultCode.INVALID_PERMANENT_CODE.getMsg());
        }

        //校验第三方应用凭证suite_access_token
        String suiteId = serviceProviderDao.getSuiteIdBySuiteAccessToken(suiteAccessToken);
        if (StringUtils.isBlank(suiteId) || !suiteId.equals(oauthParams.getSuiteId())) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getCode(), WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getMsg());
        }

        //校验 授权方corpid
        if (!corpToken.getAuth_corpid().equals(oauthParams.getCorpId())) {
            return resultFailMap(WeChatResultCode.INVALID_CORP_ID.getCode(), WeChatResultCode.INVALID_CORP_ID.getMsg());
        }

        //设置返回码 和 access_token 值
        //设置该方法返回的值
        Map map = new HashMap();
        resultSuccessMap(map);
        map.put("access_token", oauthParams.getAccessToken());
        map.put("expires_in", 7200);
        logger.info("------ end /cgi-bin/service/get_corp_token 返回结果：{}", JSON.toJSONString(map));
        return map;
    }

    /**
     * @param corPid     企业id
     * @param corpSecret 企业秘钥
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:06
     * @description: 企业获取自身access_token
     */
    public Map getToken(String corPid, String corpSecret) {
        Map map = new HashMap();
        map.put("errcode", WeChatResultCode.SUCCESS.getCode());
        map.put("errmsg", WeChatResultCode.SUCCESS.getMsg());
        map.put("access_token", RandomCharsGenerator.uuid(16));
        map.put("expires_in", 7200);
        return map;
    }

    /**
     * @param suiteToken 接收服务商token的接口body参数
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:06
     * @description: 服务提供商鉴权token接收接口
     */
    public Map providerAuthToken(SuiteToken suiteToken) {
        logger.info("------ begin  /cgi-bin/service/provider_auth_token tenantId:{},suite_id:{},token:{}",
                suiteToken.getCorp_id(), suiteToken.getSuite_id(), suiteToken.getToken());

        //校验参数是否为空
        if (StringUtils.isBlank(suiteToken.getSuite_id()) || StringUtils.isBlank(suiteToken.getToken())) {
            return  resultFailMap(WeChatResultCode.NULL_PARAM.getCode(), WeChatResultCode.NULL_PARAM.getMsg());
        }

        //查询应用鉴权参数
        OauthSuite suite = serviceProviderDao.getOauthSuiteBySuiteId(suiteToken.getSuite_id());

        //校验应用id是否合法
        if (suite == null) {
            return  resultFailMap(WeChatResultCode.INVALID_SUITE_ID.getCode(), WeChatResultCode.INVALID_SUITE_ID.getMsg());
        }

        //设置该方法的返回值
        Map map = new HashMap();

        try {
            //第三方鉴权更新token入库
            Map paramMap = new HashMap();
            paramMap.put("suiteId", suiteToken.getSuite_id());
            paramMap.put("suiteToken", suiteToken.getToken());
            serviceProviderDao.updateSuite(paramMap);

            resultSuccessMap(map);
            map.put("errcode", WeChatResultCode.SUCCESS.getCode());
            map.put("errmsg", WeChatResultCode.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("服务提供商鉴权token接收接口更新失败：{}", e.getMessage());
            resultFailMap(map);
        }
        logger.info("------ end /cgi-bin/service/provider_auth_token 返回结果：{}", JSON.toJSONString(map));
        return map;
    }


    /**
     * @param access_token 第三方应用的suie_access_token
     * @param code         通过成员授权获取到的code，最大为512字节。每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。
     * @return 结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/6 下午 4:55
     * @description: 第三方根据code获取企业成员信息
     */
    public Map getuserinfo3rd(String access_token, String code) {
        logger.info("------ begin  /cgi-bin/service/getuserinfo3rd");
        logger.info("------ 参数*** access_token:{},code:{}", access_token,code);

        //校验参数是否为空
        if (StringUtils.isBlank(access_token) || StringUtils.isBlank(code)) {
            return resultFailMap(WeChatResultCode.NULL_PARAM.getCode(), WeChatResultCode.NULL_PARAM.getMsg());
        }

        //校验第三方凭证suite_access_token
        if (checkSuiteAccessToken(access_token) == 0) {
            return resultFailMap(WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getCode(), WeChatResultCode.INVALID_SUITE_ACCESS_TOKEN.getMsg());
        }

        Map result = new HashMap();
        try {
            //根据token获取租户成员用户信息
            String url = tenantUri + userIdByToken;
            Map<String, Object> map = new HashMap();
            map.put("token", code);
            //依赖关系：第三方根据code获取企业成员信息-->租户模块-->根据登录token获取登录用户详情
            logger.debug("------ begin 根据登录token获取登录用户详情token：{}", code);
            JSONObject json = restTemplate.getForObject(url, JSONObject.class, map);
            logger.debug("------ end 返回结果：{}", JSON.toJSONString(json));
            int errCode =  json.getInteger("errcode");
            if(errCode != 0){
                return resultFailMap(errCode, json.getString("errmsg"));
            }

            String userId = json.getJSONObject("data").get("login_name").toString();
            String tenantId = json.getJSONObject("data").get("tenant_id").toString();

            //设置返回码员工信息
            resultSuccessMap(result);
            result.put("CorpId", tenantId);
            result.put("UserId", userId);
//            result.put("user_ticket",RandomCharsGenerator.uuid(16));
            result.put("expires_in", 7200);

        } catch (Exception e) {
            logger.error("获取企业成员信息失败:{}", e.getMessage());
            resultFailMap(result);
        }
        logger.info("------ end /cgi-bin/service/getuserinfo3rd 返回结果：{}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param corpId 租户id
     * @return 租户信息
     *
     * @creator 王瑞
     * @createtime 2018/4/24 10:18
     * @description: 获取租户信息
     */
    public Map getCorpInfo(String corpId){
        logger.info("------ 获取租户名称开始 corpId:{}", corpId);
        //根据租户id获取租户名称
        //请求参数赋值
        Map tenantUriParameters = new HashMap();
        tenantUriParameters.put("tenantId", corpId);

        logger.debug("------ begin 请求系统服务模块根据租户id获取租户详情");
        //依赖关系：根据租户id获取租户名称-->系统服务模块-->根据租户id获取租户详情
        JSONObject tenantResult = restTemplate.getForObject(systemUri + tenantById, JSONObject.class, tenantUriParameters);
        logger.debug("------ end 求系统服务模块根据租户id获取租户详情 返回结果：{}", tenantResult!=null?tenantResult.toJSONString():"");

        //设置返回值
        Map map = new HashMap();
        if(tenantResult!=null ){
            String name = tenantResult.getString("name");
            if(StringUtils.isNotBlank(name)){
                map.put("corpid",corpId);
                map.put("corp_name",name);
            }
        }
        logger.info("------ 获取租户名称结束");
        return map;
    }

    /**
     * @param corpId 租户id
     * @param suiteId 应用id
     * @return 应用名称
     *
     * @creator 王瑞
     * @createtime 2018/4/24 16:41
     * @description: 获取应用名称
     */
    public JSONObject getSuiteInfo(String corpId, String suiteId){
        logger.info("------ 获取应用名称开始 corpId:{},suiteId:{}", corpId, suiteId);
        //获取应用信息
        //请求参数赋值
        MultiValueMap<String, String> suiteUriParameters = new LinkedMultiValueMap<>();
        suiteUriParameters.add("suiteId", corpId);
        suiteUriParameters.add("tenantId", suiteId);
        //依赖关系：获取应用名称-->调用suite应用注册模块-->根据应用id和租户id获取应用详情
        logger.debug("------ begin 根据应用id获取应用详情");
        JSONObject suiteResult = restTemplate.postForObject(suiteUri + suiteById, suiteUriParameters, JSONObject.class);
        logger.debug("------ end 根据应用id获取应用详情 返回结果：{}", suiteResult.toJSONString());

        //封装应用名称信息
        JSONObject suite = new JSONObject();
        if("0".equals(suiteResult.getString("errcode"))){
            JSONArray suiteArray = suiteResult.getJSONArray("data");
            JSONArray suiteName = new JSONArray();
            for (int i = 0 ; i < suiteArray.size(); i++){
                JSONObject suiteJsonObject = new JSONObject();
                String name = suiteArray.getJSONObject(i).getString("name");
                String id = suiteArray.getJSONObject(i).getString("id");
                suiteJsonObject.put("agentid",id);
                suiteJsonObject.put("name",name);
                suiteName.add(suiteJsonObject);
            }
            suite.put("agent",suiteArray);
        }
        logger.info("------ 获取应用名称结束");
        return suite;
    }

    /**
     * @param suiteAccessToken
     * @return 是否存在 1 是 0 否
     *
     * @creator 王瑞
     * @createtime 2018/4/23 19:07
     * @description: 检验鉴权用suiteAccessToken
     */
    public int checkSuiteAccessToken(String suiteAccessToken){
        String suiteId = serviceProviderDao.getSuiteIdBySuiteAccessToken(suiteAccessToken);
        if(StringUtils.isBlank(suiteId))
            return 0;
        else
            return 1;
    }

    /**
     * @param map
     * @creator 王瑞
     *
     * @createtime 2018/3/13 14:10
     * @description: 封装返回失败消息的方法
     */
    public Map resultFailMap(int errCode, String errMsg) {
        Map map = new HashMap();
        map.put("errcode", errCode);
        map.put("errmsg", errMsg);
        logger.info("------ end 返回结果：{}", JSON.toJSONString(map));
        return map;
    }

    /**
     * @param map
     * @creator 王瑞
     *
     * @createtime 2018/3/13 14:10
     * @description: 封装返回成功消息的方法
     */
    public void resultSuccessMap(Map map) {
        map.put("errcode", WeChatResultCode.SUCCESS.getCode());
        map.put("errmsg", WeChatResultCode.SUCCESS.getMsg());
    }

    /**
     * @param map
     * @creator 王瑞
     *
     * @createtime 2018/3/13 14:10
     * @description: 封装返回失败消息的方法
     */
    public void resultFailMap(Map map) {
        map.put("errcode", WeChatResultCode.UNKNOW_ERROR.getCode());
        map.put("errmsg", WeChatResultCode.UNKNOW_ERROR.getMsg());
    }

    /**
     * @param obj 参数实体
     * @return 是否是空
     *
     * @creator 王瑞
     * @createtime 2018/3/12 15:08
     * @description: 利用反射判断参数实体不为空
     */
    private boolean checkObjFieldIsNotNull(Object obj) {
        try {
            for (Field f : obj.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if (null == f.get(obj) || StringUtils.isBlank(f.get(obj).toString())) {
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("利用反射判断参数实体不为空时出错:{}", e.getMessage());
        }
        return false;
    }

    /**
     * @param access_token     平台鉴权token
     * @param loginname        登陆名
     * @param password         密码
     * @return 返回验证成功失败
     *
     * @creator 陈旭
     * @createtime 2018/5/22 10:06
     * @description: 第三方服务APP登陆验证
     */
    public Map checkLogin4App(String access_token, String loginname,String password) {
        logger.info("------ begin  /cgi-bin/service/checkLogin4App");
        logger.info("------ 参数*** access_token:{},loginname:{},password:{}", access_token,loginname,password);

        //校验参数是否为空
        if (StringUtils.isBlank(access_token) || StringUtils.isBlank(loginname) || StringUtils.isBlank(password)) {
            return resultFailMap(WeChatResultCode.NULL_PARAM.getCode(), WeChatResultCode.NULL_PARAM.getMsg());
        }

        //校验access_token并获取tenantId
        Map resultMap = checkAccessToken(access_token);
        if (null == resultMap) {
            return resultFailMap(WeChatResultCode.OVERDUE_ACCESS_TOKEN.getCode(), WeChatResultCode.OVERDUE_ACCESS_TOKEN.getMsg());
        }

        long tenantId = (Long)resultMap.get("tenantId");
        String permanentCode = (String)resultMap.get("permanentCode");

        //DES 解密，密钥：permanentCode
        String decryStr = DESUtil.decrypt(password, permanentCode);
        if(null == decryStr) {
            return resultFailMap(WeChatResultCode.UNKNOW_ERROR.getCode(), WeChatResultCode.UNKNOW_ERROR.getMsg());
        }


        //调用租户服务app验证接口
        //请求参数赋值
        MultiValueMap<String, String> suiteUriParameters = new LinkedMultiValueMap<>();
        suiteUriParameters.add("userName", loginname);
        suiteUriParameters.add("passWord", decryStr);
        suiteUriParameters.add("tenantId", tenantId+"");
        //依赖关系：获取应用名称-->调用suite应用注册模块-->根据应用id和租户id获取应用详情
        logger.debug("------ begin 调用租户服务app验证接口 loginname:{},tenantId:{},decryStr:{}", loginname,tenantId,decryStr);
        JSONObject checkResult = restTemplate.postForObject(tenantUri + checkLogin4AppApi, suiteUriParameters, JSONObject.class);
        logger.debug("------ end 调用租户服务app验证接口 返回结果：{}", checkResult.toJSONString());

        //封装应用名称信息
        JSONObject suite = new JSONObject();
        if("true".equals(checkResult.getString("success"))){
            return resultFailMap(WeChatResultCode.SUCCESS.getCode(), WeChatResultCode.SUCCESS.getMsg());
        }
        else{
            return resultFailMap(WeChatResultCode.INVALID_USER_PASSWORD.getCode(), WeChatResultCode.INVALID_USER_PASSWORD.getMsg());
        }
    }

    /**
     * @param accessToken     平台鉴权token
     * @return tenantid
     *
     * @creator 陈旭
     * @createtime 2018/5/22 10:06
     * @description: 根据access_token获取tenantid
     */

    public Map checkAccessToken(String accessToken) {
        return serviceProviderDao.getTenantIdByAccessToken(accessToken);
    }
}
