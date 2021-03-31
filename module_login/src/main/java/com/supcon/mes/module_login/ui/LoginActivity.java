package com.supcon.mes.module_login.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.blankj.utilcode.util.SPUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.LogUtils;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.utils.PatternUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.LogoutAPI;
import com.supcon.mes.middleware.model.api.TokenAPI;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.AccountInfoDao;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.model.contract.LogoutContract;
import com.supcon.mes.middleware.model.contract.TokenContract;
import com.supcon.mes.middleware.presenter.LogoutPresenter;
import com.supcon.mes.middleware.presenter.TokenPresenter;
import com.supcon.mes.middleware.util.ErrorMsgHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_login.BuildConfig;
import com.supcon.mes.module_login.IntentRouter;
import com.supcon.mes.module_login.R;
import com.supcon.mes.module_login.util.AssetsViewHelper;
import com.supcon.mes.module_login.util.IntentUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by wangshizhan on 2017/8/11.
 */

@Router(Constant.Router.LOGIN)
@Presenter(value = {LogoutPresenter.class, TokenPresenter.class})
public class LoginActivity extends BaseControllerActivity implements LogoutContract.View, TokenContract.View {
    
    
    @BindByTag("usernameInput")
    CustomEditText usernameInput;
    
    @BindByTag("pwdInput")
    CustomEditText pwdInput;
    @BindByTag("loginLogo")
    ImageView loginLogo;
    @BindByTag("loginBg")
    RelativeLayout loginBg;
    
    @BindByTag("buildVersion")
    CustomTextView buildVersion;
    
    @BindByTag("productVersion")
    CustomTextView productVersion;

    @BindByTag("savePasswordIv")
    ImageView savePasswordIv;
    @BindByTag("tvLoginLogoTitle")
    TextView tvLoginLogoTitle;
    @BindByTag("icLoginUser")
    ImageView icLoginUser;
    @BindByTag("icLoginPassword")
    ImageView icLoginPassword;
    @BindByTag("loginSettingLayout")
    TextView loginSettingLayout;
    @BindByTag("loginBtn")
    Button loginBtn;
    @BindByTag("bt_app_mode")
    Button bt_app_mode;

    private boolean isFirstIn = false;
    private boolean logout = false;
    private boolean loginInvalid;
    


    boolean savePassword = false;
    String activityRouter;

    private static final String[] permissionsGroup=new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE
    };


    @Override
    protected int getLayoutID() {
        return R.layout.ac_login;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //无title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onInit() {
        super.onInit();

        loginInvalid = getIntent().getBooleanExtra(Constant.IntentKey.LOGIN_INVALID, false);
        isFirstIn = getIntent().getBooleanExtra(Constant.IntentKey.FIRST_LOGIN, true);
        logout = getIntent().getBooleanExtra(Constant.IntentKey.LOGOUT, false);
        savePassword = SharedPreferencesUtils.getParam(context, Constant.SPKey.SAVE_PASSWORD, false);
        activityRouter = getIntent().getStringExtra(Constant.IntentKey.ACTIVITY_ROUTER);
    }

    public void customMadeLoginStyle(){

        //判断本地assets文件下是否有相同的页面布局，如果有，替换掉该工程下面的
        AssetsViewHelper assetsViewHelper= AssetsViewHelper.with(this);
        View view=assetsViewHelper.getAssetsLayout("ac_login.xml");
        if (view!=null){
            setContentView(view);
            //放在assets文件下的布局是不编译的，findviewById无法使用,需要手动使用findviewBytag
            usernameInput=view.findViewWithTag("usernameInput");
            pwdInput=view.findViewWithTag("pwdInput");
            loginLogo=view.findViewWithTag("loginLogo");
            loginBg=view.findViewWithTag("loginBg");
            buildVersion=view.findViewWithTag("buildVersion");
            productVersion=view.findViewWithTag("productVersion");
            savePasswordIv=view.findViewWithTag("savePasswordIv");
            tvLoginLogoTitle=view.findViewWithTag("tvLoginLogoTitle");
            icLoginUser=view.findViewWithTag("icLoginUser");
            icLoginPassword=view.findViewWithTag("icLoginPassword");
            loginSettingLayout=view.findViewWithTag("loginSettingLayout");
            loginBtn=view.findViewWithTag("loginBtn");
        }
        if (logout) {
            presenterRouter.create(LogoutAPI.class).logout();
        }
//        customMadeLoginImageLogo();
    }

//    public void customMadeLoginImageLogo()  {
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo.png")!=null){
//            loginLogo.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo.png")));
//        }
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo.png")!=null){
//            loginLogo.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo.png")));
//        }
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_bg.png")!=null){
//            loginBg.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_bg.png")));
//        }
//
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo_title.png")!=null){
//            tvLoginLogoTitle.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_logo_title.png")));
//            tvLoginLogoTitle.setText("");
//        }
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_user.png")!=null){
//            icLoginUser.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_user.png")));
//        }
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_password.png")!=null){
//            icLoginPassword.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_password.png")));
//        }
//        if (Util.getAssetsBitmap(LoginActivity.this,"custom_login_btn_bg.png")!=null){
//            loginBtn.setBackground(new BitmapDrawable(getResources(),Util.getAssetsBitmap(LoginActivity.this,"custom_login_btn_bg.png")));
//        }
//
//    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(loginBtn)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(o -> {
                    //如果返回结果为false说明存在格式上的错误
                    if (!checkInput()) {
                        return;
                    }

                    if ((!SharedPreferencesUtils.getParam(context, MBapConstant.SPKey.URL_ENABLE, false))
                            && TextUtils.isEmpty(SharedPreferencesUtils.getParam(context, MBapConstant.SPKey.IP, ""))){
                        ToastUtils.show(context, getString(R.string.login_must_seting_ip)+"!");
                        return;
                    }

                    if (SharedPreferencesUtils.getParam(context, MBapConstant.SPKey.URL_ENABLE, false)
                            && TextUtils.isEmpty(SharedPreferencesUtils.getParam(context, MBapConstant.SPKey.URL, ""))){
                        ToastUtils.show(context, R.string.login_must_setting_service+"!");
                        return;
                    }

                    
                    onLoading(getString(R.string.login_lodding)+"...");
                    presenterRouter.create(TokenAPI.class).getAccessToken(usernameInput.getInput().trim(), pwdInput.getInput());
                    
                });
        
        
        RxView.clicks(loginSettingLayout)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(o -> IntentRouter.go(this, Constant.Router.NETWORK_SETTING)
                );

        savePasswordIv.setOnClickListener(v -> {

            if(savePassword){
                savePasswordIv.setImageResource(R.drawable.ic_choose_no);
                savePassword = false;
            }
            else{
                savePasswordIv.setImageResource(R.drawable.ic_choose_yes);
                savePassword = true;
            }

        });

        bt_app_mode.setOnClickListener(v -> {
            SPUtils.getInstance().put("SP_KEY_APP_MODE", "");
            IntentRouter.go(this, Constant.Router.ROOTACTIVITY);
            finish();
        });

        RxTextView.textChanges(usernameInput.editText())
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {

                        if(TextUtils.isEmpty(charSequence) || !savePassword){
                            return;
                        }
                        List<AccountInfo> accountInfos = PLAApplication.dao().getAccountInfoDao().queryBuilder()
                                .where(AccountInfoDao.Properties.UserName.eq(charSequence.toString().toLowerCase()))
                                .where(AccountInfoDao.Properties.Ip.eq(PLAApplication.getIp()))
                                .list();
                        if(accountInfos!=null && accountInfos.size()!=0){
                            AccountInfo accountInfo = accountInfos.get(0);
                            pwdInput.setContent(accountInfo.password);
                        }
                    }
                });
    }
    
    @Override
    protected void initView() {
        super.initView();
        setSwipeBackEnable(false);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.transparent);


        customMadeLoginStyle();
        usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        pwdInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdInput.editText().setTransformationMethod(PasswordTransformationMethod.getInstance());
        usernameInput.setInput(MBapApp.getUserName());
//        pwdInput.setInput(MBapApp.getPassword());

            StringBuilder versionName = new StringBuilder();
            versionName.append(" V");
            versionName.append(BuildConfig.VERSION_NAME);
            versionName.append(BuildConfig.DEBUG ? "(debug)" : "");

            buildVersion.setValue(versionName.toString());
            productVersion.setContent(BuildConfig.PACKAGE_CODE);


        if(savePassword){
            savePasswordIv.setImageResource(R.drawable.ic_choose_yes);

        }
        else{
            savePasswordIv.setImageResource(R.drawable.ic_choose_no);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        super.initData();
        RxPermissions rxPermissions = new RxPermissions(this);
        Observable.timer(10, TimeUnit.MILLISECONDS)
                .compose(rxPermissions.ensureEachCombined(permissionsGroup))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {

                        LogUtil.d(""+permission.name+" isGrant:"+permission.granted);

                        if(!permission.granted){

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (loginInvalid)
            finish();
        else {
            finish();

            PLAApplication.exitApp();
        }
    }
    
    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();
        
        
        if (loginInvalid) {
            ToastUtils.show(context, getString(R.string.login_logging_invaild_again)+"!");
        }
    }
    
    @SuppressWarnings("all")
    private boolean checkUsername() {
        
        return TextUtils.isEmpty(usernameInput.getInput().trim());
        
    }
    
    @SuppressWarnings("all")
    private boolean checkPwd() {
        
        return TextUtils.isEmpty(pwdInput.getInput());
        
    }
    
    private boolean checkInput() {

        if(TextUtils.isEmpty(usernameInput.getInput().trim()) && TextUtils.isEmpty(pwdInput.getInput().trim())){
            ToastUtils.show(context, getString(R.string.login_name_pass_empty)+"!");
            return false;
        }

        if (TextUtils.isEmpty(usernameInput.getInput().trim())) {
            ToastUtils.show(context, getString(R.string.login_name_empty)+"!");
            return false;
        }
        if (!PatternUtil.checkUserName(usernameInput.getInput().trim()/*,"^[A-Za-z0-9\\u4e00-\\u9fa5]+$"*/)) {
            ToastUtils.show(context, getString(R.string.login_name_pass_wrong)+"!");
            return false;
        }
        //以上只单纯检测纯格式方面的错误
        if (TextUtils.isEmpty(pwdInput.getInput())) {
            ToastUtils.show(context, getString(R.string.login_pass_empty)+"!");
            return false;
        }
        //密码方面并没有特别的限制,所以这里并不特备进行验证,只进行是否为空的验证
        
        return true;
    }

    private void goMain() {
        MBapApp.setIsLogin(true);
        MBapApp.setUserName(usernameInput.getInput().trim());
        MBapApp.setPassword(pwdInput.getInput());
        SharedPreferencesUtils.setParam(context, Constant.SPKey.SAVE_PASSWORD, savePassword);

        if(savePassword){
            AccountInfo accountInfo = PLAApplication.getAccountInfo();
            accountInfo.password = pwdInput.getContent();
            PLAApplication.setAccountInfo(accountInfo);
        }

        onLoadSuccessAndExit(getString(R.string.login_login_succeed)+"!", () -> {

            //跳转到主页
            if (isFirstIn) {

                Bundle bundle = new Bundle();

                if(TextUtils.isEmpty(activityRouter)){
                    IntentRouter.go(this, Constant.Router.BEACON_HOME, bundle);
                    finish();
                }
                else{
                    IntentRouter.go(context, activityRouter, bundle);
                    LogUtils.i("zxcv","activityRouter");
                }


                isFirstIn = false;
            }
            else {
//                IntentRouter.go(context, Constant.Router.MAIN);
                LogUtils.i("zxcv","MAIN");
            }
            LogUtils.i("zxcv","关闭");
        });
        
    }

    @Override
    public void logoutSuccess() {
        com.blankj.utilcode.util.ToastUtils.showLong(getResources().getString(R.string.operation_successful));
    }

    @Override
    public void logoutFailed(String errorMsg) {
        com.blankj.utilcode.util.ToastUtils.showLong(getResources().getString(R.string.operation_failed));
    }

    @Override
    public void getAccessTokenSuccess(TokenEntity entity) {
        if (logout) {
            //如果是web登录失败，就关闭该页面,返回原页面
            finish();
        }else {
            goMain();
        }

    }

    @Override
    public void getAccessTokenFailed(String errorMsg) {
        LogUtil.e(""+errorMsg);

        if(!TextUtils.isEmpty(errorMsg)){

            if(errorMsg.contains("401"))
                onLoadFailed(getString(R.string.login_name_pass_error));
            else if(errorMsg.contains("400")){
                onLoadFailed(getString(R.string.login_wrong_all));
            }
            else{
                onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
            }
        }
        else{
            onLoadFailed(ErrorMsgHelper.msgParse(errorMsg));
        }

    }
}
