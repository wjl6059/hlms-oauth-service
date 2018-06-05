package com.tiansu.hlms.oauth.task;

import com.tiansu.hlms.oauth.wx.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王瑞
 * @date 2018-4-24 16:59
 * @description 推送第三方授权回调通知失败的线程
 */
public class AuthorizedTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(AuthorizedTask.class);

    //租户id
    private String tenantId;

    //应用id
    private String suiteId;

    //鉴权token
    private String suiteToken;

    //推送的ticket
    private String suiteTicket;

    //预授权码
    private String preAuthCode;

    //第三方uri
    private String uri;

    private OauthService oauthService;
    //构造
    public AuthorizedTask(String tenantId, String suiteId, String suiteToken, String suiteTicket,
                          String preAuthCode, String uri ,OauthService oauthService) {
        this.tenantId = tenantId;
        this.suiteId = suiteId;
        this.suiteToken = suiteToken;
        this.suiteTicket = suiteTicket;
        this.preAuthCode = preAuthCode;
        this.uri = uri;
        this.oauthService = oauthService;
    }


    @Override
    public void run() {
        try {
            oauthService.corpAccess(suiteToken, suiteTicket, tenantId, suiteId, uri, preAuthCode);
        } catch (Exception e) {
            logger.info("推送第三方授权回调通知失败的线程失败uri:{},suiteId:{},suiteTicket:{},token:{},preAuthCode:{},tenantId:{}",
                    uri,suiteId,suiteTicket,suiteToken,preAuthCode,tenantId);
        }
    }
}
