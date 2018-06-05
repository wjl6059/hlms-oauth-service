package com.tiansu.hlms.oauth.outside.datasync.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author 王瑞
 * @description 数据同步相关
 * @date 2018-05-23 10:23
 */
@ApiModel(value = "数据同步", description = "数据同步相关")
public class DataSynchro implements Serializable {
    private static final long serialVersionUID = 1L;

    //租户id
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    //应用id
    @ApiModelProperty(value = "应用id")
    private String suiteId;

    //数据类别 用于区分推送数据范围 101：部门信息201：用户信息；202：用户角色关联信息；301：角色信息；302：管理组信息；401：权限信息；402：角色权限关联信息；
    @ApiModelProperty(value = "数据类别 用于区分推送数据范围 101：部门信息201：用户信息；202：用户角色关联信息；301：角色信息；302：管理组信息；401：权限信息；402：角色权限关联信息；")
    private String dataModule;

    //时间戳
    @ApiModelProperty(value = "时间戳")
    private Long time;

    /**
     * get tenantId
     *
     * @return java.lang.String tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * set tenantId
     *
     * @param tenantId
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * get suiteId
     *
     * @return java.lang.String suiteId
     */
    public String getSuiteId() {
        return suiteId;
    }

    /**
     * set suiteId
     *
     * @param suiteId
     */
    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    /**
     * get dataModule
     *
     * @return java.lang.String dataModule
     */
    public String getDataModule() {
        return dataModule;
    }

    /**
     * set dataModule
     *
     * @param dataModule
     */
    public void setDataModule(String dataModule) {
        this.dataModule = dataModule;
    }

    /**
     * get time
     *
     * @return java.lang.Long time
     */
    public Long getTime() {
        return time;
    }

    /**
     * set time
     *
     * @param time
     */
    public void setTime(Long time) {
        this.time = time;
    }
}
