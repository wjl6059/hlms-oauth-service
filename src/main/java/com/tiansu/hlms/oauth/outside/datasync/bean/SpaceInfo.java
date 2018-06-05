package com.tiansu.hlms.oauth.outside.datasync.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author chenrl
 * @description 空间信息对象
 * @date 2018-05-23 10:23
 */
@ApiModel(value = "空间信息", description = "空间信息简单对象")
public class SpaceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "空间信息主键id")
    private Long id;

    @ApiModelProperty(value = "空间信息编码")
    private String code;

    @ApiModelProperty(value = "空间信息名称")
    private String name;

    @ApiModelProperty(value = "空间信息层级")
    private Integer level;

    @ApiModelProperty(value = "上级空间信息")
    private Long parentid;

    @ApiModelProperty(value = "是否有效")
    private String disable;

    @ApiModelProperty(value = "空间分类 0-医院；1-建筑；2-楼层；3房间；")
    private String locType;

    @ApiModelProperty(value = "空间属性 0-医院；1-医院分区；2-建筑；3-建筑分区；4-楼层；5楼层分区；6房间；7-房间分区")
    private String levelCode;

    /**
     * get locType
     *
     * @return java.lang.String locType
     */

    public String getLocType() {
        return locType;
    }

    /**
     * set locType
     *
     * @param locType
     */

    public void setLocType(String locType) {
        this.locType = locType;
    }

    /**
     * get levelCode
     *
     * @return java.lang.String levelCode
     */

    public String getLevelCode() {
        return levelCode;
    }

    /**
     * set levelCode
     *
     * @param levelCode
     */

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    /**
     * get id
     *
     * @return java.lang.Long id
     */

    public Long getId() {
        return id;
    }

    /**
     * set id
     *
     * @param id
     */

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get code
     *
     * @return java.lang.String code
     */

    public String getCode() {
        return code;
    }

    /**
     * set code
     *
     * @param code
     */

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * get name
     *
     * @return java.lang.String name
     */

    public String getName() {
        return name;
    }

    /**
     * set name
     *
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * get level
     *
     * @return java.lang.Integer level
     */

    public Integer getLevel() {
        return level;
    }

    /**
     * set level
     *
     * @param level
     */

    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * get parentid
     *
     * @return java.lang.Long parentid
     */

    public Long getParentid() {
        return parentid;
    }

    /**
     * set parentid
     *
     * @param parentid
     */

    public void setParentid(Long parentid) {
        this.parentid = parentid;
    }

    /**
     * get disable
     *
     * @return java.lang.String disable
     */

    public String getDisable() {
        return disable;
    }

    /**
     * set disable
     *
     * @param disable
     */

    public void setDisable(String disable) {
        this.disable = disable;
    }

    @Override
    public String toString() {
        return "SpaceInfo{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", parentid=" + parentid +
                ", disable='" + disable + '\'' +
                ", locType='" + locType + '\'' +
                ", levelCode='" + levelCode + '\'' +
                '}';
    }
}
