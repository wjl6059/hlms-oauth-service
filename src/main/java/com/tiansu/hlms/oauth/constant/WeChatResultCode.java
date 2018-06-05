package com.tiansu.hlms.oauth.constant;

/**
 * @author wangjl
 * @description
 * @create 2018-02-27 15:58
 **/
public enum WeChatResultCode {

    UNKNOW_ERROR(-1, "系統繁忙"),
    SUCCESS(0, "成功"),

    INVALID_SECRET(40001, "不合法的Secret"),
    INVALID_CORP_ID(40013, "不合法的CorpID"),
    INVALID_ACCESS_TOKEN(40014, "不合法的access_token"),
    INVALID_PARAM(40035, "不合法的参数"),
    NULL_PARAM(40063, "参数为空"),
    INVALID_PRE_AUTH_CODE(40077, "不合法的pre_auth_code参数"),
    INVALID_AUTH_CODE(40078, "不合法的auth_code参数"),
    INVALID_SUITE_SECRET(40080, "不合法的suite_secret"),
    INVALID_TOKEN(40082, "不合法的suite_token"),
    INVALID_SUITE_ID(40083, "不合法的suite_id"),
    INVALID_PERMANENT_CODE(40085, "不合法的permanent_code参数"),
    INVALID_SUITE_TICEKT(40086, "不合法的的suite_ticket参数"),
    INVALID_APPID(40001, "不合法的服务提供商应用appid"),
    OVERDUE_ACCESS_TOKEN(42001, "access_token已过期"),
    OVERDUE_PRE_AUTH_CODE(42007, "pre_auth_code已过期"),
    INVALID_SUITE_ACCESS_TOKEN(40084, "不合法的suite_access_token参数"),
    OVERDUE_SUITE_ACCESS_TOKEN(42009, "suite_access_token已过期"),
    INVALID_USER_PASSWORD(60130, "用户名密码错误"),
    OVERDUE_USER_TICEKT(84014, "成员票据过期"),
    INVALID_USER_TICEKT(84015, "成员票据无效");


    // 编码
    private int code;
    // 信息
    private String msg;

    WeChatResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
