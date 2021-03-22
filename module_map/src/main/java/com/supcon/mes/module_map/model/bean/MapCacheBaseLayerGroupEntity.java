package com.supcon.mes.module_map.model.bean;


import java.util.ArrayList;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class MapCacheBaseLayerGroupEntity extends ArrayList<MapCacheBaseLayerBeanEntity> {

    String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
