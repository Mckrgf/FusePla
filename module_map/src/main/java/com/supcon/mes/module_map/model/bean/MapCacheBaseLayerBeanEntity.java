package com.supcon.mes.module_map.model.bean;


import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class MapCacheBaseLayerBeanEntity extends BaseEntity {


    /**
     * name : 中控科技园精模加简模
     * url : /greenDill/static/GISModel/JZ_Medium/tileset.json
     * muduleType : main
     * appliedTo : MOBILE
     * version : 1.0
     * remark : 20200525
     * size : 500
     * attrType : baseLayer
     * visible : false
     *
     *
     * {
     *                     "name": "中控科技园精模加简模",
     *                     "url": "/greenDill/static/GISModel/JZ_Medium/tileset.json",
     *                     "muduleType": "main",
     *                     "appliedTo": "MOBILE",
     *                     "version": "1.0",
     *                     "packageName": "JZ_Medium",
     *                     "size": 500,
     *                     "visible": false,
     *                     "attrType": "baseLayer"
     *                 }
     */
    public static int STATE_HAS_DOWNLOAD = 1;//已经下载，没有更新的
    public static int STATE_NEED_UPDATE = 2;//已经下载，可更新
    public static int STATE_DOWNLOADABLE = 3;//没有下载，可下载
    public static int STATE_DOWNLOAD_ING = 4;//正在下载中。。。



    private String name;
    private String url;
    private String muduleType;
    private String appliedTo;
    private String version;
    private String packageName;
    private int size;
    private String attrType;
    private boolean visible;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMuduleType() {
        return muduleType;
    }

    public void setMuduleType(String muduleType) {
        this.muduleType = muduleType;
    }

    public String getAppliedTo() {
        return appliedTo;
    }

    public void setAppliedTo(String appliedTo) {
        this.appliedTo = appliedTo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
