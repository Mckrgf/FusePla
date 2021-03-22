package com.supcon.mes.module_webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.supcon.common.com_router.api.IRouter;
import com.supcon.common.com_router.util.RouterManager;
import com.supcon.mes.module_webview.ui.WebActivity;
import java.lang.String;

/**
 * @API intent router created by apt
 * 支持组件化多模块
 * add by wangshizhan
 */
public final class IntentRouter implements IRouter {
  static {
    RouterManager.getInstance().register("web", WebActivity.class);
  }

  /**
   * @created by apt 
   */
  public static void go(Context context, String name, Bundle extra) {
    Intent intent =new Intent();
    if(extra != null)
    	intent.putExtras(extra);
     switch (name) {
      	case "web": 
      		intent.setClass(context, WebActivity.class);
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
