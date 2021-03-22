package com.supcon.mes.middleware.common;

import android.text.TextUtils;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.network.BaseInterceptor;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.CookieTempUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by wangshizhan on 2020/3/3
 * Email:wangshizhan@supcom.com
 */
public class BAP5ResponseInterceptor extends BaseInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response response = chain.proceed(chain.request());
        HttpUrl url = response.request().url();

        Buffer buffer = getBuffer(response);

        String content = readContent(response, buffer);


        /*if(url.encodedPath().contains("/cas/mobile/logon")){

            if (content.contains("true") && content.contains("cinfo")) {
                Log.w("LoginInterceptor", "登陆成功");
                getBAPOldCookies(response);

                buffer.clear();

                JSONObject jsonObject = new JSONObject();
                JSONObject resultObject = new JSONObject();
                try {
                    resultObject.put("cname", XmlUtil.getStringByTag(content, "NAME"));
                    resultObject.put("cid", XmlUtil.getStringByTag(content, "ID"));
                    jsonObject.put("success", true);
                    jsonObject.put("result", resultObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                buffer.write(jsonObject.toString().getBytes());


                buffer.flush();
                return response;
            }
        }*/

        if (content.contains("记住用户名")) {

            buffer.clear();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("success", false);
                jsonObject.put("code", 401);
                jsonObject.put("msg", "Unauthorized");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            buffer.write(jsonObject.toString().getBytes());

            buffer.flush();
            return response;
        }

        if(url.encodedPath().contains("/auth/oauth/loginCheck")){
            getBAPAuthCookies(response);
        }

//        if(url.encodedPath().contains("/foundation/workbench/timer.action")){
//            getBAPOldCookies(response);
//        }

//        if(url.encodedPath().contains("/msService/baseService/inner/company/getAllCompanies")
//                && TextUtils.isEmpty(SharedPreferencesUtils.getParam(MBapApp.getAppContext(), Constant.SPKey.GW_SESSION_ID,""))){
//            getGWSessionId(response);
//        }

        return response;

    }

    private void getGWSessionId(Response response) {

        Headers headers = response.headers();

        List<String> cookies = headers.values("Set-Cookie");

        for(int i = 0; i < cookies.size(); i++){

            String cookie = cookies.get(i);

            if(cookie.contains("GWSessionId")){

                if(cookie.contains(";")){
                    String[] cookieStrs = cookie.split(";");
                    for(String cookStr:cookieStrs){
                        if(cookStr.contains("GWSessionId")){
                            LogUtil.w("GWSessionId:"+cookStr);
                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.GW_SESSION_ID, cookStr.split("=")[1]);
                        }
                    }
                }

            }

        }

    }


    private void getBAPAuthCookies(Response response){

        Headers headers = response.headers();

        List<String> cookies = headers.values("Set-Cookie");

        for(int i = 0; i < cookies.size(); i++){

            String cookie = cookies.get(i);

            if(cookie.contains("AuthSessionId")){

                if(cookie.contains(";")){
                    String[] cookieStrs = cookie.split(";");
                    for(String cookStr:cookieStrs){
                        if(cookStr.contains("AuthSessionId")){
                            cookie = cookStr.split("=")[1];
                        }
                    }
                }

                SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.AUTH_SESSION_ID, cookie);

                CookieTempUtil.saveCookie(MBapApp.getAppContext(), "AuthSessionId="+cookie+";");
                CookieTempUtil.syncCookie(MBapApp.getAppContext(), Api.getInstance().getBaseUrl());

                break;
            }

        }
    }

    private void getBAPOldCookies(Response response){

//        String jsessionId ="";
//        String CASTGC ="";
//
//        Headers headers = response.headers();
//
//        List<String> cookies = headers.values("Set-Cookie");
//
//        for(int i = 0; i < cookies.size(); i++){
//
//            String cookie = cookies.get(i);
//
//            if(cookie.contains("JSESSIONID")){
//
//                if(cookie.contains(";")){
//                    String[] cookieStrs = cookie.split(";");
//                    for(String cookStr:cookieStrs){
//                        if(cookStr.contains("JSESSIONID")){
//                            jsessionId = cookStr.split("=")[1];
//                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, jsessionId);
//                        }
//                    }
//                }
//
//            }
//
//            if(cookie.contains("CASTGC")){
//
//                if(cookie.contains(";")){
//                    String[] cookieStrs = cookie.split(";");
//                    for(String cookStr:cookieStrs){
//                        if(cookStr.contains("CASTGC")){
//                            CASTGC = cookStr.split("=")[1];
//                            SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, CASTGC);
//                        }
//                    }
//                }
//
//            }
//            if(!TextUtils.isEmpty(jsessionId)) {
//                CookieUtil.saveCookie(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, jsessionId);
//            }
//            if(!TextUtils.isEmpty(CASTGC)) {
//                CookieUtil.saveCookie(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, CASTGC);
//            }
//            CookieUtil.syncCookie(MBapApp.getAppContext(), Api.getInstance().getBaseUrl());
//        }
    }

}
