package com.supcon.mes.module_beacon.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_beacon.bean.OkResponse;

/**
 * @author : yaobing
 * @date : 2020/11/3 15:31
 * @desc :
 */
@ContractFactory(entites = {OkResponse.class})
public interface GetBeaconsAPI {
    void getBeacons();
}
