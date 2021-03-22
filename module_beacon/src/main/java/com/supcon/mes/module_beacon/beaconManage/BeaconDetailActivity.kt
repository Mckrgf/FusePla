package com.supcon.mes.module_beacon.beaconManage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.WebChromeClient
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.supcon.common.BaseConstant
import com.supcon.common.view.view.js.DefaultHandler
import com.supcon.mes.*
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.middleware.util.UrlUtil
import com.supcon.mes.module_beacon.*
import com.supcon.mes.module_beacon.bean.BLEDevice
import com.supcon.mes.module_beacon.bean.JSGPSCoordinate
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.net.HttpRequest
import com.supcon.mes.module_beacon.ui.GPSActivity
import com.supcon.mes.module_beacon.utils.MyDateUtils
import kotlinx.android.synthetic.main.activity_beacon_detail.*
import kotlinx.android.synthetic.main.activity_beacon_detail.bt_choose_gps
import kotlinx.android.synthetic.main.activity_beacon_detail.et_floor_content
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class BeaconDetailActivity : BaseWebActivity() {
    private var currentCoordinate: JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    private var beaconData = LinkedTreeMap<String, Any>()
    private var sn = ""
    private var n: Any? = null
    private var p0: Any? = null
    private var mac: Any? = null
    private val headers: Map<String, String> = HashMap()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data: HashMap<String, Any> = intent.getSerializableExtra("data") as HashMap<String, Any>
        sn = data["sn"].toString()
        iv_return.setOnClickListener { finish() }
        initData(data)
        initWebView()
    }

    private fun initWebView() {
        requireNotNull(webView) { "BridgeWebView is null!" }
        webView.setDefaultHandler(DefaultHandler())

//        webView.getSettings().setAppCacheEnabled(true);//启用localstorage本地存储api
//        webView.getSettings().setLightTouchEnabled(true);//启用选中功能
//        webView.getSettings().setDomStorageEnabled(true);//启用dom存储(关键就是这句)，貌似网上twitter显示有问题也是这个属性没有设置的原因
//        webView.getSettings().setDatabaseEnabled(true);//启用html5数据库功能
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        val settings = webView.settings
        settings.setAppCacheEnabled(false)
        //        settings.setJavaScriptEnabled(true);
        settings.allowFileAccess = true
        settings.databaseEnabled = true
        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path
        settings.databasePath = dir
        settings.domStorageEnabled = true
        settings.setGeolocationEnabled(true)
        settings.lightTouchEnabled = true
        settings.allowUniversalAccessFromFileURLs = true
        webView.webChromeClient = object : WebChromeClient() {}
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_beacon_detail

    }

    private fun initData(data: HashMap<String, Any>) {

        bt_choose_gps.setOnClickListener {
            val bleDevice = BLEDevice()
            bleDevice.deviceName = data["sn"].toString()
            bleDevice.deviceAddress = data["mac"].toString()
            val intent = Intent(this, GPSActivity::class.java)
            intent.putExtra("device", bleDevice)
            val bundle = Bundle()
            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_GET_COORDINATE)
        }

        bt_commit.visibility = View.VISIBLE
        bt_commit.setOnClickListener {
            beaconData["floor"] = et_floor_content.text.toString()
            beaconData["x"] = et_gps_x_content.text.toString()
            beaconData["y"] = et_gps_y_content.text.toString()
            beaconData["z"] = et_gps_z_content.text.toString()
            beaconData["updatedAt"] = System.currentTimeMillis()
            beaconData["sn"] = sn
            beaconData["n"] = n
            beaconData["p0"] = p0
            beaconData["mac"] = mac

            val jsonObject = Gson().toJson(beaconData)
            HttpRequest().updateBeacon(this, jsonObject, data["sn"].toString())
        }
        HttpRequest().getBeacon(this, data["sn"].toString())
        bt_delete.setOnClickListener {
            HttpRequest().deleteBeacon(this, sn)
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
        when (event?.requestCode) {
            HttpRequest.REQUEST_CODE_UPDATE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showShort("修改成功")
                        HttpRequest().getBeacon(this, sn)
                    }
                    else -> {
                        ToastUtils.showShort("修改失败" + event.msg)
                    }
                }
            }
            HttpRequest.REQUEST_CODE_DELETE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showShort("删除成功")
                        setResult(200)
                        finish()
                    }
                    else -> {
                        ToastUtils.showShort("删除失败" + event.msg)
                    }
                }
            }
            HttpRequest.REQUEST_CODE_GET_BEACON -> {
                when (event.code) {
                    200 -> {
                        val data: LinkedTreeMap<String, Any> = event.data as LinkedTreeMap<String, Any>
                        fragment_title_mac.visibility = View.VISIBLE
                        fragment_title_des.text = data["sn"].toString()
                        fragment_title_mac.text = data["mac"].toString()
                        et_floor_content.setText(data["floor"].toString())
                        et_gps_x_content.setText(data["x"].toString())
                        et_gps_y_content.setText(data["y"].toString())
                        et_gps_z_content.setText(data["z"].toString())
                        et_n_content.setText(data["n"].toString())
                        et_p0_content.setText(data["p0"].toString())
                        currentCoordinate?.lat = data["x"].toString().toDouble()
                        currentCoordinate?.lon = data["y"].toString().toDouble()
                        currentCoordinate?.hei = data["z"].toString().toDouble()
                        val time = MyDateUtils.getDateFromLong(data["updatedAt"].toString().toDouble().toLong(), MyDateUtils.date_Format)
                        et_time_content.setText(time)

                        n = data["n"]
                        p0 = data["p0"]
                        mac = data["mac"]
                        val id = data["spaceId"].toString()
                        val label = data["sn"].toString()
                        val aa = NetUtil().getBeaconMapUrl(id,label)
                        val cookie = PLAApplication.getCooki()
                        val token =  PLAApplication.getToken()
                        val bundle = UrlUtil.getNewWebBundle(aa)
                        val header: HashMap<String, String> = HashMap()
                        header["Cookie"] = cookie
                        header["Authorization"] = token
                        header.putAll(headers)
                        webView!!.loadUrl(aa,header)
                    }
                    else -> {
                        ToastUtils.showShort("获取数据失败" + event.msg)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GET_COORDINATE -> {
                when (resultCode) {
                    RESPONSE_GET_COORDINATE -> {
                        currentCoordinate = data?.getSerializableExtra(COORDINATE_KEY) as JSGPSCoordinate.ParamBean.CoordinatesBean
                        et_gps_x_content.setText(currentCoordinate?.lon.toString())
                        et_gps_y_content.setText(currentCoordinate?.lat.toString())
                        et_gps_z_content.setText(currentCoordinate?.hei.toString())
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}