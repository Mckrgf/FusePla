package com.supcon.mes.module_map.ui;

import android.view.View;

import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.js.BridgeHandler;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.MapResultEntity;
import com.supcon.mes.middleware.model.event.SelectMapEvent;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_map.R;
import com.supcon.mes.module_webview.ui.WebActivity;
import com.supcon.mes.module_webview.util.map.MapDefaultWebClientEx;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Time:    2020-05-11  10: 31
 * Author： nina
 * Des:
 * point：{"param":{"coordinates":[{"lon":120.13419593553726,"lat":30.18031639410836,"hei":11.77}],"buildingPatchId":null,"floor":null}}
 * line：{"param":{"coordinates":[{"lon":120.13307983220338,"lat":30.18143770488222,"hei":6.68},{"lon":120.1337529614409,"lat":30.1814153375503,"hei":6.68}],"buildingPatchId":null,"floor":null}}
 */
@Router(Constant.Router.MAP)
public class MapActivity extends WebActivity {
    String clickJson;


    @Override
    protected void initView() {
        super.initView();

        setTitle(getString(R.string.map_title));
    }

    @Override
    protected void initListener() {
        super.initListener();
        webView.registerHandler("mobilejs", new BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        LogUtil.i("mobilejs" + data);
                        clickJson = data;

                        if (rightBtn.getVisibility() != View.VISIBLE){
                            rightBtn.setImageResource(R.drawable.sl_top_submit);
                            rightBtn.setVisibility(View.VISIBLE);
                        }
                    }

                });

        RxView.clicks(rightBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

        webView.setWebViewClient(new MapDefaultWebClientEx(webView, context));
    }

    @Override
    public void onBackPressed() {
        //首先 发布广播 告诉它点击的返回的数据，然后调用父类方法 finish掉
        if (!StringUtil.isBlank(clickJson)) {
            MapResultEntity entity = GsonUtil.gsonToBean(clickJson, MapResultEntity.class);
            SelectMapEvent selectMapEvent = new SelectMapEvent(entity, "");
            EventBus.getDefault().post(selectMapEvent);
        }

        super.onBackPressed();
    }
}
