package com.supcon.mes.module_map.model.bean;


import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class MapCacheConfigEntity extends BaseEntity {


    /**
     * useProxy : false
     * gisServer : 192.168.95.162:8070
     * weatherEnable : false
     * weatherCode : null
     * mousePosition : true
     * searchRadiu : 500
     * baseLayers : [{"name":"中控科技园精模加简模","url":"/greenDill/static/GISModel/JZ_Medium/tileset.json","muduleType":"main","appliedTo":"MOBILE","version":"1.0","remark":"20200525","size":500,"attrType":"baseLayer","visible":false}]
     */

    private boolean useProxy;
    private String gisServer;
    private boolean weatherEnable;
    private Object weatherCode;
    private boolean mousePosition;
    private String searchRadiu;
    private List<MapCacheBaseLayerBeanEntity> baseLayers;

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getGisServer() {
        return gisServer;
    }

    public void setGisServer(String gisServer) {
        this.gisServer = gisServer;
    }

    public boolean isWeatherEnable() {
        return weatherEnable;
    }

    public void setWeatherEnable(boolean weatherEnable) {
        this.weatherEnable = weatherEnable;
    }

    public Object getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(Object weatherCode) {
        this.weatherCode = weatherCode;
    }

    public boolean isMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(boolean mousePosition) {
        this.mousePosition = mousePosition;
    }

    public String getSearchRadiu() {
        return searchRadiu;
    }

    public void setSearchRadiu(String searchRadiu) {
        this.searchRadiu = searchRadiu;
    }

    public List<MapCacheBaseLayerBeanEntity> getBaseLayers() {
        return baseLayers;
    }

    public void setBaseLayers(List<MapCacheBaseLayerBeanEntity> baseLayers) {
        this.baseLayers = baseLayers;
    }

}
