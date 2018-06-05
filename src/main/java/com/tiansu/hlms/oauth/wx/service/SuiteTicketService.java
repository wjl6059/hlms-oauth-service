package com.tiansu.hlms.oauth.wx.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tiansu.hlms.oauth.wx.bean.SuiteTicket;
import com.tiansu.hlms.oauth.wx.dao.OauthDao;
import com.tiansu.hlms.oauth.wx.dao.ServiceProviderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author 王瑞
 * @description 定时推送suiteTicket业务
 * @date 2018-03-15 20:31
 */
@Service
public class SuiteTicketService {

    private Logger logger = LoggerFactory.getLogger(SuiteTicketService.class);

    @Autowired
    private RestTemplate restTemplate;

    /*
     * 鉴权应用表操作dao
     */
    @Autowired
    private ServiceProviderDao serviceProviderDao;

    //获取suite模块的地址
    @Value("${suite.uri}")
    private String uri;

    //suite模块的根据应用id获取应用详情接口路由
    @Value("${suite.providerApi}")
    private String providerApi;

    /**
     * @return 定时推送suiteTicket所需实体集合
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:09
     * @description: 定时推送suiteTicket所需的uri, token, suiteId
     */
    public List<SuiteTicket> getRestTicketUri() {
        logger.info("------ 获取定时推送suiteTicket所需实体集合开始");

        //定时推送suiteTicket所需实体集合
        List<SuiteTicket> suiteTickets = serviceProviderDao.getOauthSuiteInfo();

        for (int i = 0; i < suiteTickets.size(); i++) {
            try {
                //根据应用id获取对应的uri
                //参数赋值
                Map<String, Object> param = new HashMap<>();
                param.put("suite_id", suiteTickets.get(i).getSuiteId());
                param.put("business", "RestTicket");

                //依赖关系：定时推送ticket-->suite应用注册模块-->根据应用id获取第三方uri
                logger.debug("------ 从suite应用注册模块获取第三方的uri开始");
                JSONObject json = restTemplate.getForObject(uri + providerApi, JSONObject.class, param);
                logger.debug("------ 从suite应用注册模块获取第三方的uri结束：{}", json.toJSONString());

                //获取uri
                JSONArray data = json.getJSONArray("data");
                if ("0".equals(json.getString("errcode")) && data != null) {
                    List<String> ticketStr = new LinkedList<>();
                    for (int j = 0; j < data.size(); j++) {
                        String restService = data.getJSONObject(j).get("restService").toString();
                        //将得到的uri封装
                        ticketStr.add(restService);
                    }
                    suiteTickets.get(i).setUri(ticketStr);
                }
            } catch (Exception e){
                logger.error("------ 从suite应用注册模块获取第三方的uri失败：{}", e.getMessage());
            }
        }
        logger.info("------ 获取定时推送suiteTicket所需实体集合开始");
        return suiteTickets;
    }

    /**
     * @param suiteId 应用id
     * @param suiteTicket 服务商鉴权用token
     * @return
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:13
     * @description: 根据suiteId更新suiteTicket
     */
    public int updateSuiteTicket(String suiteId, String suiteTicket) {
        logger.info("------ 更新suite_ticket开始");
        Map param = new HashMap();
        param.put("suiteTicket", suiteTicket);
        param.put("suiteId", suiteId);
        int result = serviceProviderDao.updateSuite(param);
        logger.info("------ 更新suite_ticket结束 更新了{}条数据", result);
        return result;
    }

    /**
     * @param suiteId 应用id
     * @param suiteTicket 要推送的ticket
     * @param token 第三方鉴权token
     *
     * @creator 王瑞
     * @createtime 2018/4/26 11:00
     * @description: 推送ticket
     */
    public void pushTicket(String suiteId, String suiteTicket, String token, String ticketUri){

        //推送赋值
        Map uriParameters = new HashMap();
        Map bodyParameters = new HashMap();
        bodyParameters.put("suiteId", suiteId);
        bodyParameters.put("timeStamp", System.currentTimeMillis());
        bodyParameters.put("suiteTicket", suiteTicket);
        uriParameters.put("token", token);
        logger.debug("发起ticket推送uri:{},suiteId:{},suiteTicket:{},token{}", ticketUri, suiteId, suiteTicket, token);
        String resultStr = restTemplate.postForObject(ticketUri, bodyParameters, String.class, uriParameters);
        logger.info("调用推送接口返回值：{}", resultStr);

        //将推送的ticket入库
        updateSuiteTicket(suiteId, suiteTicket);
    }
}

