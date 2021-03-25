package com.supcon.mes.middleware.util;

import com.supcon.common.view.util.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 资源导入器，用于IntentRouter,WidgetWapper 导入
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public class ModuleClassHelper {

    /**
     * 模块IntentRouter,WidgetWapper
     * 在这里设置模块名，未设置的模块这些资源不会导入
     */
    private static final String[] MODULES = {
            "home", "module_login", "module_beacon","module_webview","middleware","module_map","app",
    };

    private static class MainRouterHolder {
        private static ModuleClassHelper instance = new ModuleClassHelper();
    }


    public static ModuleClassHelper getInstance() {

        return MainRouterHolder.instance;
    }

    List<String> classPaths = new ArrayList<>();

    private ModuleClassHelper() {
        for (String module : MODULES) {
            classPaths.add("com.supcon.mes." + module + ".IntentRouter");
            classPaths.add("com.supcon.mes." + module + ".WidgetWapper");
        }
    }


    public void setup() {

        for (String module : classPaths) {

            try {
                Class clazz = Class.forName(module);

                Method method = clazz.getMethod("setup", new Class[]{});
                method.invoke(null);

            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }

        }
    }

}
