package com.supcon.mes.module_beacon

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.common.view.base.activity.BaseWebViewActivity
import com.supcon.common.view.util.StatusBarUtils
import com.supcon.common.view.view.js.BridgeWebViewClientNew
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_beacon.IntentRouter


@Suppress("DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
open class BaseWebActivity : BaseWebViewActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)
        webView.webViewClient = object : BridgeWebViewClientNew(webView) {
            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                LogUtils.d("cnmcnmcnm")
                if (errorResponse?.statusCode == 401) {
                    ToastUtils.showLong(resources.getString(R.string.please_login))

//                bundle.putInt(Constant.IntentKey.LOGIN_LOGO_ID, R.drawable.ic_login_logo);
                    val bundle = Bundle()
                    bundle.putBoolean(Constant.IntentKey.FIRST_LOGIN, true)
                    bundle.putBoolean(Constant.IntentKey.LOGOUT, true)
                    IntentRouter.go(this@BaseWebActivity, Constant.Router.LOGIN, bundle)
                }else {
                    ToastUtils.showLong("" + errorResponse?.statusCode  + errorResponse?.reasonPhrase)
                }
            }
        }
    }

    override fun initView() {
        super.initView()
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white)
        setStatusBarTextColor(true)
    }

    open fun setStatusBarTextColor( lightStatusBar: Boolean) {
        // 设置状态栏字体颜色 白色与深色
        val decor: View = window.decorView
        var ui: Int = decor.systemUiVisibility
        ui = ui or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ui = if (lightStatusBar) {
                ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
        decor.systemUiVisibility = ui
    }

    override fun getLayoutID(): Int {
        return 0
    }
}