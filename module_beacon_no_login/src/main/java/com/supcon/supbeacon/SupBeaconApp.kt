package com.supcon.supbeacon

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.inuker.bluetooth.library.BluetoothClient
import com.supcon.supbeacon.net.OkhttpConfig
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk

/**
 * @author : yaobing
 * @date   : 2020/8/3 14:13
 * @desc   :
 */
class SupBeaconApp: Application() {

    override fun onCreate() {

        super.onCreate()
        mClient = BluetoothClient(this)
        Utils.init(this)
        OkhttpConfig().initOkhttpConfig(this)
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String,Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true;
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true;
        QbSdk.initTbsSettings(map)
        CrashHandler.getInstance().init(this)
    }

    companion object {
        public var mClient : BluetoothClient? = null

        private var instance: SupBeaconApp? = null
            get() {
                if (field == null) {
                    field = SupBeaconApp()
                }
                return field
            }

        fun get(): SupBeaconApp {
            return instance!!
        }
    }
}