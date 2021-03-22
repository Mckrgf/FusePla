package com.supcon.mes.middleware.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.supcon.common.view.util.LogUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wangshizhan on 2019/3/5
 * Email:wangshizhan@supcom.com
 */
public class CookieTempUtil {

    public static void saveCookie(Context context, String sessionId, String CASTGC){

        //先建立数据库存储
        SharedPreferences spf = context.getSharedPreferences("Cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("jsessionId", sessionId);
        editor.putString("CASTGC", CASTGC);
        String cookieString = "JSESSIONID="+sessionId+";CASTGC="+CASTGC;
        LogUtil.e( "cookieString:"+cookieString);
        editor.putString("cookieString", cookieString);
        editor.apply();

    }


    public static void saveCookie(Context context, String cookie){

        //先建立数据库存储
        SharedPreferences spf = context.getSharedPreferences("Cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        String cookieString = getCookie(context);

        String[] cookies;

        if(!TextUtils.isEmpty(cookieString)){
            StringBuilder cookieTemp = new StringBuilder();
            if(cookieString.contains("|")){
                cookies = cookieString.split("\\|");
                for(int i = cookies.length-1; i >=0; i--){

                    String cook = cookies[i];

                    if(cook!=null && cook.equals(cookie)){
                        return;
                    }

                    String left = null;
                    String leftIn = null;
                    if(cook!=null && cook.contains("=") && cookie.contains("=")){
                        left = cook.split("=")[0];
                        leftIn = cookie.split("=")[0];

                        if(left!=null && left.equals(leftIn)){
                            cookies[i] = null;
                        }

                    }

                }
                int size = cookies.length;
                String[] tmp = new String[size + 1];


                for (int i = 0; i < size; i++){

                    if(!TextUtils.isEmpty(cookies[i])){
                        tmp[i] = cookies[i];
                        cookieTemp.append(tmp[i]);
                        cookieTemp.append("|");
                    }
                }
                tmp[size] = cookie;  //在最后添加上需要追加的cookie
                cookieTemp.append(cookie);
            }
            else {
                cookieTemp.append(cookieString);
                cookieTemp.append("|");
                cookieTemp.append(cookie);
            }


            LogUtil.e( "cookieString:"+cookieTemp.toString());
            editor.putString("cookieString", cookieTemp.toString());
        }
        else{
            editor.putString("cookieString", cookie);
            LogUtil.e( "cookieString:"+cookie);
        }


        editor.apply();


    }


    public static String getCookie(Context context){
        String cookie = null;
        SharedPreferences spf = context.getSharedPreferences("Cookie", Context.MODE_PRIVATE);

        cookie = spf.getString("cookieString", "");
//        LogUtil.e( "cookie local:"+cookie);
        return cookie;
    }


    public static void syncCookie(Context context, String url){
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();

        SharedPreferences spf = context.getSharedPreferences("Cookie", MODE_PRIVATE);
        String cookieString = spf.getString("cookieString", "");

        if(TextUtils.isEmpty(cookieString)){
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
        } else {
            cookieManager.removeSessionCookie();
        }

        if(cookieString.contains("|")){
            String[] cookies = cookieString.split("\\|");

            for(String cookie:cookies){
                cookieManager.setCookie(url, cookie);
            }
        }
        else{
            cookieManager.setCookie(url, cookieString);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }


    public static void clearCookie(Context context){
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除
        cookieManager.removeAllCookie();

//        SharedPreferences spf = context.getSharedPreferences("Cookie", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = spf.edit();
//        editor.putString("cookieString", "");
//        editor.apply();
    }

}
