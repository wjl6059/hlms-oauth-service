package com.tiansu.hlms.oauth.common;

import com.tiansu.hlms.oauth.constant.OauthResultCode;
import io.swagger.annotations.ApiModel;

/**
 * @description 相应结果工具类，封装接口返回格式，包含返回码、返回信息、返回结果数据
 * @author 王瑞
 * @date 2018-02-01 15:37
 **/
@ApiModel(value = "结果对象", description = "结果对象信息")
public class ResultUtil {

    /**
     * 成功的返回格式
     *
     * @param object 对象
     * @return Result 返回结果对象
     */
    public static Result success(Object object) {
        Result result = new Result();
        result.setErrcode(OauthResultCode.SUCCESS.getCode());
        result.setErrmsg(OauthResultCode.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    /**
     * 成功的返回格式，返回数据结果为空
     *
     * @return Result
     */
    public static Result success() {
        return success(null);
    }

    /**
     * 失败情况的返回格式
     *
     * @param code 返回结果编码
     * @param msg  返回结果信息
     * @return Result 返回结果对象
     */
    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setErrcode(code);
        result.setErrmsg(msg);
        return result;
    }
}
