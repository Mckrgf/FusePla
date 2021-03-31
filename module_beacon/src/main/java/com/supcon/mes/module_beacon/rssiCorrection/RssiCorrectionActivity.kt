package com.supcon.mes.module_beacon.rssiCorrection

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
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.common.BaseConstant
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.module_beacon.*
import com.supcon.mes.module_beacon.adapter.DeviceListForRssiAdapter
import com.supcon.mes.module_beacon.adapter.RssiDataListAdapter
import com.supcon.mes.module_beacon.bean.JSGPSCoordinate
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.bean.MyBluetoothDevice
import com.supcon.mes.module_beacon.net.HttpRequest
import com.supcon.mes.module_beacon.utils.CalculateUtil
import com.supcon.mes.module_beacon.utils.RssiDataUtil
import kotlinx.android.synthetic.main.activity_bluetooth_detail.*
import kotlinx.android.synthetic.main.activity_device_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bt_sort
import kotlinx.android.synthetic.main.activity_main.et_search
import kotlinx.android.synthetic.main.activity_main.number_progress_bar
import kotlinx.android.synthetic.main.activity_main.rv_rssi_data
import kotlinx.android.synthetic.main.activity_main.tv_count
import kotlinx.android.synthetic.main.activity_main.tv_search
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
        fragment_title_des.text = getString(R.string.app_name_module) + AppUtils.getAppVersionName()
        number_progress_bar.max = SCAN_PERIOD.toInt()
        tv_search.setOnClickListener(this)
        bt_gps.setOnClickListener(this)
        bt_calculate.setOnClickListener(this)
        iv_return.setOnClickListener(this)
        fragment_title_des.text = resources.getString(R.string.RSSI_COLLECT)
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
            val intent = Intent(this,BluetoothDetailActivity::class.java)
            intent.putExtra("device",device)
            val bundle = Bundle()
            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
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
                    mScanning = false
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
                    tv_search.text = getString(R.string.START_SCAN)
                    iv_refresh.clearAnimation()
                }

            }, SCAN_PERIOD)
            mScanning = true
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
            tv_search.text = getString(R.string.STOP_SCAN)
        } else {
            iv_refresh.clearAnimation()
            mScanning = false
            tv_search.text = getString(R.string.START_SCAN)
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            mHandler!!.removeCallbacksAndMessages(null)
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

            tv_count.text = "${mDeviceListAdapter.mLeDevices?.size} " + resources.getString(R.string.ALL_DEVICE_FOUND)
            if (null != device.name && device.name.contains("SUP")) {
                LogUtils.d( "设备名称：${device.name} + 它的电量是：${a[16]}")
            }

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
                showNetAddrDlg(this, resources.getString(R.string.network_setting), NetUtil().getIP(), NetUtil().getPort())
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
                        tv_coordinate_main.text = "${resources.getString(R.string.lan)}：${currentCoordinate?.lat} ${resources.getString(R.string.lon)}: ${currentCoordinate?.lon} ${resources.getString(R.string.height)}: ${currentCoordinate?.hei}"

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

    override fun onDestroy() {
        super.onDestroy()
    }
}