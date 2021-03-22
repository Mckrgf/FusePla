package com.supcon.mes.middleware.model.network;

import com.app.annotation.apt.ApiFactory;
import com.google.gson.JsonObject;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.TokenEntity;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wangshizhan on 2018/7/10
 * Email:wangshizhan@supcom.com
 */
@ApiFactory(name = "MiddlewareHttpClient")
public interface NetworkAPI {


    /**
     * 登陆并获取token
     *
     * @param loginMap
     * @return
     */
    @POST("/auth/oauth/token")
    Flowable<TokenEntity> getToken(@QueryMap Map<String, Object> loginMap);



    /**
     * 获取powerCode(单据已存在 即存在pending)
     *
     * @param codes 操作编码
     * @return
     */
    @GET("/msService/baseService/powerCode/getPowerCode")
    Flowable<BAP5CommonEntity<JsonObject>> getPowerCode(@Query("codes") String codes);


    /**
     * 登出接口
     *
     * @param authenticationCode
     * @return
     */
    @POST("/msService/user/kickOut")
    @FormUrlEncoded
    Flowable<BAP5CommonEntity> kickOut(@Field("authenticationCode") String authenticationCode);

}
