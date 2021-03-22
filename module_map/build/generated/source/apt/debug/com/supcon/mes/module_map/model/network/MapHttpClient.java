package com.supcon.mes.module_map.model.network;

import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_map.model.bean.MapCacheConfigEntity;
import io.reactivex.Flowable;
import java.lang.String;
import okhttp3.ResponseBody;

/**
 * @API factory created by apt
 */
public final class MapHttpClient {
  /**
   * @created by apt
   */
  public static Flowable<BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>>> getBaseConfigInfoForMobile(
      ) {
    return Api.getInstance().retrofit.create(ApiService.class).getBaseConfigInfoForMobile().compose(RxSchedulers.io_main());
  }

  /**
   * @created by apt
   */
  public static Flowable<ResponseBody> downloadFile(String name) {
    return Api.getInstance().retrofit.create(ApiService.class).downloadFile(name).compose(RxSchedulers.io_main());
  }
}
