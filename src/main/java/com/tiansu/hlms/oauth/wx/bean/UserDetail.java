package com.tiansu.hlms.oauth.wx.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 接收第三方使用user_ticket获取成员详情的接口的包体参数
 * @create 2018-03-08 15:53
 */
@ApiModel(value = "接收第三方使用user_ticket的接口body参数", description = "接收第三方使用user_ticket获取成员详情的接口的包体参数")
public class UserDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    //成员票据
    @ApiModelProperty(value = "成员票据",required = true)
    private String user_ticket;

    /**
     * get user_ticket
     *
     * @return java.lang.String user_ticket
     */
    public String getUser_ticket() {
        return user_ticket;
    }

    /**
     * set user_ticket
     *
     * @param user_ticket
     */
    public void setUser_ticket(String user_ticket) {
        this.user_ticket = user_ticket;
    }
}
