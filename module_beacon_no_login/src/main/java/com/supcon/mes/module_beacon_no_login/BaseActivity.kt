package com.supcon.mes.module_beacon_no_login

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.lzy.okgo.OkGo
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_beacon_no_login.customUI.DigitalEditText
import com.supcon.mes.module_beacon_no_login.listener.PermissionListener
import com.supcon.mes.module_beacon_no_login.utils.NetUtil
import com.supcon.supbeacon.R
import java.util.*

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar)
        super.onCreate(savedInstanceState)

//        supportActionBar!!.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT //设置状态栏颜色透明
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val firstChildAtDecorView = (window.decorView as ViewGroup).getChildAt(0) as ViewGroup
            val statusView = View(this)
            val statusViewLp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight()
            )
            //颜色的设置可抽取出来让子类实现之
            statusView.setBackgroundColor(resources.getColor(R.color.white))
            firstChildAtDecorView.addView(statusView, 0, statusViewLp)
        }
        setAndroidNativeLightStatusBar(this,true)

    }

     private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor: View = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    protected open fun getStatusBarHeight(): Int {
        val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }
    fun copyData(copyStr: String) {
        val str: ClipData = ClipData.newPlainText("Label", copyStr)
        val cm: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(str)
    }


    fun showNetAddrDlg(
        context: Context?, strTitle: String?, oldAddr: String,
        oldPort: String?
    ) {
        val oldStr:String  = "当前设置：$oldAddr"
        val dialog: Dialog? = object : Dialog(context!!, R.style.Dialog) {
            private var mOnClickListener: View.OnClickListener? = null
            private var mtvTitle: TextView? = null
            private var medoldaddr: TextView? = null
            private var mTVPort: EditText? = null
            private var mednewaddr: DigitalEditText? = null
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.dlg_changenetaddr1)
                mtvTitle = findViewById(R.id.tv_netsetting_title)
                medoldaddr = findViewById(R.id.et_oldaddr)
                mednewaddr = findViewById(R.id.et_newaddr1)
                mTVPort = findViewById(R.id.tv_port)
                mtvTitle?.text = strTitle
                medoldaddr?.text = oldAddr
                mTVPort?.setText(oldPort)
                mOnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.btn_ok -> {
                            val serverIpArray: Array<String> = mednewaddr?.digitalEditTextValue as Array<String>
                            if (serverIpArray[0] == "" || serverIpArray[1] == "" || serverIpArray[2] == "" || serverIpArray[3] == "" || serverIpArray[0] == "0111" || serverIpArray[1] == "0111" || serverIpArray[2] == "0111" || serverIpArray[3] == "0111") {
                                ToastUtils.showShort("错误.IP段不能为空")
                                return@OnClickListener
                            }
                            if (serverIpArray[0].toInt() > 255 || serverIpArray[1].toInt() > 255 || serverIpArray[2].toInt() > 255 || serverIpArray[3].toInt() > 255
                            ) {
                                ToastUtils.showShort("错误,IP段值必须在0至255之间")
                                return@OnClickListener
                            }
                            val strOut = serverIpArray[0] + "." + serverIpArray[1] + "." + serverIpArray[2] + "." + serverIpArray[3]
                            val port = mTVPort?.text.toString()
                            if (TextUtils.isEmpty(port)) {
                                ToastUtils.showShort("端口号不能为空")
                                return@OnClickListener
                            }
                            NetUtil().save(strOut,port)
                            ToastUtils.showShort("保存完成")
                            dismiss()
                            exitProc1()
                        }
                        R.id.btn_cancel -> {
                            dismiss()
                        }
                        R.id.bt_app_mode -> {
                            SPUtils.getInstance().put("SP_KEY_APP_MODE", "")
                            IntentRouter.go(context, Constant.Router.ROOTACTIVITY)
                            finish()
                        }
                        else -> {
                        }
                    }
                }
                findViewById<View>(R.id.btn_ok).setOnClickListener(mOnClickListener)
                findViewById<View>(R.id.btn_cancel).setOnClickListener(mOnClickListener)
                findViewById<View>(R.id.bt_app_mode).setOnClickListener(mOnClickListener)
            }

            override fun onBackPressed() { //在setCanclable为false的情况下，重写onBackPressed方法还是能够成功获取到回退事件的
                super.onBackPressed()
                dismiss()
            }
        }
        dialog?.show()
    }

    open fun exitProc1() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private var mlistener: PermissionListener? = null
    /**
     * 权限申请
     *
     * @param permissions 待申请的权限集合
     * @param listener    申请结果监听事件
     */
    protected open fun requestRunTimePermission(permissions: Array<String>, listener: PermissionListener) {
        this.mlistener = listener

        //用于存放为授权的权限
        val permissionList: MutableList<String> = ArrayList()
        //遍历传递过来的权限集合
        for (permission in permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(this, permission) !== PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission)
            }
        }

        //判断集合
        if (!permissionList.isEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), 222)
        } else {  //为空，则已经全部授权
            listener.onGranted()
        }
    }

    /**
     * 权限申请结果
     *
     * @param requestCode  请求码
     * @param permissions  所有的权限集合
     * @param grantResults 授权结果集合
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            222 -> if (grantResults.isNotEmpty()) {
                //被用户拒绝的权限集合
                val deniedPermissions: MutableList<String> = ArrayList()
                //用户通过的权限集合
                val grantedPermissions: MutableList<String> = ArrayList()
                var i = 0
                while (i < grantResults.size) {

                    //获取授权结果，这是一个int类型的值
                    val grantResult = grantResults[i]
                    if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                        val permission = permissions[i]
                        deniedPermissions.add(permission)
                    } else {  //用户同意的权限
                        val permission = permissions[i]
                        grantedPermissions.add(permission)
                    }
                    i++
                }
                if (deniedPermissions.isEmpty()) {  //用户拒绝权限为空
                    mlistener?.onGranted()
                } else {  //不为空
                    //回调授权成功的接口
                    mlistener?.onDenied(deniedPermissions)
                    //回调授权失败的接口
                    mlistener?.onGranted(grantedPermissions)
                }
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        OkGo.getInstance().cancelTag(this)
        super.onDestroy()
    }
}