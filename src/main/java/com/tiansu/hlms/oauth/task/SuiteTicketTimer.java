package com.tiansu.hlms.oauth.task;

import com.tiansu.hlms.oauth.utils.RandomCharsGenerator;
import com.tiansu.hlms.oauth.wx.bean.SuiteTicket;
import com.tiansu.hlms.oauth.wx.service.SuiteTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author 王瑞
 * @description 定时向第三方推送ticket
 * @date 2018-02-26 17:08
 * @modifier 王瑞
 * @modifytime 2018/3/15 17:17
 * @modifyDec: 实现定时向第三方推送ticket
 **/
@Configuration          //证明这个类是一个配置文件   暂时注释掉
@EnableScheduling       //打开quartz定时器总开关   暂时注释掉
public class SuiteTicketTimer {

    private Logger logger = LoggerFactory.getLogger(SuiteTicketTimer.class);

    @Autowired
    private SuiteTicketService suiteTicketService;

    /**
     * 定时生成ticket并推送给各个服务应用（每1小时执行一次）
     */
    @Scheduled(cron = "0 0 */1 * * *")
    public void pushSuiteTicket() {
        logger.debug("begin to pushSuiteTicket……");
        //获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        logger.debug("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        try {
            //获取所需推送的应用id
            List<SuiteTicket> list = suiteTicketService.getRestTicketUri();
            logger.debug("ticket to push:"+list.size()+"--"+list.toString());
            if (list != null && list.size() > 0) {
                //声明Executor线程池
                ExecutorService executor = Executors.newCachedThreadPool();

                for (int i = 0; i < list.size(); i++) {
                    //生成ticket
                    String suiteTicket = RandomCharsGenerator.uuid(16);

                    //获取第三方鉴权token
                    String token = list.get(i).getToken();

                    //获取suiteId
                    String suiteId = list.get(i).getSuiteId();

                    //获取要推送ticket的uri集合
                    List<String> uris = list.get(i).getUri();
                    if(uris != null && uris.size()>0) {
                        for (int j = 0; j < uris.size(); j++) {
                            //给推送线程赋值
                            TicketTask ticketTask = new TicketTask(
                                    token,
                                    suiteId,
                                    uris.get(j),
                                    suiteTicket,
                                    suiteTicketService);

                            //将推送ticket的线程提交到声明Executor的线程池
                            executor.execute(ticketTask);
                        }
                    }
                    /*  后期如果需要接收线程执行结果请用下面注释
                    Future future = executor.submit(ticketTask);
                    logger.info("线程池中线程数目：{}，队列中等待执行的任务数目：{}，已执行玩别的任务数目：{}",
                            executor.getPoolSize(),executor.getQueue().size(),executor.getCompletedTaskCount());
                    */
                }
                executor.shutdown();
            }

        } catch (Exception e) {
            logger.error("定时向第三方推送ticket错误!", e);
        }
        logger.debug("end to pushSuiteTicket……");
        //获取当前时间
        LocalDateTime nowDateTime = LocalDateTime.now();
        logger.debug("当前时间为:" + nowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}







/*
    一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。
    按顺序依次为
    秒（0~59）
    分钟（0~59）
    小时（0~23）
    天（月）（0~31，但是你需要考虑你月的天数）
    月（0~11）
    天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
    年份（1970－2099）
    “/”字符用来指定数值的增量
    例如：在子表达式（分钟）里的“0/15”表示从第0分钟开始，每15分钟
    在子表达式（分钟）里的“3/20”表示从第3分钟开始，每20分钟（它和“3，23，43”）的含义一样
    “？”字符仅被用于天（月）和天（星期）两个子表达式，表示不指定值
    当2个子表达式其中之一被指定了值以后，为了避免冲突，需要将另一个子表达式的值设为“？”
    “L” 字符仅被用于天（月）和天（星期）两个子表达式，它是单词“last”的缩写
*/

