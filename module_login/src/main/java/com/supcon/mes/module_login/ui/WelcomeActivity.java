package com.supcon.mes.module_login.ui;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.app.annotation.apt.Router;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;

/**
 * Created by wangshizhan on 2017/8/11.
 */
@Router(Constant.Router.WELCOME)
public class WelcomeActivity extends Activity {


    private long welcomeStartTime=100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome);
//        if (Util.getAssetsBitmap(WelcomeActivity.this,"bg_welcome.png")!=null){
//            getWindow().getDecorView().setBackground(new BitmapDrawable(Util.getAssetsBitmap(WelcomeActivity.this,"bg_welcome.png")));
//        }else{
//            getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome);
//        }
        getWindow().getDecorView().setBackgroundResource(R.drawable.layer_welcome);

        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            Bundle bundle = new Bundle();

            if (MBapApp.isIsLogin()) {
                IntentRouter.go(WelcomeActivity.this, Constant.Router.BEACON_HOME, bundle);
            } else {
//                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo);
                bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, true);
                IntentRouter.go(WelcomeActivity.this, Constant.Router.LOGIN, bundle);
            }
            
            finish();
        }, welcomeStartTime);
    }


}
