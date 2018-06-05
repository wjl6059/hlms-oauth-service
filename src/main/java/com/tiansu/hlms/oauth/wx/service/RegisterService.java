package com.tiansu.hlms.oauth.wx.service;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.common.ResultUtil;
import com.tiansu.hlms.oauth.utils.RandomCharsGenerator;
import com.tiansu.hlms.oauth.wx.dao.RegisterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.tiansu.hlms.oauth.common.IdGen.uuid;

/**
 * @author shufq
 * @description
 * @date 2018/2/26 17:06
 */
@Service
public class RegisterService {
    @Autowired
    private RegisterDao registerDao;

    /**
     * 生成服务的秘钥并存入数据库
     * @param providerId
     */
    public void insertProvider(String providerId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",RandomCharsGenerator.idGen());
        map.put("provider_secret",uuid(12));
        map.put("providerId",providerId);
        registerDao.insertProvider(map);
    }

    /**
     * 查询providerId和秘钥并返回
     * @param providerId
     * @return
     */
    public Map<String,Object> getProvider(String providerId) {
        return registerDao.getProvider(providerId);
    }

    /**
     * 生成应用的秘钥并存入数据库
     * @param suiteId
     */
    public void insertSuite(String suiteId,String suite_token) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", RandomCharsGenerator.idGen());
        map.put("suite_secret",uuid(12));
        map.put("suiteId",suiteId);
        map.put("suite_token",suite_token);
        registerDao.insertSuite(map);
    }

    /**
     * 查询suiteId和秘钥并返回
     * @param suiteId
     * @return
     */
    public Map<String,Object> getSuite(String suiteId) {
        return registerDao.getSuite(suiteId);
    }

    /**
     * 获取字段权限
     * @param map
     * @return
     */
    public Result getFiledByToken(Map map) {
        /*
        根据token查询应用id
         */
        Map result = registerDao.getSuiteByToken(map);
        if(result == null){
            return ResultUtil.error("30000040014","不合法的access_token");
        }
        map.put("suite_id",result.get("suite_id"));

        /*
        根据suite_id获取字段权限
         */
        String field=registerDao.getFiled(map);
        result.put("field",field);
        return ResultUtil.success(result);
    }
}
