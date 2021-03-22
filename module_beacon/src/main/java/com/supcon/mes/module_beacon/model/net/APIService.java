package com.supcon.mes.module_beacon.model.net;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.module_beacon.bean.OkResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * @author : yaobing
 * @date : 2020/11/3 15:39
 * @desc :
 */
@ApiFactory(name = "ModuleBeaconHttpClient")
public interface APIService {
    @GET("/msService/public/VxPLS/beacon/list?currentPage=1&pageSize=1300")
    Flowable<OkResponse> getBeaconList(@Header("Authorization") String lang);
}
