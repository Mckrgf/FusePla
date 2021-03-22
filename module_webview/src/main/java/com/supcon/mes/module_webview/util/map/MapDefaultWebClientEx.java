package com.supcon.mes.module_webview.util.map;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.js.BaseBridgeWebViewClient;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_webview.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Time:    2020-06-01  19: 33
 * Author： nina
 * Des:
 */
public class MapDefaultWebClientEx extends BaseBridgeWebViewClient {
    Context context;

    public MapDefaultWebClientEx(BridgeWebView webView, Context context) {
        super(webView);

        this.context = context;

        showTipOpenCache();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        //截获资源图片
        //http://192.168.95.164:8080/greenDill/static/GISModel/JZ/0/22/0/0/0/0.b3dm
        if (request != null && request.getUrl() != null && StringUtil.contains(request.getUrl().getPath(), FixedParamKey.map_load_path)){
            try {
                String urlPath = request.getUrl().getPath();
                LogUtil.e("urlPath: " + urlPath);
                String urlLast = null;
                int index = StringUtil.lastIndexOf(urlPath, "GISModel/");
                if (urlPath.length() > index + 10) {
                    urlLast = StringUtil.substring(urlPath, index + 9);
                }

                String jpgPath = FileHandleUtil.filePath + urlLast;
                LogUtil.e(jpgPath);
                File file = new File(jpgPath);
                if (file.exists()) {
                    FileInputStream localCopy = new FileInputStream(file);
                    WebResourceResponse response = new WebResourceResponse("image/png", "UTF-8", localCopy);
                    return response;
                } else {
                    return super.shouldInterceptRequest(view, request);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    protected boolean dealUrl(WebView view, String url) {
        return false;
    }

    private void showTipOpenCache() {
        boolean isFirst = SharedPreferencesUtils.getParam(context, Constant.SPKey.FIRST_OPEN_MAP, true);
        if (isFirst) {
            ToastUtils.show(context, context.getString(R.string.web_map_cache_open_tip));
            SharedPreferencesUtils.setParam(context, Constant.SPKey.FIRST_OPEN_MAP, false);
        }
    }

}
