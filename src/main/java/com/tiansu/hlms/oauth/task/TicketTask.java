package com.tiansu.hlms.oauth.task;

import com.tiansu.hlms.oauth.wx.service.SuiteTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王瑞
 * @date 2018-03-16 11:07
 * @description 推送ticket的线程
 */
public class TicketTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(TicketTask.class);

    //租户id
    private String token;

    //应用id
    private String suiteId;

    //推送的ticket
    private String suiteTicket;

    //第三方uri
    private String uri;

    private SuiteTicketService suiteTicketService;

    //构造
    public TicketTask(String token, String suiteId, String uri, String suiteTicket, SuiteTicketService suiteTicketService) {
        this.token = token;
        this.suiteId = suiteId;
        this.suiteTicket = suiteTicket;
        this.uri = uri;
        this.suiteTicketService = suiteTicketService;
    }


    @Override
    public void run() {
        try {
            suiteTicketService.pushTicket(suiteId, suiteTicket, token, uri);
        } catch (Exception e) {
            logger.error("suiteTicket推送失败：{}", e.getMessage());
        }
    }
}
