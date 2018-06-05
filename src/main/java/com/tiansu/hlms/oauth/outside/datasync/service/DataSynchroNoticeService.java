package com.tiansu.hlms.oauth.outside.datasync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.constant.OauthResultCode;
import com.tiansu.hlms.oauth.outside.datasync.bean.DataSynchro;
import com.tiansu.hlms.oauth.wx.dao.ServiceProviderDao;
import com.tiansu.hlms.oauth.wx.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王瑞
 * @description 通知第三方数据同步，同步成功回调业务逻辑操作
 * @date 2018-05-23 10:38
 */
@Service
public class DataSynchroNoticeService {

    private Logger logger = LoggerFactory.getLogger(OauthService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    //获取suite模块的地址
    @Value("${suite.uri}")
    private String uri;

    //suite模块的根据应用id获取应用详情接口路由
    @Value("${suite.providerApi}")
    private String providerApi;

    /**
     *
     * @param dataSynchro 数据同步相关参数
     * @return 数据同步通知结果
     *
     * @creator 王瑞
     * @createtime 2018/5/23 10:39
     * @description: 通知第三方同步数据
     */
    public Result dataSynchroNotice(DataSynchro dataSynchro){
        logger.info("------ begin /hlms/oauth/data_synchro_notice");
        logger.info("推送数据版本号------body:{}", JSON.toJSONString(dataSynchro));
        Result result = new Result();

        //根据应用id获取对应的uri
        //参数赋值
        Map<String, Object> param = new HashMap<>();
        param.put("suite_id", dataSynchro.getSuiteId());
        param.put("business", "RestTicket");

        //服务商接收平台数据变化接口
        String restService = "";
        try {
            //依赖关系：通知第三方同步数据-->suite应用注册模块-->根据应用id获取第三方uri
            logger.debug("------ 从suite应用注册模块获取第三方的uri开始");
            JSONObject json = restTemplate.getForObject(uri + providerApi, JSONObject.class, param);
            logger.debug("------ 从suite应用注册模块获取第三方的uri结束：{}",json.toJSONString());

            //获取uri
            JSONArray data = json.getJSONArray("data");
            if ("0".equals(json.getString("errcode")) && data != null) {
                restService = data.getJSONObject(0).get("restService").toString();
            } else {
                result.setErrcode(OauthResultCode.INVALID_URI_ERROR.getCode());
                result.setErrmsg(OauthResultCode.INVALID_URI_ERROR.getMsg());
                logger.info("------ end /hlms/oauth/data_synchro_notice");
                return result;
            }

            //通知赋值
            Map bodyParameters = new HashMap();
            bodyParameters.put("tenantId", dataSynchro.getTenantId());
            bodyParameters.put("suiteId", dataSynchro.getSuiteId());
            bodyParameters.put("timeStamp", dataSynchro.getTime());
            bodyParameters.put("datamodule", dataSynchro.getDataModule());

            //获取第三方应用鉴权token
            String token = serviceProviderDao.getSuiteTokenBySuiteId(dataSynchro.getSuiteId());
            Map uriParameters = new HashMap();
            uriParameters.put("token", token);

            String resultStr = restTemplate.postForObject(restService, bodyParameters, String.class, uriParameters);
            logger.info("推送数据版本号返回结果：{}", resultStr);
        } catch (Exception e){
            logger.error("通知第三方同步数据失败：{}", e.getMessage());
            result.setErrcode(OauthResultCode.UNKNOW_ERROR.getCode());
            result.setErrmsg(OauthResultCode.UNKNOW_ERROR.getMsg());
        }
        logger.info("------ end /hlms/oauth/data_synchro_notice");
        return result;
    }

}
