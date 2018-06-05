package com.tiansu.hlms.oauth.wx.dao;

import com.tiansu.hlms.oauth.wx.bean.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 王瑞
 * @description 鉴权持久化操作
 * @date 2018-02-26 上午 11:33
 */

@Mapper
public interface OauthDao {

    /**
     * @param oauthParams 鉴权参数实体类
     * @return 新增结果
     *
     * @creator 王瑞
     * @createtime 2018/3/5 20:18
     * @description: 新增鉴权参数
     */
    int createOauthParams(OauthParams oauthParams);

    /**
     * @param oauthParams 鉴权参数实体类
     * @return 新增结果
     *
     * @creator 王瑞
     * @createtime 2018/3/5 20:18
     * @description: 新增鉴权参数
     */
    int createOauthParams(SynchroOauthParams oauthParams);

    /**
     * @param map 鉴权参数
     * @return 更新结果
     *
     * @creator 王瑞
     * @createtime 2018/3/12 11:44
     * @description: 更新鉴权参数
     */
    int updateOauthParams(Map map);

    /**
     * @param map 参数集合
     * @return 鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/3/6 8:50
     * @description: 根据租户鉴权id 和 应用id 查询鉴权参数
     * <br/> 返回结果 corpId(租户id), suiteId(应用id)
     */
    OauthParams getOauthParam(Map map);

    /**
     * @param preAuthCode 预授权码
     * @return 鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/4/24 9:50
     * @description: 根据预授权码 查询鉴权参数
     * <br/> 返回结果 corpId(租户id), suiteId(应用id)
     */
    OauthParams getOauthParamByPreAuthCode(String preAuthCode);

    /**
     * @param permanent_code 永久授权码
     * @return 鉴权参数对象
     *
     * @creator 王瑞
     * @createtime 2018/4/24 9:50
     * @description: 根据永久授权码 查询鉴权参数
     * <br/> 返回结果 corpId(租户id), suiteId(应用id)
     */
    OauthParams getOauthParamByPermanentCode(String permanent_code);

    /**
     * @return 所有租户id
     *
     * @creator 王瑞
     * @createtime 2018/3/16 17:23
     * @description: 获取鉴权参数表中所有租户id

    List<String> getCorpIdAll();
     */

    /**
     * @param tenantId 租户id
     * @return 该租户下要推送的数据版本
     *
     * @creator 王瑞
     * @createtime 2018/3/16 17:24
     * @description: 封装返回要推送的数据版本号信息
     */
    List<Version> getSuiteIdByCorpId(String tenantId);

    /**
     * @return 返回结果对象
     *
     * @creator 王瑞
     * @createtime 2018/3/16 17:23
     * @description: 获取鉴权参数表中所有应用
     */
    List<String> getSuiteIdAll();

    /**
     * @param accessToken 鉴权token
     * @return 返回结果对象
     *
     * @creator 舒方强
     * @createtime 2018/3/19 09:54
     * @description: 校验平台鉴权用的token是否合法
     * <br/> 返回结果：id(主键)，corp_id(租户id)，suite_id(应用id)
     */
    Map checkToken(String accessToken);

    /**
     * @return 所有未授权成功的集合
     *  @creator 王瑞
     * @createtime 2018/4/24 15:46
     * @description: 获取所有未成功授权的鉴权参数
     * <br/> 返回结果 corpId(租户id), suiteId(应用id), suiteToken(第三方鉴权用的token), suiteTicket(定时推送的token), preAuthCode(预授权码)
     */
    List<Authorized> getAuthorized();

}
