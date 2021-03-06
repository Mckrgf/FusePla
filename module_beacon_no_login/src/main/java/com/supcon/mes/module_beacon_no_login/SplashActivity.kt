package com.supcon.mes.module_beacon_no_login

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.app.annotation.apt.Router
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_beacon_no_login.listener.PermissionListener
import com.supcon.supbeacon.R

@Router(value = Constant.Router.SPLASH)
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
                requestRunTimePermission(arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), object : PermissionListener {
                    override fun onGranted() {  //所有权限授权成功
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
//                        val intent = Intent(this@SplashActivity, RssiCorrectionActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onGranted(grantedPermission: List<String?>) { //授权成功权限集合
                        val nnn = grantedPermission.size
                        Log.i("failed", "" + nnn)
                    }

                    override fun onDenied(deniedPermission: List<String?>?) { //授权失败权限集合
                        ToastUtils.showShort(resources.getString(R.string.need_to_allow_all_permission_required))
                    }
                })
    }
}