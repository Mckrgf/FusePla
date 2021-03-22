package com.supcon.mes.module_login.ui;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.SwitchButton;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.KeyHelper;
import com.supcon.mes.mbap.utils.PatternUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.LogoutAPI;
import com.supcon.mes.middleware.model.contract.LogoutContract;
import com.supcon.mes.middleware.presenter.LogoutPresenter;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.util.EditTextHelper;

import static com.supcon.mes.mbap.utils.PatternUtil.URL_PATTERN;

/**
 * Created by wangshizhan on 2017/8/16.
 */
@Router(Constant.Router.NETWORK_SETTING)
@Presenter(value = {LogoutPresenter.class})
public class NetworkSettingActivity extends BaseControllerActivity implements LogoutContract.View{
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("rightBtn")
    ImageButton rightBtn;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("ipInput")
    CustomEditText ipInput;
    @BindByTag("urlInput")
    CustomEditText urlInput;
    @BindByTag("portInput")
    CustomEditText portInput;
    @BindByTag("urlSwitchBtn")
    SwitchButton urlSwitchBtn;


    private StringBuilder originalParam = new StringBuilder();

    private boolean isUrlEnabled = false;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_network_setting;
    }

    @Override
    protected void onInit() {
        super.onInit();
        isUrlEnabled = SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL_ENABLE, false);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.login_net_setting));
        rightBtn.setVisibility(View.VISIBLE);
        rightBtn.setImageResource(R.drawable.sl_top_submit);

        urlSwitchBtn.setChecked(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL_ENABLE, false));

        ipInput.setInputType(InputType.TYPE_CLASS_PHONE);

        portInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        EditTextHelper.setEditTextIPInput(ipInput.editText());
        EditTextHelper.setEditTextInhibitInputSpace(urlInput.editText());

        EditTextHelper.setEditTextInhibitInputSpeChat(portInput.editText());

        initHost(isUrlEnabled);

    }



    private void initHost(boolean isUrlEnabled) {
        if(isUrlEnabled){
            urlInput.setVisibility(View.VISIBLE);
            ipInput.setVisibility(View.GONE);
            portInput.setVisibility(View.GONE);
            ipInput.setInput("");
            portInput.setInput("");

            urlInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.URL, ""));

        }
        else{
            urlInput.setVisibility(View.GONE);
            urlInput.setInput("");

            ipInput.setVisibility(View.VISIBLE);
            portInput.setVisibility(View.VISIBLE);


            ipInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.IP, ""));


            portInput.setInput(SharedPreferencesUtils.getParam(this, MBapConstant.SPKey.PORT, ""));
        }
    }


    @Override
    protected void initData() {
        originalParam
                .append(ipInput.getInput())
                .append(portInput.getInput())
                .append(urlInput.getInput());

    }

    @Override
    protected void initListener() {
        super.initListener();

        rightBtn.setOnClickListener(v -> {

            if(checkIsModified()) {
                if(doCheck()){
                    doSave();
                }
            }
            else{
                finish();
                executeBackwardAnim();
            }

        });


        KeyHelper.doActionNext(ipInput.editText(), portInput.editText(), false);
        KeyHelper.doActionNext(portInput.editText(), true, this::onBackPressed
            /*rightBtn.performClick()*/
        );

        leftBtn.setOnClickListener(v -> onBackPressed());

        urlSwitchBtn.setOnCheckedChangeListener((switchButton, b) -> {

            isUrlEnabled = b;

            initHost(isUrlEnabled);

        });

    }

    @Override
    public void onBackPressed() {
        //验证页面是否已被修改
        if (checkIsModified()) {
            new CustomDialog(context)
                    .twoButtonAlertDialog(getString(R.string.login_net_repair_save)+"?")
                    .bindView(R.id.redBtn, getString(R.string.login_save))
                    .bindView(R.id.grayBtn, getString(R.string.login_out))
                    .bindClickListener(R.id.redBtn, v1 -> {
                        if(doCheck()){
                            doSave();
                        }
                    }, true)
                    .bindClickListener(R.id.grayBtn, v3-> super.onBackPressed(),true)
                    .show();


        } else {
            super.onBackPressed();
        }
    }


    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

    }


    private void save() {

        if(isUrlEnabled){
            MBapApp.setUrl(urlInput.getInput().trim());
            LogUtil.d("url:"+ MBapApp.getUrl());

            MBapApp.setIp("");
            MBapApp.setPort("");
            SharedPreferencesUtils.setParam(context, MBapConstant.SPKey.URL_ENABLE, true);
        }
        else {
            MBapApp.setIp(ipInput.getInput().trim());
            LogUtil.d("ip:" + MBapApp.getIp());

            MBapApp.setPort(portInput.getInput().trim());
            LogUtil.d("port:" + MBapApp.getPort());

            MBapApp.setUrl("");
            SharedPreferencesUtils.setParam(context, MBapConstant.SPKey.URL_ENABLE, false);
        }


        Api.getInstance().rebuild();


    }

    private boolean doCheck() {

        if(!isUrlEnabled) {
            if (TextUtils.isEmpty(ipInput.getInput().trim())) {
                ToastUtils.show(this, R.string.login_setting_ip_location);
                return false;
            }

            if (!PatternUtil.checkIP(ipInput.getInput().trim())) {
                ToastUtils.show(this, R.string.login_input_ip_location);
                return false;
            }

            if (TextUtils.isEmpty(portInput.getInput().trim())) {
                ToastUtils.show(this, R.string.login_setting_port);
                return false;
            }

            if (!PatternUtil.checkPort(portInput.getInput().trim())) {
                ToastUtils.show(this, R.string.login_input_sure_port);
                return false;
            }
        }
        else{
            if(TextUtils.isEmpty(urlInput.getInput().trim())){
                ToastUtils.show(this, getString(R.string.login_must_setting_location)+"！");
                return false;
            }

            if(!PatternUtil.checkInput(urlInput.getInput().trim(), URL_PATTERN)){
                ToastUtils.show(this, getString(R.string.login_input_sure_ip_location)+"！");
                return false;
            }
        }


        return true;
    }

    private void doSave() {

        if(MBapApp.isIsLogin()){
            new CustomDialog(context)
                    .twoButtonAlertDialog(getString(R.string.login_net_repair_logging)+"?")
                    .bindView(R.id.redBtn, getString(R.string.login_again_logging))
                    .bindView(R.id.grayBtn, getString(R.string.cancel))
                    .bindClickListener(R.id.redBtn, v1 -> {
                        onLoading(getString(R.string.login_logouting)+"...");
                        presenterRouter.create(LogoutAPI.class).logout();
                    }, true)
                    .bindClickListener(R.id.grayBtn, null,true)
                    .show();
        }
        else{

            save();
            finish();
        }

    }

    /**
     * 验证页面是否已被修改
     */
    private boolean checkIsModified() {

        StringBuilder latestParam = new StringBuilder();
        latestParam.append(ipInput.getInput())
                .append(portInput.getInput())
                .append(urlInput.getInput());

        return !latestParam.toString().equals(originalParam.toString());

    }

    @Override
    public void logoutSuccess() {
        save();
        onLoadSuccessAndExit(getString(R.string.login_out_succeed)+"！", () -> {
            IntentRouter.go(context, Constant.Router.LOGIN);
            finish();
        });
    }

    @Override
    public void logoutFailed(String errorMsg) {
        LogUtil.w("logoutFailed:"+errorMsg);
        onLoadFailed(getString(R.string.login_out_fail)+"！");
    }
}
