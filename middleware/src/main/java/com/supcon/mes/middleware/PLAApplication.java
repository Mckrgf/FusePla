package com.supcon.mes.middleware;


import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.cache.CacheUtil;
import com.supcon.mes.middleware.common.BAP5HeadInterceptor;
import com.supcon.mes.middleware.common.BAP5NetworrkInterceptor;
import com.supcon.mes.middleware.common.BAP5ResponseInterceptor;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.AccountInfoDao;
import com.supcon.mes.middleware.model.bean.ContactEntity;
import com.supcon.mes.middleware.model.bean.ContactEntityDao;
import com.supcon.mes.middleware.model.bean.DaoMaster;
import com.supcon.mes.middleware.model.bean.DaoSession;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.util.ChannelUtil;
import com.supcon.mes.middleware.util.CookieTempUtil;
import com.supcon.mes.middleware.util.CrashHandler;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.middleware.util.ModuleClassHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_beacon.net.OkhttpConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangshizhan on 2017/8/11.
 */

public class PLAApplication extends MBapApp {

    private static DaoSession daoSession;
    private static AccountInfo accountInfo; //用户信息
    private static ContactEntity staffEntity;

    public static AccountInfo getAccountInfo() {
        if (accountInfo == null) {
            List<AccountInfo> accountInfos = dao().getAccountInfoDao().queryBuilder()
                    .where(AccountInfoDao.Properties.UserName.eq(TextUtils.isEmpty(getUserName()) ? "" :
                            getUserName().toLowerCase()), AccountInfoDao.Properties.Ip.eq(getIp()))
                    .list();
            if (accountInfos != null && accountInfos.size() != 0) {
                accountInfo = accountInfos.get(0);
            }
        }

//        List<AccountInfo> accountInfos = SupPlantApplication.dao().getAccountInfoDao().loadAll();
//        List<StaffEntity> staffEntities = SupPlantApplication.dao().getStaffEntityDao().loadAll();
//        List<CompanyEntity> companyEntities = SupPlantApplication.dao().getCompanyEntityDao().loadAll();


        if (accountInfo == null) {
            return new AccountInfo();
        }

        return accountInfo;
    }

    public static void setStaffEntity(ContactEntity staffEntity) {
        PLAApplication.staffEntity = staffEntity;
        if (accountInfo != null) {
            accountInfo.staff = staffEntity;
        }
    }

    public static void setAccountInfo(AccountInfo accountInfo) {
        PLAApplication.accountInfo = accountInfo;
        if (accountInfo != null) {
            dao().getAccountInfoDao().insertOrReplace(accountInfo);
        }

    }

    public static ContactEntity me() {

        if (staffEntity != null) {
            return staffEntity;
        }

//        if(accountInfo.getStaff()!=null){
//            staffEntity = accountInfo.staff;
//            return staffEntity;
//        }

        if(accountInfo == null){
            return null;
        }

        staffEntity = dao().getContactEntityDao().queryBuilder()
                .where(ContactEntityDao.Properties.StaffId.eq(accountInfo.staffId)).unique();
        if (staffEntity != null) {
            return staffEntity;
        }

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        CrashHandler.getInstance().init(this, Constant.CRASH_PATH);

        if (isIsAlone()) {

        }

        FileUtil.createDir(Constant.FILE_PATH);

        Api.setHeadInterceptor(new BAP5HeadInterceptor());
        Api.setResponseInterceptor(new BAP5ResponseInterceptor());
        Api.setFollowRedirects(true);


        Util.initPhotoError();//解决android7.0以上系统拍照BUG
        setupDatabase();
        initRouter();
        initIP();
        iniData();

        Utils.init(this);
        ToastUtils.setMsgColor(getResources().getColor(R.color.black));
        ToastUtils.setBgColor(getResources().getColor(R.color.white));
        new OkhttpConfig().initOkhttpConfig(this);

    }


    private void iniData() {

        initCookie();
    }

    private void initCookie() {

        String language = null;

        if(Util.isZh(App.getAppContext())){
            language = "zh_CN";
        }
        else {
            language = "en_US";
        }
        CookieTempUtil.saveCookie(getAppContext(), "language="+language+";");
        CookieTempUtil.syncCookie(MBapApp.getAppContext(), Api.getInstance().getBaseUrl());
    }


    private void initRouter() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ModuleClassHelper.getInstance().setup();
        } else {
            ModuleClassHelper.getInstance().setup();
        }
    }


    public void initIP() {

        String port = " ";
        String ip = SharedPreferencesUtils.getParam(getAppContext(), MBapConstant.SPKey.IP, "");
        if (!BuildConfig.DEBUG && TextUtils.isEmpty(ip)) {

                ip = "192.168.95.165";
                port = "8080";
                ip = "192.168.95.165";
                port = "8080";



            setIp(ip);
            setPort(port);

        }
        else if(TextUtils.isEmpty(ip)){
            ip = "192.168.95.165";
            port = "8080";

            setIp(ip);
            setPort(port);

        }


    }


    public static boolean isChannel(String chan) {

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals(chan)) {
            return true;
        }

        return false;
    }

    public static boolean isDev() {

        String channel = ChannelUtil.getUMengChannel();
        if (channel.equals("dev")) {
            return true;
        }

        return false;
    }

    public static SQLiteDatabase getWritableDb() {
        //创建数据库equipment.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(isIsAlone() ? getAppContext() : getAppContext(), "pla.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        return db;
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(getWritableDb());
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession dao() {

        return daoSession;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }


    public static String getHost() {

        StringBuilder sb = new StringBuilder("http://");

        if (SharedPreferencesUtils.getParam(getAppContext(), MBapConstant.SPKey.URL_ENABLE, false)) {
            sb.append(SharedPreferencesUtils.getParam(getAppContext(), MBapConstant.SPKey.URL, ""));
        } else {
            sb.append(getIp());
            sb.append(":");
            sb.append(getPort());
        }


        return sb.toString();
    }


    public static String getToken() {
        String token = "";

        String tokenCache = SharedPreferencesUtils.getParam(getAppContext(), Constant.SPKey.ACCESS_TOKEN, "");

        if (!TextUtils.isEmpty(tokenCache)) {
            TokenEntity tokenEntity = GsonUtil.gsonToBean(tokenCache, TokenEntity.class);
            token = tokenEntity.getTokenType() + " " + tokenEntity.getAccessToken();
        }


        return token;
    }

    /**
     * 旧平台action接口参数
     *
     * @return
     */
    public static String getAuthSessionId() {

        String cookies = SharedPreferencesUtils.getParam(getAppContext(), Constant.SPKey.AUTH_SESSION_ID, "");
        return cookies;
    }


    public static void exitMain() {
        if (getAppContext().store.size() > 1) {
            for (Activity activity : getAppContext().store) {
                if (activity.getClass().getSimpleName().equals("MainActivity")) {
                    activity.finish();
                }
            }
        }
    }


    public static void exitOther(Activity myActivity) {
        if (getAppContext().store.size() > 1) {
            for (Activity activity : getAppContext().store) {
                if (!activity.getClass().getSimpleName().equals(myActivity.getClass().getSimpleName())) {
                    activity.finish();
                }
            }
        }
    }


    public static void exitActivity(String activityName) {
        if (getAppContext().store.size() > 1) {
            for (Activity activity : getAppContext().store) {
                if (activity.getClass().getSimpleName().equals(activityName)) {
                    activity.finish();
                }
            }
        }
    }
}
