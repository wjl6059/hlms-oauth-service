package com.tiansu.hlms.oauth.wx.dao;


import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author shufq
 * @description
 * @date 2018/2/26 17:10
 */
@Mapper
public interface RegisterDao {

    /**
     * @author shufq
     * @description 插入服务id和秘钥
     * @date 2018/2/26 17:10
     */
    void insertProvider(Map<String, Object> map);

    /**
     * @author shufq
     * @description 根据服务id查询秘钥
     * @date 2018/2/26 17:10
     */
    Map<String,Object> getProvider(String providerId);

    /**
     * @author shufq
     * @description 插入应用id和秘钥
     * @date 2018/2/26 17:10
     */
    void insertSuite(Map<String, Object> map);

    /**
     * @author shufq
     * @description 根据应用id获取秘钥
     * @date 2018/2/26 17:10
     */
    Map<String,Object> getSuite(String suiteId);

    /**
     * @author shufq
     * @description 根据token获取应用id
     * @date 2018/2/26 17:10
     */
    Map getSuiteByToken(Map map);

    /**
     * @author shufq
     * @description 根据应用id和字段拥有者查询字段权限
     * @date 2018/2/26 17:10
     */
    String getFiled(Map map);
}
