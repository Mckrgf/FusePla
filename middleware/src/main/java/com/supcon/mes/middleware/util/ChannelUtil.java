package com.supcon.mes.middleware.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.supcon.mes.middleware.PLAApplication;

/**
 * Created by wangshizhan on 2019/4/30
 * Email:wangshizhan@supcom.com
 */
public class ChannelUtil {

    public static String getUMengChannel() {
        ApplicationInfo appInfo = null;
        String channelValue = "";
        try {
            appInfo = PLAApplication.getAppContext().getPackageManager().getApplicationInfo(PLAApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object channelId = appInfo.metaData.get("CHANNEL_VALUE");
            channelValue = TextUtils.isEmpty(channelId.toString()) ? "hongshi" : channelId.toString();
        } catch (PackageManager.NameNotFoundException e) {
            channelValue = "hongshi";
            e.printStackTrace();
        }
        return channelValue;
    }

    public static String getChannel() {
        ApplicationInfo appInfo = null;
        String channelValue = "";
        try {
            appInfo = PLAApplication.getAppContext().getPackageManager().getApplicationInfo(PLAApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object channelId = appInfo.metaData.get("CHANNEL_VALUE");
            channelValue = TextUtils.isEmpty(channelId.toString()) ? "hongshi" : channelId.toString();
        } catch (PackageManager.NameNotFoundException e) {
            channelValue = "hongshi";
            e.printStackTrace();
        }
        return channelValue;
    }

    public static String getAppName() {
        ApplicationInfo appInfo = null;
        String value = "";
        try {
            appInfo = PLAApplication.getAppContext().getPackageManager().getApplicationInfo(PLAApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object appName = appInfo.metaData.get("APP_NAME");
            value = TextUtils.isEmpty(appName.toString()) ? "移动设备管理" : appName.toString();
        } catch (PackageManager.NameNotFoundException e) {
            value = "移动设备管理";
            e.printStackTrace();
        }
        return value;
    }


    public static String getAppPackage() {
        ApplicationInfo appInfo = null;
        String value = "";
        try {
            appInfo = PLAApplication.getAppContext().getPackageManager().getApplicationInfo(PLAApplication.getAppContext().getPackageName(), PackageManager.GET_META_DATA);
            Object appPackage = appInfo.metaData.get("APP_SHARE_ID");
            value = TextUtils.isEmpty(appPackage.toString()) ? "com.supcon.mes.eam" : appPackage.toString();
        } catch (PackageManager.NameNotFoundException e) {
            value = "com.supcon.mes.eam";
            e.printStackTrace();
        }
        return value;
    }



}
