package com.supcon.mes.middleware.common;

import com.supcon.common.view.App;
import com.supcon.common.view.util.NetWorkUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangshizhan on 2018/1/26.
 * Email:wangshizhan@supcon.com
 */

public class BAP5NetworrkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetWorkUtil.isNetConnected(App.getAppContext())) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (response != null && response.isRedirect()) {
            //如果是重定向，则不做缓存
            return response;
        }


/*        if (NetWorkUtil.isNetConnected(App.getAppContext())) {
            int maxAge = 60; // read from cache for 1 minute
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }*/
        return response;
    }
}

