package com.supcon.mes.module_beacon.rssiCorrection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.widget.EditText
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.internal.LinkedTreeMap
import com.supcon.common.BaseConstant
import com.supcon.common.view.view.js.DefaultHandler
import com.supcon.mes.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.middleware.util.UrlUtil
import com.supcon.mes.module_beacon.*
import com.supcon.mes.module_beacon.adapter.RssiDataListAdapter
import com.supcon.mes.module_beacon.bean.BLEDevice
import com.supcon.mes.module_beacon.bean.JSGPSCoordinate
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.event.BleReceiverEvent
import com.supcon.mes.module_beacon.net.HttpRequest
import com.supcon.mes.module_beacon.powerSetting.DeviceControlActivity
import com.supcon.mes.module_beacon.ui.GPSActivity
import com.supcon.mes.module_beacon.utils.RssiDataUtil
import kotlinx.android.synthetic.main.activity_bluetooth_detail.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("DEPRECATION")
open class BluetoothDetailActivity : BluetoothConnectionActivity() {
    private val mRssiDataListAdapter = RssiDataListAdapter()
    private var mScanning = false
    private var mHandler: Handler? = null
    private var scanTime: Long = 0
    private var mCurrentPosition: Int = 0
    private var alertDialog: AlertDialog? = null
    private var rssis = ArrayList<Int>()
    private var currentCoordinate: JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    private var getBeaconState = 0  //0为仅获取。1为获取后根据状况选择更新/新建接口
    private var beaconData = LinkedTreeMap<String, Any>()

    //两个为gps坐标用
    private var id : String = ""
    private var label : String = ""

    private val headers: Map<String, String> = HashMap()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        actionBar?.hide()
        bleDevice = intent.getParcelableExtra("device")
        initView1()
        initData1()
        initWebView()
        initBleBonded()
    }

    private fun initBleBonded() {
        if ((mBluetoothAdapter?.bondedDevices?.size)!! > 0) {
            for (i in 0 until mBluetoothAdapter?.bondedDevices!!.size) {
                val device = mBluetoothAdapter?.bondedDevices!!.distinct()[i]
                if (getMac() == device.address) {
                    isBonded = true
                    break
                }
            }
        }
        bt_unBind.setOnClickListener {
            bleDevice?.let { it1 ->
                removeBond(it1)
                bt_unBind.visibility = View.GONE
            }
        }
        bt_detail.setOnClickListener {
            if (isBonded) {
                goBLEDetail()
            }else {
                ToastUtils.showLong("需要先绑定设备")
            }
        }
        if (isBonded) {
            bt_bind.visibility = View.GONE
        } else {
            bt_unBind.visibility = View.GONE
            bt_bind.setOnClickListener {
                bleDevice?.createBond()
            }
        }
    }
    private fun goBLEDetail() {
        val intent = Intent(this, DeviceControlActivity::class.java)
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, bleDevice?.name)
        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, bleDevice?.address)
        if (this.mScanning) {
            this.mBluetoothAdapter?.stopLeScan(this.mLeScanCallback)
            this.mScanning = false
        }
        startActivity(intent)
    }

    private fun initWebView() {
        requireNotNull(webView) { "BridgeWebView is null!" }
        webView.setDefaultHandler(DefaultHandler())
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

    override fun initData() {}

    private fun initData1() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        mHandler = Handler()
        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createEmptyRssiList())
        HttpRequest().getBeacon(this, getSN())
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_bluetooth_detail
    }

    private fun initView1() {
        fragment_title_mac.visibility = View.VISIBLE
        fragment_title_mac.text = bleDevice?.address
        fragment_title_des.text = bleDevice?.name
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        iv_return.setOnClickListener {
            finish()
        }
        rv_rssi_data.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_rssi_data.adapter = mRssiDataListAdapter
        bt_commit.visibility = View.VISIBLE
        bt_commit.setOnClickListener {
            getBeaconState = 1
            HttpRequest().getBeacon(this, getSN())
        }
        alertDialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.tip))
            .setMessage(resources.getString(R.string.scanning))
            .setCancelable(false)
            .create()

        bt_choose_gps.setOnClickListener {

            val intent = Intent(this, GPSActivity::class.java)
            val bleDevice = BLEDevice()
            bleDevice.deviceName = bleDevice.deviceName
            bleDevice.deviceAddress = bleDevice.deviceAddress
            intent.putExtra("device", bleDevice)
            val bundle = Bundle()
            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
            startActivityForResult(intent, REQUEST_GET_COORDINATE)
        }

        bt_template_use.setOnClickListener {
            mRssiDataListAdapter.setNewData(RssiDataUtil.get().createTemplateRssiList())
        }

        bt_template_manage.setOnClickListener {
            showTempleDlg()
        }

        webView!!.webChromeClient = object : WebChromeClient() {}
    }

    fun showTempleDlg() {
        val dialog: Dialog? = object : Dialog(this, R.style.Dialog) {
            private var mOnClickListener: View.OnClickListener? = null
            private var mednewaddr: EditText? = null
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.dlg_template_edit)
                mednewaddr = findViewById(R.id.et_newaddr)
                mednewaddr?.setText(RssiDataUtil.get().getTemplate().toString())
                mOnClickListener = View.OnClickListener { view ->
                    when (view.id) {
                        R.id.btn_ok -> {
                            var templateS = mednewaddr?.text.toString()
                            var templateJ = Gson().fromJson(templateS,JsonArray::class.java)
                            RssiDataUtil.get().setTemplate(templateS)
                            ToastUtils.showShort(resources.getString(R.string.has_saved))
                            dismiss()
                        }
                        R.id.btn_cancel -> {
                            dismiss()
                        }
                    }
                }
                findViewById<View>(R.id.btn_ok).setOnClickListener(mOnClickListener)
                findViewById<View>(R.id.btn_cancel).setOnClickListener(mOnClickListener)
            }

            override fun onBackPressed() { //在setCanclable为false的情况下，重写onBackPressed方法还是能够成功获取到回退事件的
                super.onBackPressed()
                dismiss()
            }
        }
        dialog?.show()
    }


    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GET_COORDINATE -> {
                when (resultCode) {
                    RESPONSE_GET_COORDINATE -> {
                        currentCoordinate = data?.getSerializableExtra(COORDINATE_KEY) as JSGPSCoordinate.ParamBean.CoordinatesBean
                        et_gps_content.setText("${resources.getString(R.string.lan)}：${currentCoordinate?.lat} ${resources.getString(R.string.lon)}: ${currentCoordinate?.lon} ${resources.getString(R.string.height)}: ${currentCoordinate?.hei}")
                    }
                }
            }
        }
    }

    private fun getSN(): String {
        if (TextUtils.isEmpty(bleDevice?.name)) {
            return ""
        }
        val name: String
        val a = bleDevice?.name?.length!! - 8
        val b = bleDevice?.name?.length!!
        name = bleDevice?.name?.substring(a, b).toString()
        return name
    }

    private fun getMac(): String {
        return bleDevice!!.address
    }

    fun checkPermissionAndScanDevice(position: Int) {
        mCurrentPosition = position
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            scanDevice(true)
        }
    }

    private fun scanDevice(enable: Boolean) {
        if (enable) {
            alertDialog?.show()
            rssis.clear()
            iv_refresh.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotaterepeat))
            scanTime = System.currentTimeMillis()
            RssiDataUtil.get().rssiCollector.clear()
            mHandler!!.postDelayed({
                if (mScanning) {
                    //若在单位时间内手动结束上一次扫描，开始下一次扫描，如果上一次扫描还未结束，则会停止上一次扫描
                    mScanning = false
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
                    alertDialog?.dismiss()
                    iv_refresh.clearAnimation()
                }
            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
        } else {
            alertDialog?.dismiss()
            iv_refresh.clearAnimation()
            mScanning = false
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            ToastUtils.showShort(resources.getString(R.string.stop_scan))
            mHandler!!.removeCallbacksAndMessages(null);
        }
        invalidateOptionsMenu()
    }

    private val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, _ ->
        runOnUiThread {
            if (device.address == bleDevice?.address) {
                alertDialog?.setMessage(resources.getString(R.string.scanning) + ": $rssi")
                LogUtils.d("获取到强度值：$rssi")
                rssis.add(rssi)
                if (rssis.size > 10) {
                    val rssisForAvg : ArrayList<Int> = rssis.clone() as ArrayList<Int>
                    rssisForAvg.sort()
                    rssisForAvg.removeAt(rssisForAvg.size-1)
                    rssisForAvg.removeAt(0)
                    var rssis1 = rssisForAvg.average()
                    LogUtils.d("计算出平均强度值：$rssis1")
                    RssiDataUtil.get().updateRssi(mCurrentPosition,rssis1)
                    mRssiDataListAdapter.setNewData(RssiDataUtil.get().mRssiDataList)

                    if (rssisForAvg.size>14 && mScanning) {
                        scanDevice(false)
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

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
        when (event?.requestCode) {
            HttpRequest.REQUEST_CODE_GET_BEACON -> {
                LogUtils.d(event.toString() + event.data)
                when (getBeaconState) {
                    0 -> {
                        when (event.code) {
                            200 -> {
                                //查到信标，填充界面
                                val data = event.data as LinkedTreeMap<*, *>
                                et_floor_content.setText(data["floor"].toString())
                                val x = data["x"].toString()
                                val y = data["y"].toString()
                                val z = data["z"].toString()
                                et_gps_content.setText("${resources.getString(R.string.lan)}：$x ${resources.getString(R.string.lan)}: $y ${resources.getString(R.string.lan)}: $z")
                                var p0 = data["p0"].toString().toDouble()
//                                p0 -= 127
                                et_p0.setText(p0.toString())

                                currentCoordinate = JSGPSCoordinate.ParamBean.CoordinatesBean()
                                currentCoordinate?.lat = x.toDouble()
                                currentCoordinate?.lon = y.toDouble()
                                currentCoordinate?.hei = z.toDouble()

                                id = data["spaceId"].toString()
                                label = data["sn"].toString()


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
                        }
                    }
                    1 -> {
                        when (event.code) {
                            200 -> {
                                //请求更新接口
                                beaconData = event.data as LinkedTreeMap<String, Any>
                                if (RssiDataUtil.get().allDataPrepared()) {
                                    beaconData["n"] = RssiDataUtil.get().getN()
                                    beaconData["p0"] = RssiDataUtil.get().getP0()
                                }
                                beaconData["floor"] = et_floor_content.text.toString()
                                if (null != currentCoordinate) {
                                    beaconData["x"] = currentCoordinate?.lon
                                    beaconData["y"] = currentCoordinate?.lat
                                    beaconData["z"] = currentCoordinate?.hei
                                }
                                beaconData["updatedAt"] = System.currentTimeMillis()
                                val jsonObject = Gson().toJson(beaconData)
                                HttpRequest().updateBeacon(this, jsonObject, getSN())
                            }
                            400 -> {
                                //请求新建接口
                                //后台接口不允许以下三项为空
                                beaconData["battery"] = 0
                                beaconData["createdAt"] = System.currentTimeMillis()
                                beaconData["updatedAt"] = System.currentTimeMillis() + 1

                                //以下四项后台说不用管
                                beaconData["distance"] = ""
                                beaconData["factoryMarkId"] = ""
                                beaconData["id"] = ""
                                beaconData["rssi"] = ""

                                //以下两项为必须的
                                beaconData["sn"] = getSN()
                                beaconData["mac"] = getMac()
                                if (RssiDataUtil.get().allDataPrepared()) {
                                    beaconData["n"] = RssiDataUtil.get().getN()
                                    beaconData["p0"] = RssiDataUtil.get().getP0()
                                }
                                beaconData["floor"] = et_floor_content.text.toString()
                                if (null != currentCoordinate) {
                                    beaconData["x"] = currentCoordinate?.lon
                                    beaconData["y"] = currentCoordinate?.lat
                                    beaconData["z"] = currentCoordinate?.hei
                                }
                                val jsonObject = Gson().toJson(beaconData)
                                HttpRequest().addBeacon(this, jsonObject)
                            }
                            else -> {
                                ToastUtils.showLong(resources.getString(R.string.fail_to_get_ble_info) + event.msg)
                            }
                        }
                    }
                }
            }
            HttpRequest.REQUEST_CODE_UPDATE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showLong(resources.getString(R.string.success_to_get_ble_info))
                        getBeaconState = 0
                        HttpRequest().getBeacon(this, getSN())
                        saveDataToSP()
                    }
                    else -> ToastUtils.showLong(resources.getString(R.string.fail_to_update_ble_info) + event.msg)
                }
            }
            HttpRequest.REQUEST_CODE_ADD_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showLong(resources.getString(R.string.success_to_update_ble_info))
                        saveDataToSP()
                    }
                    else -> ToastUtils.showLong(resources.getString(R.string.fail_to_update_ble_info) + event.msg)
                }
            }
        }
    }

    private fun saveDataToSP() {
        val mapValue = JSONObject()
        mapValue.put("mac", beaconData["mac"].toString())
        mapValue.put("n", beaconData["n"].toString())
        mapValue.put("floor", beaconData["floor"].toString())
        mapValue.put("x", beaconData["x"].toString())
        mapValue.put("y", beaconData["y"].toString())
        mapValue.put("z", beaconData["z"].toString())
        SPUtils.getInstance().put(bleDevice!!.address, mapValue.toString())
        val d = SPUtils.getInstance().getString(bleDevice!!.address)
        val ds = SPUtils.getInstance().getString(bleDevice!!.address)
    }


    override fun onDestroy() {
        super.onDestroy()
        scanDevice(false)
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BleReceiverEvent?) {
        when(event?.data) {
            BluetoothDevice.BOND_BONDED -> {
                bt_unBind.visibility = View.VISIBLE
                bt_bind.visibility = View.GONE
                isBonded = true
                goBLEDetail()
            }
        }
    }
}