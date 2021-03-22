package com.supcon.mes.module_map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.supcon.common.com_router.api.IRouter;
import com.supcon.common.com_router.util.RouterManager;
import com.supcon.mes.module_map.ui.MapActivity;
import com.supcon.mes.module_map.ui.MapCacheSettingActivity;
import java.lang.String;

/**
 * @API intent router created by apt
 * 支持组件化多模块
 * add by wangshizhan
 */
public final class IntentRouter implements IRouter {
  static {
    RouterManager.getInstance().register("map", MapActivity.class);
    RouterManager.getInstance().register("MAP_CACHE_SETTING", MapCacheSettingActivity.class);
  }

  /**
   * @created by apt 
   */
  public static void go(Context context, String name, Bundle extra) {
    Intent intent =new Intent();
    if(extra != null)
    	intent.putExtras(extra);
     switch (name) {
      	case "map": 
      		intent.setClass(context, MapActivity.class);
      		break;
      	case "MAP_CACHE_SETTING": 
      		intent.setClass(context, MapCacheSettingActivity.class);
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
