package com.supcon.mes.module_beacon_no_login.rssiCorrection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.supcon.mes.module_beacon_no_login.*
import com.supcon.mes.module_beacon_no_login.adapter.RssiDataListAdapter
import com.supcon.mes.module_beacon_no_login.bean.JSGPSCoordinate
import com.supcon.mes.module_beacon_no_login.event.BleReceiverEvent
import com.supcon.mes.module_beacon_no_login.event.HttpEvent
import com.supcon.mes.module_beacon_no_login.net.HttpRequest
import com.supcon.mes.module_beacon_no_login.powerSetting.DeviceControlActivity
import com.supcon.mes.module_beacon_no_login.ui.GPSActivity
import com.supcon.mes.module_beacon_no_login.utils.RssiDataUtil
import com.supcon.supbeacon.*
import kotlinx.android.synthetic.main.activity_bluetooth_detail_b.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
@SuppressLint("MissingPermission")
class BluetoothDetailActivity : BluetoothConnectionActivity() {
    private val mRssiDataListAdapter = RssiDataListAdapter()
    private var mScanning = false
    private var mHandler: Handler? = null
    private var scanTime: Long = 0
    private var mCurrentPosition: Int = 0
    private var alertDialog: AlertDialog? = null
    var rssis = ArrayList<Int>()
    private var currentCoordinate: JSGPSCoordinate.ParamBean.CoordinatesBean? = null
    private var getBeaconState = 0  //0???????????????1????????????????????????????????????/????????????
    private var beaconData = LinkedTreeMap<String, Any>()

    //WebViewClient????????????WebView?????????????????????????????????
    private val wbTestClient: WebViewClient = object : WebViewClient() {}

    //WebChromeClient????????????WebView??????Javascript????????????????????????????????????title??????????????????
    private val webChromeClient: WebChromeClient = object : WebChromeClient() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_detail_b)
        bleDevice = intent.getParcelableExtra("device")
        initView()
        initData()
    }

    private fun initData() {
        mHandler = Handler()
        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createEmptyRssiList())
        HttpRequest().getBeacon(this,getSN())
    }

    @SuppressLint("MissingPermission")
    private fun initView() {
        fragment_title_mac.visibility = View.VISIBLE
        fragment_title_mac.text = bleDevice?.address
        fragment_title_des.text = bleDevice?.name
        fragment_title_des.setTextColor(resources.getColor(R.color.black,null))
        iv_return.setOnClickListener { finish() }
        rv_rssi_data.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_rssi_data.adapter = mRssiDataListAdapter
        bt_commit.visibility = View.VISIBLE
        bt_commit.setOnClickListener {
            getBeaconState = 1
            HttpRequest().getBeacon(this,getSN())
        }
        alertDialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.tip))
            .setMessage(resources.getString(R.string.scanning))
            .setCancelable(false)
            .create()

        bt_choose_gps.setOnClickListener {
            val intent = Intent(this, GPSActivity::class.java)
            startActivityForResult(intent, REQUEST_GET_COORDINATE)
        }

        val webSettings: WebSettings = iv_map.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.useWideViewPort = true //????????????????????????webview?????????
        webSettings.loadWithOverviewMode = true // ????????????????????????
        webSettings.setSupportZoom(true) //????????????????????????true??????????????????????????????
        webSettings.builtInZoomControls = true //????????????????????????????????????false?????????WebView????????????
        webSettings.displayZoomControls = false //???????????????????????????
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE //??????webview?????????
        webSettings.allowFileAccess = true //????????????????????????
        webSettings.javaScriptCanOpenWindowsAutomatically = true //????????????JS???????????????
        webSettings.loadsImagesAutomatically = true //????????????????????????
        webSettings.defaultTextEncodingName = "utf-8" //??????????????????
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT //??????????????????????????????????????????.
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        iv_map.webChromeClient = webChromeClient
        iv_map.webViewClient = wbTestClient

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
                ToastUtils.showLong(resources.getString(R.string.need_to_bind_device_first))
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

    @SuppressLint("MissingPermission")
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GET_COORDINATE -> {
                when (resultCode) {
                    RESPONSE_GET_COORDINATE -> {
                        currentCoordinate = data?.getSerializableExtra(COORDINATE_KEY) as JSGPSCoordinate.ParamBean.CoordinatesBean
                        val coordinates2f = currentCoordinate
                        et_gps_content.setText("${resources.getString(R.string.lan)}???${currentCoordinate?.lat} ${resources.getString(R.string.lon)}: ${currentCoordinate?.lon} ${resources.getString(R.string.height)}: ${currentCoordinate?.hei}")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getSN() :String{
        if (TextUtils.isEmpty( bleDevice?.name)) {
            return  ""
        }
        val name: String
        val a  = bleDevice?.name?.length!!-8
        val b  = bleDevice?.name?.length!!
        name = bleDevice?.name?.substring(a,b).toString()
        return name
    }
    private fun getMac() :String{
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
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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

    private val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, a ->
        runOnUiThread {
            if (device.address == bleDevice?.address) {
                alertDialog?.setMessage( resources.getString(R.string.scanning) + "???$rssi")
                LogUtils.d("?????????????????????$rssi")
                rssis.add(rssi)
                if (rssis.size > 10) {
                    val rssisForAvg : ArrayList<Int> = rssis.clone() as ArrayList<Int>
                    rssisForAvg.sort()
                    rssisForAvg.removeAt(rssisForAvg.size-1)
                    rssisForAvg.removeAt(0)
                    val rssis1 = rssisForAvg.average()
                    LogUtils.d("???????????????????????????$rssis1")
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
                LogUtils.d(event.toString()+ event.data)
                when(getBeaconState) {
                    0-> {
                        when (event.code) {
                            200 -> {
                                //???????????????????????????
                                val data = event.data as LinkedTreeMap<*, *>
                                et_floor_content.setText(data["floor"].toString())
                                val x = data["x"].toString()
                                val y = data["y"].toString()
                                val z = data["z"].toString()
                                et_gps_content.setText("${resources.getString(R.string.lan)}???$x ${resources.getString(R.string.lon)}: $y ${resources.getString(R.string.height)}: $z")
                                val p0 = data["p0"].toString().toDouble()
//                                p0 -= 127
                                et_p0.setText(p0.toString())

                                currentCoordinate = JSGPSCoordinate.ParamBean.CoordinatesBean()
                                currentCoordinate?.lat = x.toDouble()
                                currentCoordinate?.lon = y.toDouble()
                                currentCoordinate?.hei = z.toDouble()

                                val url = DEVICE_ADDRESS + data["sn"].toString()
                                iv_map.loadUrl(url)
                            }
                        }
                    }
                    1-> {
                        when (event.code) {
                            200 -> {
                                //??????????????????
                                beaconData = event.data as LinkedTreeMap<String,Any>
                                if (RssiDataUtil.get().allDataPrepared()) {
                                    beaconData["n"] = RssiDataUtil.get().getN()
                                    beaconData["p0"] = RssiDataUtil.get().getP0()
                                }
                                beaconData["floor"] = et_floor_content.text.toString()
                                if (null != currentCoordinate) {
                                    beaconData["x"] = currentCoordinate?.lat
                                    beaconData["y"] = currentCoordinate?.lon
                                    beaconData["z"] = currentCoordinate?.hei
                                }
                                val jsonObject = Gson().toJson(beaconData)
                                HttpRequest().updateBeacon(this, jsonObject, getSN())
                            }
                            400 -> {
                                //??????????????????
                                beaconData =  LinkedTreeMap()

                                //???????????????????????????????????????
                                beaconData["battery"] = 0
                                beaconData["createdAt"] = System.currentTimeMillis()
                                beaconData["updatedAt"] = System.currentTimeMillis() + 1

                                //??????????????????????????????
                                beaconData["distance"] = ""
                                beaconData["factoryMarkId"] = ""
                                beaconData["id"] = ""
                                beaconData["rssi"] = ""

                                //????????????????????????
                                beaconData["sn"] = getSN()
                                beaconData["mac"] = getMac()
                                if (RssiDataUtil.get().allDataPrepared()) {
                                    beaconData["n"] = RssiDataUtil.get().getN()
                                    beaconData["p0"] = RssiDataUtil.get().getP0()
                                }
                                beaconData["floor"] = et_floor_content.text.toString()
                                if (null != currentCoordinate) {
                                    beaconData["x"] = currentCoordinate?.lat
                                    beaconData["y"] = currentCoordinate?.lon
                                    beaconData["z"] = currentCoordinate?.hei
                                }
                                val jsonObject = Gson().toJson(beaconData)
                                HttpRequest().addBeacon(this, jsonObject)
                            }
                            else -> {
                                ToastUtils.showLong(resources.getString(R.string.operation_failed)+ event.msg)
                            }
                        }
                    }
                }
            }
            HttpRequest.REQUEST_CODE_UPDATE_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showLong(resources.getString(R.string.operation_successful))
                        getBeaconState = 0
                        HttpRequest().getBeacon(this, getSN())
                        saveDataToSP()
                    }

                    else -> ToastUtils.showLong(resources.getString(R.string.operation_failed) + event.msg)
                }
            }
            HttpRequest.REQUEST_CODE_ADD_BEACON -> {
                when (event.code) {
                    200 -> {
                        ToastUtils.showLong(resources.getString(R.string.operation_successful))
                        saveDataToSP()
                    }
                    else -> ToastUtils.showLong(resources.getString(R.string.operation_failed) + event.msg)
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
    }

    override fun onDestroy() {
        if (iv_map != null) {
            iv_map.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            iv_map.clearHistory()
            (iv_map.parent as ViewGroup).removeView(iv_map)
            iv_map.destroy()
        }
        super.onDestroy()
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