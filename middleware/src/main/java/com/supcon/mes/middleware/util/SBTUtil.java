package com.supcon.mes.middleware.util;

import android.os.Build;

import com.supcon.common.view.util.LogUtil;

/**
 * Created by wangshizhan on 2019/9/27
 * Email:wangshizhan@supcom.com
 */
public class SBTUtil {

    /**
     * 是否支持超高频
     * @return 是否支持超高频
     */
    public static boolean isSupportUHF(){
        return Build.MODEL.contains("T50")
                || Build.MODEL.contains("T55")
                || Build.MODEL.contains("T60")
                || Build.MODEL.contains("SD60RT")
                || Build.MODEL.contains("SD55UHF")
                ;

    }


    /**
     * 是否支持测温
     * @return 是否支持测温
     */
    public static boolean isSupportTemp(){
        return Build.MODEL.contains("T50")
                || Build.MODEL.contains("T55")
                ;

    }

    public static boolean isSBT(){
        LogUtil.e(""+Build.MODEL);
        return Build.MODEL.contains("T50")
                || Build.MODEL.contains("T55")
                || Build.MODEL.contains("T60")
                || Build.MODEL.contains("SD60")
                || Build.MODEL.contains("SD55")
                ;

    }

}
