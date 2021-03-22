package com.supcon.mes.middleware.common;


import android.text.TextUtils;

import com.supcon.common.view.App;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangshizhan on 2019/11/26
 * Email:wangshizhan@supcom.com
 */
public class BAP5HeadInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl url = chain.request().url();
//        if(url.encodedPath().contains("/cas/mobile/logon") || url.encodedPath().contains("/cas/logout") /*|| url.encodedPath().contains("/auth/oauth/loginCheck")*/){
//            return chain.proceed(chain.request());
//        }

        Request.Builder builder = chain.request().newBuilder()
                .addHeader("USER_AGENT", "Linux; U; Android")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json");

        String cookieStr = null;

        if(Util.isZh(App.getAppContext())){
            cookieStr = "language=zh_CN";
        }
        else {
            cookieStr = "language=en_US";
        }


        if(url.encodedPath().contains("/services/public/") ){
            return chain.proceed(builder
                    .build());
        }

        if(url.encodedPath().contains("/auth/oauth/token")){

            return chain.proceed(builder
                    .addHeader("Authorization", "Basic bW9iaWxlLWNsaWVudDoxMjM0NTY=")
                    .build());

        }
        //针对该接口不添加Authorization header
        if(!url.encodedPath().contains("/msService/public/VxPLS/beacon/list")){

            return chain.proceed(builder
                    .addHeader("Authorization", PLAApplication.getToken())
                    .build());

        }

        if(url.encodedPath().contains("/auth/oauth/loginCheck")){
            return chain.proceed(builder
                    .build());
        }


        if(url.encodedPath().contains(".action")){

            return chain.proceed(builder
                    .addHeader("Cookie", cookieStr+";AuthSessionId="+PLAApplication.getAuthSessionId()+";")
                    .build());
        }

//        String GWSessionId = SharedPreferencesUtils.getParam(MBapApp.getAppContext(), Constant.SPKey.GW_SESSION_ID, "");
//        if(!TextUtils.isEmpty(GWSessionId)){
//            return chain.proceed(builder
//                    .addHeader("Cookie", cookieStr+";GWSessionId="+GWSessionId+";")
//                    .build());
//        }

        return chain.proceed(builder
                .build());
    }



}
