package com.supcon.mes.module_map.model.network;

import com.app.annotation.apt.ApiFactory;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_map.model.bean.MapCacheConfigEntity;
import com.supcon.mes.module_webview.util.map.FixedParamKey;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/3/23.
 */
@ApiFactory(name = "MapHttpClient")
public interface ApiService {


    /**
     * @return
     */
    //http://192.168.95.164:8080/msService/SESGISConfig/globalConfig/basicInfoSet/getBaseConfigInfoForMobile
    @GET("/msService/SESGISConfig/globalConfig/basicInfoSet/getBaseConfigInfoForMobile")
    Flowable<BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>>> getBaseConfigInfoForMobile();

    @GET(FixedParamKey.map_load_path + "{name}")
    Flowable<ResponseBody> downloadFile(@Path("name") String name);
}
