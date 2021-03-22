package com.supcon.mes.module_map.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_map.model.bean.DownLoadFileEntity;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerBeanEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {CommonEntity.class, DownLoadFileEntity.class})
public interface GetMapConfigAPI {
    void getBaseConfigInfoForMobile( );
    void downLoadFile(MapCacheBaseLayerBeanEntity packageName);
}

