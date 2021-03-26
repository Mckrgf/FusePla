package com.supcon.mes.module_beacon_no_login.rssiCorrection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon_no_login.*
import com.supcon.mes.module_beacon_no_login.adapter.DeviceListForRssiAdapter
import com.supcon.mes.module_beacon_no_login.adapter.RssiDataListAdapter
import com.supcon.mes.module_beacon_no_login.bean.JSGPSCoordinate
import com.supcon.mes.module_beacon_no_login.bean.MyBluetoothDevice
import com.supcon.mes.module_beacon_no_login.event.HttpEvent
import com.supcon.mes.module_beacon_no_login.net.HttpRequest
import com.supcon.mes.module_beacon_no_login.ui.GPSActivity
import com.supcon.mes.module_beacon_no_login.utils.CalculateUtil
import com.supcon.mes.module_beacon_no_login.utils.NetUtil
import com.supcon.mes.module_beacon_no_login.utils.RssiDataUtil
import com.supcon.supbeacon.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


@Suppress("DEPRECATION")
class RssiCorrectionActivity : BaseActivity(), View.OnClickListener, RssiCorrectionContract.View {

    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning = false
    private var mHandler: Handler? = null
    private val mDeviceListAdapter = DeviceListForRssiAdapter(this)
    private val mRssiDataListAdapter = RssiDataListAdapter()
    private var currentDevice: BluetoothDevice? = null
    private var currentCoordinate: JSGPSCoordinate.ParamBean.CoordinatesBean? = null

    private var scanTime: Long = 0
    private var mPresenter: RssiCorrectionPresenter? = null
    private var rssiDevice : HashMap<BluetoothDevice,Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (NetUtil().isFirstOpen()) {
            showNetAddrDlg(this@RssiCorrectionActivity, getString(R.string.FIRST_LOGIN_IP_CONFIG), "", "")
        }

        //模拟坐标数据
        currentCoordinate = JSGPSCoordinate.ParamBean.CoordinatesBean()
        currentCoordinate?.lat = 1.0
        currentCoordinate?.lon = 2.0
        currentCoordinate?.hei = 3.0

        initView()
        initData()
        checkPermissionAndScanDevice()
    }

    private fun initData() {
        mHandler = Handler()
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
//        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createEmptyRssiList())
        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createMockRssiList())
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        fragment_title_des.text = getString(R.string.app_name) + AppUtils.getAppVersionName()
        number_progress_bar.max = SCAN_PERIOD.toInt()
        tv_search.setOnClickListener(this)
        bt_gps.setOnClickListener(this)
        bt_calculate.setOnClickListener(this)
        iv_return.setOnClickListener(this)
        fragment_title_des.text = "RSSI收集"
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        iv_refresh.visibility = View.VISIBLE
        tv_search.visibility = View.VISIBLE

        rv_devices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_devices.adapter = mDeviceListAdapter
        mDeviceListAdapter.setOnItemClickListener { adapter, _, position ->
            val myBluetoothDevice = adapter.data[position] as MyBluetoothDevice
            val device = myBluetoothDevice.device
            currentDevice = device
            tv_device_selected.text = getString(R.string.DEVICE_CHOSEN) + device.name
//            if (RssiDataUtil.get().getAverageRssiByDeviceSuccessfully(device)) {
//                copyData(RssiDataUtil.get().averageRssi.toString())
//                ToastUtils.showShort(RssiDataUtil.get().averageRssi.toString() + getString(R.string.COPY_PASTE))
//            } else {
//                ToastUtils.showShort(getString(R.string.RESEARCH_BLE))
//            }
            val intent = Intent(this,BluetoothDetailActivity::class.java)
            intent.putExtra("device",device)
            startActivity(intent)
        }

        rv_rssi_data.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_rssi_data.adapter = mRssiDataListAdapter

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mDeviceListAdapter.refresh(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        bt_sort.setOnClickListener {
            if (mDeviceListAdapter.data.size > 0) {
                val data = CalculateUtil.sortBLEDevice(mDeviceListAdapter.data)
                mDeviceListAdapter.setNewData(data)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {

    }

    private fun scanDevice(enable: Boolean) {
        if (enable) {
            iv_refresh.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotaterepeat))
            scanTime = System.currentTimeMillis()
            RssiDataUtil.get().rssiCollector.clear()
            mHandler!!.postDelayed({
                if (mScanning) {
                    //若在单位时间内手动结束上一次扫描，开始下一次扫描，如果上一次扫描还未结束，则会停止上一次扫描
                    mScanning = false
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
                    iv_refresh.clearAnimation()
                    tv_search.text = getString(R.string.START_SCAN)
                }
            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
            tv_search.text = getString(R.string.STOP_SCAN)
        } else {
            iv_refresh.clearAnimation()
            mScanning = false
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            tv_search.text = getString(R.string.START_SCAN)
            mHandler!!.removeCallbacksAndMessages(null);
        }
        invalidateOptionsMenu()
    }

    private val mLeScanCallback = LeScanCallback { device, rssi, a ->
        runOnUiThread {
            number_progress_bar.progress = (System.currentTimeMillis() - scanTime).toInt()
            if (number_progress_bar.progress > number_progress_bar.max-300) number_progress_bar.progress = number_progress_bar.max
            RssiDataUtil.get().collectRssi(device, rssi)

            rssiDevice[device] = rssi
            val myBluetoothDevice = MyBluetoothDevice()
            myBluetoothDevice.device = device
            myBluetoothDevice.rssi = rssi
            mDeviceListAdapter.addDevice(myBluetoothDevice)

            tv_count.text = "共找到${mDeviceListAdapter.mLeDevices?.size}个"
            LogUtils.d("zxcv" + device.name)
        }
    }


    private fun checkPermissionAndScanDevice() {
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            scanDevice(true)
            mDeviceListAdapter.clear()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {
                scanDevice(!mScanning)
                if (mScanning) mDeviceListAdapter.clear()
            }
            R.id.bt_ip_setting -> {
                showNetAddrDlg(this, "网络配置", NetUtil().getIP(), NetUtil().getPort())
            }
            R.id.bt_gps -> {
                val intent = Intent(this, GPSActivity::class.java)
                startActivityForResult(intent, REQUEST_GET_COORDINATE)
            }
            R.id.bt_calculate -> {
                val n = RssiDataUtil.get().getN()
                tv_n.text = Formatter().format("%.2f", n).toString()
            }
            R.id.bt_commit -> {
                if (RssiDataUtil.get().allDataPrepared() && currentDevice != null) {
                    HttpRequest().getBeacon(this, getBeaconSN()!!)
                } else {
                    ToastUtils.showShort(getString(R.string.MUST_FULL_ALL_DATA))
                }
            }
            R.id.iv_return -> {
                finish()
            }
        }
    }

    private fun getBeaconSN(): String? {
        var name = currentDevice?.name
        name = name?.replace("SUP ", "")
        return name
    }

    private fun getBeaconMac(): String? {
        var address = currentDevice?.address
        address = address?.replace("SUP ", "")
        return address
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
                        coordinates2f?.hei = Formatter().format("%.2f", currentCoordinate?.hei).toString().toDouble()
                        coordinates2f?.lat = Formatter().format("%.2f", currentCoordinate?.lat).toString().toDouble()
                        coordinates2f?.lon = Formatter().format("%.2f", currentCoordinate?.lon).toString().toDouble()
                        tv_coordinate_main.text = "维度：${coordinates2f?.lat} 经度: ${coordinates2f?.lon} 高度: ${coordinates2f?.hei}"
                    }
                }
            }
        }
    }

    override fun updateAScanResult() {

    }

    override fun updateScanState() {

    }

    override fun setPresenter(presenter: RssiCorrectionContract.Presenter) {
        mPresenter = RssiCorrectionPresenter(this, this)
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