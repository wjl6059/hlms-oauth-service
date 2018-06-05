package com.tiansu.hlms.oauth.outside.datasync.controller;

import com.tiansu.hlms.oauth.common.Result;
import com.tiansu.hlms.oauth.outside.datasync.bean.DataSynchro;
import com.tiansu.hlms.oauth.outside.datasync.service.DataSynchroNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王瑞
 * @description 通知第三方数据同步，同步成功回调
 * @date 2018-05-23 09:59
 */
@RestController
@Api(value = "dataSynchroNoticeController", description = "通知第三方数据同步，同步成功回调")
public class DataSynchroNoticeController {

    /*
     * 通知第三方数据同步，同步成功回调业务逻辑操作
     */
    private DataSynchroNoticeService dataSynchroNoticeService;

    /**
     *
     * @param dataSynchro 数据同步相关参数
     * @return 数据同步通知结果
     *
     * @creator 王瑞
     * @createtime 2018/5/23 10:39
     * @description: 通知第三方同步数据
     */
    @PostMapping("/hlms/oauth/data_synchro_notice")
    @ApiOperation(value = "通知第三方同步数据", notes = "dataSynchro", produces = "application/json")
    public Result dataSynchroNotice(@RequestBody DataSynchro dataSynchro){
        return dataSynchroNoticeService.dataSynchroNotice(dataSynchro);
    }

}
