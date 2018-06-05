package com.tiansu.hlms.oauth.constant;

/**
 * @author 王瑞
 * @description
 * @create 2018-02-27 15:58
 **/
public enum OauthResultCode {

    UNKNOW_ERROR("-1", "系統繁忙"),
    SUCCESS("0", "成功"),

    NULL_PARAM("300101", "参数为空"),
    INVALID_PARAM("300102", "参数不合法"),
    NULL_PARAM_TOKEN("300103","登录token为空"),
    INVALID_SECRET("300501", "不合法的Secret"),
    INVALID_CORP_ID("300513", "不合法的CorpID"),
    INVALID_ACCESS_TOKEN("300514", "不合法的access_token"),
    INVALID_PRE_AUTH_CODE("300577", "不合法的pre_auth_code参数"),
    INVALID_AUTH_CODE("300578", "不合法的auth_code参数"),
    INVALID_SUITE_SECRET("300580", "不合法的suite_secret"),
    INVALID_TOKEN("300582", "不合法的suite_token"),
    INVALID_SUITE_ID("300583", "不合法的suite_id"),
    INVALID_PERMANENT_CODE("300585", "不合法的permanent_code参数"),
    INVALID_SUITE_TICEKT("300586", "不合法的的suite_ticket参数"),
    INVALID_APPID("300501", "不合法的服务提供商应用"),
    OVERDUE_ACCESS_TOKEN("300521", "access_token已过期"),
    OVERDUE_PRE_AUTH_CODE("300527", "pre_auth_code已过期"),
    OVERDUE_SUITE_ACCESS_TOKEN("300529", "suite_access_token已过期"),
    OVERDUE_USER_TICEKT("300514", "成员票据过期"),
    INVALID_USER_TICEKT("300515", "成员票据无效"),
    INVALID_URI_ERROR("300201", "获取服务商接收平台数据变化接口失败");



    // 编码
    private String code;
    // 信息
    private String msg;

    OauthResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
