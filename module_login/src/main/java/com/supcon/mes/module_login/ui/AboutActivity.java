package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2018/12/20
 * Email:wangshizhan@supcom.com
 */
@Router(Constant.Router.ABOUT)
public class AboutActivity extends BaseActivity {

    @BindByTag("aboutIv")
    ImageView aboutIv;

    @BindByTag("aboutName")
    TextView aboutName;

    @BindByTag("buildVersion")
    TextView buildVersion;

    @BindByTag("productCode")
    TextView productCode;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("tokenLayout")
    LinearLayout tokenLayout;

    @BindByTag("tokenText")
    TextView tokenText;

    @BindByTag("tokenUpdateTime")
    TextView tokenUpdateTime;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_about;
    }


    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.login_about_our));

        aboutIv.setImageResource(R.drawable.ic_app_launcher);
        aboutName.setText(ChannelUtil.getAppName());
        StringBuilder versionName = new StringBuilder();
        versionName.append(BuildConfig.VERSION_NAME);
        versionName.append(BuildConfig.DEBUG ? "(debug)" : "(release)");

        buildVersion.setText(versionName.toString());

        productCode.setText(BuildConfig.PACKAGE_CODE);

        if("mobileTest".equals(ChannelUtil.getChannel())){
            tokenLayout.setVisibility(View.VISIBLE);
            tokenText.setText(PLAApplication.getToken());

            long expireTime = SharedPreferencesUtils.getParam(context, Constant.SPKey.ACCESS_TOKEN_VALID_TIME, 0L);

            if (expireTime !=0) {
                tokenUpdateTime.setText("失效时间:"+DateUtil.dateTimeFormat(expireTime));
            }

        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        back();
                    }
                });

    }
}
