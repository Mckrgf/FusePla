package com.supcon.mes.middleware.util;

import android.os.Bundle;
import android.text.TextUtils;

import com.supcon.common.BaseConstant;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangshizhan on 2019/11/14
 * Email:wangshizhan@supcom.com
 */
public class UrlUtil {

    /**
     * @param url
     * @param flag 分割符
     * @return
     */
    public static String getUrl(String url, String flag) {
        int i = url.lastIndexOf(flag);
        return url.substring(i);
    }


    /**
     * 从URL中获取相应的值
     *
     * @param url
     * @param name 指定url的参数名
     * @return 指定参数名的值
     */
    public static String getParamByUrl(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (m.find()) {
            return m.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    /**
     * 获取viewcode
     * 截取URL最后一个斜杠和问号之间的string片段
     *
     * @param url
     * @return
     */
    public static String getPendingViewCode(String url) {

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        if (url.contains("/msService/WTS/workTicket/workTicket/")) {
            if (url.contains(Constant.TableStatus_CH.TICKET_EDIT)) {
                return Constant.Router.WRITE_TICKET;
            }
            return Constant.Router.TICKET_DETAIL;
        }

        int lastSlashPosition = url.lastIndexOf("/") + 1;
        int lastQuestionMarkPosition = url.lastIndexOf("?");

        if (lastQuestionMarkPosition == -1) {
            lastQuestionMarkPosition = url.length();
        }
        String viewCodeStr = url.substring(lastSlashPosition, lastQuestionMarkPosition);
        LogUtil.w("viewCode:" + viewCodeStr);

        return viewCodeStr;
    }

    /**
     * 获取viewcode
     * 截取URL最后一个斜杠和问号之间的string片段
     *
     * @param url
     * @return
     */
    public static String getViewCode(String url) {

        if (TextUtils.isEmpty(url)) {
            return "";
        }
        int lastSlashPosition = url.lastIndexOf("/") + 1;
        int lastQuestionMarkPosition = url.lastIndexOf("?");

        if (lastQuestionMarkPosition == -1) {
            lastQuestionMarkPosition = url.length();
        }
        String viewCodeStr = url.substring(lastSlashPosition, lastQuestionMarkPosition);
        String hazardViewCodeStr = url.substring(0, lastSlashPosition);
        if (hazardViewCodeStr.equals("/msService/SESHRM/accidentRisk/riskHandle/")) {
            return "riskHandle";
        }
        LogUtil.w("viewCode:" + viewCodeStr);
        return viewCodeStr;
    }


    /**
     * 获取移动视图header
     *
     * @return
     */
    public static Map<String, String> getHeaders() {

        Map<String, String> header = new HashMap<>();
        header.put("Cookie", PLAApplication.getCooki());
        header.put("Authorization", PLAApplication.getAuthorization());

        return header;
    }

    /**
     * 获取移动视图Bundle
     *
     * @param url 移动视图url
     * @return bundle
     */
    public static Bundle getWebAppBundle(String url) {
        return getWebAppBundle(url, false);
    }

    public static Bundle getWebAppBundle(String url, Boolean needTitle) {
        return getWebAppBundle(url, needTitle, null);
    }

    public static Bundle getWebAppBundle(String url, Boolean needTitle, String screenType) {
        if (TextUtils.isEmpty(url)) {
            return new Bundle();
        }
        Bundle bundle = getNewWebBundle(url);
        bundle.putBoolean(BaseConstant.WEB_IS_LIST, true);
        bundle.putBoolean(Constant.WebUrl.HAS_TITLE, needTitle);
        if (screenType != null) {
            bundle.putString(Constant.WebUrl.SCREEN_ORIENT, screenType);
        }
        return bundle;
    }





    /**
     * 获取新移动视图Bundle
     *
     * @param url 移动视图url
     * @return bundle
     */
    public static Bundle getNewWebBundle(String url) {
        if (TextUtils.isEmpty(url)) {
            return new Bundle();
        }
        CookieTempUtil.getCookie(PLAApplication.getAppContext());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PLAApplication.getHost());
        stringBuilder.append(url);

        if(!url.contains("clientType")){
            stringBuilder.append("&clientType=" + Constant.CLIENT_TYPE_MOBILE);
        }

        Bundle bundle = new Bundle();
        bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken());
        bundle.putString(BaseConstant.WEB_URL, stringBuilder.toString());

        return bundle;
    }


    public static boolean isMobileWeb(String url) {


        if ("mobileTest".equals(ChannelUtil.getChannel())) {
            return true;
        }

        return !TextUtils.isEmpty(url) && (url.contains(Constant.MobileWeb.TSD_LIST));

    }


}
