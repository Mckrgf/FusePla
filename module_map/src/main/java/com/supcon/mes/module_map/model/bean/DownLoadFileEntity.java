package com.supcon.mes.module_map.model.bean;

import com.supcon.common.com_http.BaseEntity;

import okhttp3.ResponseBody;

/**
 * Time:    2020-05-30  14: 09
 * Author： nina
 * Des:
 */
public class DownLoadFileEntity extends BaseEntity {
    ResponseBody responseBody;
    MapCacheBaseLayerBeanEntity entity;

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public MapCacheBaseLayerBeanEntity getEntity() {
        return entity;
    }

    public void setEntity(MapCacheBaseLayerBeanEntity entity) {
        this.entity = entity;
    }
}
