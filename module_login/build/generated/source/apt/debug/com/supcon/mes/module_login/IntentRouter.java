package com.supcon.mes.module_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.supcon.common.com_router.api.IRouter;
import com.supcon.common.com_router.util.RouterManager;
import com.supcon.mes.module_login.ui.AboutActivity;
import com.supcon.mes.module_login.ui.LoginActivity;
import com.supcon.mes.module_login.ui.NetworkSettingActivity;
import com.supcon.mes.module_login.ui.WelcomeActivity;
import java.lang.String;

/**
 * @API intent router created by apt
 * 支持组件化多模块
 * add by wangshizhan
 */
public final class IntentRouter implements IRouter {
  static {
    RouterManager.getInstance().register("ABOUT", AboutActivity.class);
    RouterManager.getInstance().register("login", LoginActivity.class);
    RouterManager.getInstance().register("NETWORK_SETTING", NetworkSettingActivity.class);
    RouterManager.getInstance().register("welcome", WelcomeActivity.class);
  }

  /**
   * @created by apt 
   */
  public static void go(Context context, String name, Bundle extra) {
    Intent intent =new Intent();
    if(extra != null)
    	intent.putExtras(extra);
     switch (name) {
      	case "ABOUT": 
      		intent.setClass(context, AboutActivity.class);
      		break;
      	case "login": 
      		intent.setClass(context, LoginActivity.class);
      		break;
      	case "NETWORK_SETTING": 
      		intent.setClass(context, NetworkSettingActivity.class);
      		break;
      	case "welcome": 
      		intent.setClass(context, WelcomeActivity.class);
      		break;
      default: 
      		RouterManager routerManager = RouterManager.getInstance();
      		Class destinationClass = routerManager.getDestination(name);
      		if(destinationClass == null) return;
      		intent.setClass(context, destinationClass);
      		break;
    }
    context.startActivity(intent);
  }

  /**
   * @created by apt */
  public static void go(Context context, String name) {
    go(context, name, null);
  }

  /**
   * @created by apt */
  public static void setup() {
  }
}
