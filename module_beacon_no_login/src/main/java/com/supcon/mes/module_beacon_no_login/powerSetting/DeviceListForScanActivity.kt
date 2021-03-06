package com.supcon.mes.module_beacon_no_login.powerSetting

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon_no_login.BaseActivity
import com.supcon.mes.module_beacon_no_login.REQUEST_ENABLE_BT
import com.supcon.mes.module_beacon_no_login.SCAN_PERIOD
import com.supcon.mes.module_beacon_no_login.adapter.DeviceListForBondAdapter
import com.supcon.mes.module_beacon_no_login.adapter.DeviceListForConnectAdapter
import com.supcon.mes.module_beacon_no_login.bean.MyBluetoothDevice
import com.supcon.mes.module_beacon_no_login.event.HttpEvent
import com.supcon.mes.module_beacon_no_login.utils.CalculateUtil
import com.supcon.mes.module_beacon_no_login.utils.ClsUtils
import com.supcon.mes.module_beacon_no_login.utils.NetUtil
import com.supcon.supbeacon.R
import kotlinx.android.synthetic.main.activity_device_list.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION")
class DeviceListForScanActivity : BaseActivity(), View.OnClickListener {
    var mBluetoothAdapter: BluetoothAdapter? = null
    var mScanning = false
    private var mHandler: Handler? = null
    private val mDeviceListAdapter = DeviceListForConnectAdapter(this)
    private val mDeviceListForBondAdapter = DeviceListForBondAdapter(this)
    private var currentDevice: BluetoothDevice? = null

    private var scanTime: Long = 0
    private var mwaitdlg: ProgressDialog? = null

    private var currentDevicePosition: Int = -1

    private var rssiDevice : HashMap<BluetoothDevice,Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        initView()
        initData()
        checkPermissionAndScanDevice()
    }

    @SuppressLint("MissingPermission")
    private fun initData() {
        mHandler = Handler()
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        mDeviceListForBondAdapter.setNewData(mBluetoothAdapter?.bondedDevices?.distinct())
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    private fun initView() {
        registerLoginBroadcast()
        mwaitdlg = ProgressDialog(this)
        mwaitdlg?.isIndeterminate = false //????????????

        mwaitdlg?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mwaitdlg?.setMessage(resources.getString(R.string.binding_bluetooth))
        mwaitdlg?.setCancelable(false) //false?????????????????????true??????????????????
        iv_refresh.visibility = View.VISIBLE
        tv_search.setOnClickListener(this)
        tv_search.visibility = View.VISIBLE
        fragment_title_des.text = getString(R.string.app_name) + AppUtils.getAppVersionName()
        number_progress_bar.max = SCAN_PERIOD.toInt()
        bt_ble_status.setOnClickListener(this)
        bt_ip_setting.setOnClickListener(this)
//        bt_ble_status.visibility = View.VISIBLE
//        bt_ip_setting.visibility = View.VISIBLE
        rv_devices_scan.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_devices_scan.adapter = mDeviceListAdapter

        rv_devices_bond.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_devices_bond.adapter = mDeviceListForBondAdapter

        rg_device.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_scan -> {
                    rv_devices_scan.visibility = View.VISIBLE
                    rv_devices_bond.visibility = View.GONE
                }
                R.id.rb_bond -> {
                    rv_devices_scan.visibility = View.GONE
                    rv_devices_bond.visibility = View.VISIBLE
                }
            }
        }
        rb_scan.isChecked = true

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mDeviceListAdapter.refresh(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        iv_return.setOnClickListener { finish() }
        mDeviceListAdapter.setOnItemClickListener { adapter, view, position ->
            val myBluetoothDevice = adapter.data[position] as MyBluetoothDevice
            val device = myBluetoothDevice.device
            if (bleBonded(device)) {
                val intent = Intent(this, DeviceControlActivity::class.java)
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.name)
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.address)
                if (this.mScanning) {
                    this.mBluetoothAdapter?.stopLeScan(this.mLeScanCallback)
                    this.mScanning = false
                }
                startActivity(intent)
            } else {
                ToastUtils.showLong(resources.getString(R.string.tip_connect_device))
            }
        }


        v_transparent_black.setOnClickListener { }
        val dialogAnimationUp = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0F, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f)
        dialogAnimationUp.duration = 400L //???????????????????????????
        val dialogAnimationDown = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0F, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f)
        dialogAnimationDown.duration = 400L //???????????????????????????
        tv_cancel.setOnClickListener {
            closeBottomDialog(dialogAnimationDown)
        }
        mDeviceListAdapter.setOnItemLongClickListener { adapter, view, position ->
            val myBluetoothDevice = adapter.data[position] as MyBluetoothDevice
            currentDevice = myBluetoothDevice.device
            currentDevicePosition = position
            if (bleBonded(myBluetoothDevice.device)) {
                //????????????????????????
                LogUtils.d("????????????")
                showBottomDialog(resources.getString(R.string.un_bind), dialogAnimationUp)
            } else {
                //????????????????????????
                LogUtils.d("????????????")
                showBottomDialog(resources.getString(R.string.bind), dialogAnimationUp)
            }
            true
        }

        tv_bond.setOnClickListener {
            when (currentDevice?.bondState) {
                BluetoothDevice.BOND_BONDED -> {
                    if (ClsUtils.removeBond(BluetoothDevice::class.java, currentDevice)) {
                        ToastUtils.showLong(resources.getString(R.string.success_to_unbind))
                        mDeviceListAdapter.notifyItemChanged(currentDevicePosition)
                    } else {
                        ToastUtils.showLong(resources.getString(R.string.please_unbind_by_yourself))
                        object : Thread() {
                            override fun run() {
                                super.run()
                                sleep(2000) //??????3???
                                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                                startActivity(intent)
                            }
                        }.start()

                    }
                }
                BluetoothDevice.BOND_BONDING -> {
                    ToastUtils.showLong(resources.getString(R.string.binding))
                }
                BluetoothDevice.BOND_NONE -> {
                    currentDevice?.createBond()
                }
            }
            closeBottomDialog(dialogAnimationDown)
        }

        bt_sort.setOnClickListener {
            if (mDeviceListAdapter.data.size > 0) {
                val data = CalculateUtil.sortBLEDevice(mDeviceListAdapter.data)
                mDeviceListAdapter.setNewData(data)
            }
        }
    }

    private fun closeBottomDialog(dialogAnimationDown: TranslateAnimation) {
        mPopupLayout.postDelayed(Runnable {
            mPopupLayout.visibility = View.VISIBLE
            mPopupLayout.startAnimation(dialogAnimationDown)
            mPopupLayout.visibility = View.GONE
        }, 1)
    }

    private fun showBottomDialog(str: String, ctrlAnimation: TranslateAnimation) {
        tv_bond.text = str
        mPopupLayout.postDelayed(Runnable {
            mPopupLayout.visibility = View.VISIBLE
            mPopupLayout.startAnimation(ctrlAnimation)
        }, 1)
    }

    fun bleBonded(device: BluetoothDevice): Boolean {
        return when (device.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                true
            }
            BluetoothDevice.BOND_BONDING -> {
                false
            }
            BluetoothDevice.BOND_NONE -> {
                false
            }
            else -> false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
    }

    @SuppressLint("MissingPermission")
    private fun scanDevice(enable: Boolean) {
        if (enable) {
            iv_refresh.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotaterepeat))
            scanTime = System.currentTimeMillis()
            mHandler!!.postDelayed({
                if (mScanning) {
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    mScanning = false
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
                    iv_refresh.clearAnimation()
                    tv_search.text = getString(R.string.START_SCAN)
                }
            }, SCAN_PERIOD)
            mScanning = true
            tv_search.text = getString(R.string.STOP_SCAN)
            mBluetoothAdapter!!.startLeScan(mLeScanCallback)
        } else {
            iv_refresh.clearAnimation()
            mScanning = false
            tv_search.text = getString(R.string.START_SCAN)
            mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            mHandler!!.removeCallbacksAndMessages(null);
        }
        invalidateOptionsMenu()
    }

    val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, _ ->
        runOnUiThread {
            number_progress_bar.progress = (System.currentTimeMillis() - scanTime).toInt()
            if (number_progress_bar.progress > number_progress_bar.max - 300) number_progress_bar.progress = number_progress_bar.max
            rssiDevice[device] = rssi
            val myBluetoothDevice = MyBluetoothDevice()
            myBluetoothDevice.device = device
            myBluetoothDevice.rssi = rssi
            mDeviceListAdapter.addDevice(myBluetoothDevice)
        }
    }


    private fun checkPermissionAndScanDevice() {
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {

            mDeviceListAdapter.clear()
            addBondedDevice()
            scanDevice(true)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_ble_status -> {
                scanDevice(!mScanning)
                if (mScanning) mDeviceListAdapter.clear()
            }
            R.id.bt_ip_setting -> {
                showNetAddrDlg(this, "????????????", NetUtil().getIP(), NetUtil().getPort())
            }
            R.id.tv_search -> {

                scanDevice(!mScanning)
                if (mScanning) {
                    //???????????????????????????????????????????????????????????????????????????
                    mDeviceListAdapter.clear()
                    addBondedDevice()
                }
            }
        }
    }

    private fun addBondedDevice() {
        if ((mBluetoothAdapter?.bondedDevices?.size)!! > 0) {
            for (i in 0 until mBluetoothAdapter?.bondedDevices!!.size) {
                val device = mBluetoothAdapter?.bondedDevices!!.distinct()[i]
                val myBluetoothDevice = MyBluetoothDevice()
                myBluetoothDevice.device = device
                mDeviceListAdapter.addDevice(myBluetoothDevice)
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

    private var mReceiver: BroadcastReceiver? = null
    private fun registerLoginBroadcast() {
        if (mReceiver == null) {
            mReceiver = BlueToothBondReceiver()
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
            registerReceiver(mReceiver, intentFilter)
        }
    }

    /**
     * ??????????????????
     */
    inner class BlueToothBondReceiver : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                // ??????????????????????????????
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device?.bondState) {
                    BluetoothDevice.BOND_BONDING -> {
                        //????????????
                        LogUtils.d("????????????")
                        if (!mwaitdlg?.isShowing!!) {
                            mwaitdlg?.show()
                        }
                    }
                    BluetoothDevice.BOND_BONDED -> {
                        //????????????
                        if (mwaitdlg?.isShowing!!) {
                            mwaitdlg?.dismiss()
                        }
                        mDeviceListForBondAdapter.setNewData(mBluetoothAdapter?.bondedDevices?.distinct())
                        mDeviceListAdapter.notifyItemChanged(currentDevicePosition)
                        if (bleBonded(currentDevice!!)) {
                            val intent = Intent(this@DeviceListForScanActivity, DeviceControlActivity::class.java)
                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.name)
                            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.address)
                            if (mScanning) {
                                mBluetoothAdapter?.stopLeScan(mLeScanCallback)
                                mScanning = false
                            }
                            startActivity(intent)
                        } else {
                            ToastUtils.showLong(resources.getString(R.string.tip_connect_device))
                        }
                        LogUtils.d("???????????????????????????")
                    }
                    BluetoothDevice.BOND_NONE -> {
                        //????????????/?????????
                        LogUtils.d("????????????/?????????")
                        if (mwaitdlg?.isShowing!!) {
                            mwaitdlg?.dismiss()
                        }
                        mDeviceListAdapter.notifyItemChanged(currentDevicePosition)
                        mDeviceListForBondAdapter.setNewData(mBluetoothAdapter?.bondedDevices?.distinct())
                    }
                    else -> {
                    }
                }
            } else if (intent.action == BluetoothDevice.ACTION_PAIRING_REQUEST) {
                try {
                    //1.????????????
//                    ClsUtils.setPairingConfirmation(BluetoothDevice::class.java, currentDevice,true)
                    //2.??????????????????
                    abortBroadcast() //???????????????????????????????????????????????????????????????????????????
                    //3.??????setPin??????????????????...
                    val ret = ClsUtils.setPin(BluetoothDevice::class.java, currentDevice, "951357")
                    Log.d("zxcv", "????????????$ret")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //????????????
    private fun unRegisterLoginBroadcast() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //????????????
        unRegisterLoginBroadcast()
    }
}