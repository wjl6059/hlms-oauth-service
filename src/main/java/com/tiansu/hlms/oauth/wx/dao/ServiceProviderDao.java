package com.tiansu.hlms.oauth.wx.dao;

import com.tiansu.hlms.oauth.wx.bean.OauthSuite;
import com.tiansu.hlms.oauth.wx.bean.SuiteTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * @author 王瑞
 * @description 鉴权应用表操作
 * @date 2018-03-12 14:28
 */
@Mapper
public interface ServiceProviderDao {
    /**
     * @param map 参数集合
     * @return 应用鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/4/23 19:45
     * @description: 获取所有鉴权应用信息
     * <br/>返回结果：suiteId(应用id), suiteToken(第三方鉴权token)
     */
    List<SuiteTicket> getOauthSuiteInfo();


    /**
     * @param map 参数集合
     * @return 应用鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/3/12 14:45
     * @description: 根据suite_id和suite_secret查询表hlms_oauth_suite中是否存在
     * <br/>返回结果：suiteId(应用id),suiteSecret(应用秘钥)，suiteAccessToken(鉴权用)， suiteTicket(定时推送所需的ticket)
     */
    OauthSuite getOauthSuite(Map map);

    /**
     * @param suiteSecret 应用秘钥
     * @return 返回结果对象
     *
     * @creator 舒方强
     * @createtime 2018/3/31 09:54
     * @description: 校验应用秘钥是否合法
     */
    Map checkSuiteSecret(String suiteSecret);


    /**
     * @param suiteId 应用id
     * @return 应用鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:53
     * @description: 根据suiteId查询应用鉴权参数
     * <br/>返回结果：suiteId(应用id)，suiteSecret(应用秘钥)，suiteToken(第三方所需的鉴权token)，
     *                suiteTicket(定时推送给第三方的ticket)，suiteAccessToken(鉴权用)
     */
    OauthSuite getOauthSuiteBySuiteId(String suiteId);

    /**
     * @param suiteId 应用id
     * @return 服务商提供的token
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 根据应用id获取服务商提供的token
     */
    String getSuiteTokenBySuiteId(String suiteId);

    /**
     * @param suiteAccessToken 鉴权用token
     * @return 服务商提供的token
     *
     * @creator 王瑞
     * @createtime 2018/3/17 16:01
     * @description: 根据suite_access_token获取suiteId
     */
    String getSuiteIdBySuiteAccessToken(String suiteAccessToken);

    /**
     * @param accessToken     平台鉴权token
     * @return tenantid
     *
     * @creator 陈旭
     * @createtime 2018/5/22 10:06
     * @description: 根据access_token获取tenantid
     */
    Map getTenantIdByAccessToken(String accessToken);
    /**
     *
     * @param map 参数集合
     * @return 更新结果
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:56
     * @description: 根据suiteId更新鉴权应用表
     *
     */
    int updateSuite(Map map);

    /**
     *
     * @param oauthSuite 鉴权应用对象
     * @return 新增结果
     *
     * @creator 王瑞
     * @createtime 2018/3/14 10:56
     * @description: 新增鉴权应用表
     *
     */
    int createSuite(OauthSuite oauthSuite);

}
