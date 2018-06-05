package com.tiansu.hlms.oauth.wx.controller;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.common.ResultUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 王瑞
 * @description 只测试用 后面删除
 * @date 2018-03-20 09:20
 */
@RestController
public class TestController {

    /**
     * @creator 王瑞
     * @createtime 2018/3/20 09:27
     * @description: 用来模拟定时推送ticket是第三方接收ticket的接口
     */
    @PostMapping("/hlms/ticket/get")
    public Result ticket(@RequestBody Map map, String token){

        map.put("token",token);

        return ResultUtil.success(map);
    }

    /**
     * @creator 王瑞
     * @createtime 2018/3/20 09:27
     * @description: 用来模拟定时推送数据版本号是第三方接收数据版本号的接口
     */
    @PostMapping("/hlms/version/get")
    public Result version(@RequestBody Map map, String token){

        map.put("token",token);

        return ResultUtil.success(map);
    }

}
