package com.supcon.mes.middleware.model.network;

import com.google.gson.JsonObject;
import com.supcon.common.com_http.util.RxSchedulers;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import io.reactivex.Flowable;
import java.lang.Object;
import java.lang.String;
import java.util.Map;

/**
 * @API factory created by apt
 */
public final class MiddlewareHttpClient {
  /**
   * @created by apt
   */
  public static Flowable<TokenEntity> getToken(Map<String, Object> loginMap) {
    return Api.getInstance().retrofit.create(NetworkAPI.class).getToken(loginMap).compose(RxSchedulers.io_main());
  }

  /**
   * @created by apt
   */
  public static Flowable<BAP5CommonEntity<JsonObject>> getPowerCode(String codes) {
    return Api.getInstance().retrofit.create(NetworkAPI.class).getPowerCode(codes).compose(RxSchedulers.io_main());
  }

  /**
   * @created by apt
   */
  public static Flowable<BAP5CommonEntity> kickOut(String authenticationCode) {
    return Api.getInstance().retrofit.create(NetworkAPI.class).kickOut(authenticationCode).compose(RxSchedulers.io_main());
  }
}
