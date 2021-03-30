package com.supcon.mes.module_beacon.powerSetting

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
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.BaseActivity
import com.supcon.mes.module_beacon.REQUEST_ENABLE_BT
import com.supcon.mes.module_beacon.SCAN_PERIOD
import com.supcon.mes.module_beacon.adapter.DeviceListForBondAdapter
import com.supcon.mes.module_beacon.adapter.DeviceListForConnectAdapter
import com.supcon.mes.module_beacon.bean.MyBluetoothDevice
import com.supcon.mes.module_beacon.utils.CalculateUtil
import com.supcon.mes.module_beacon.utils.ClsUtils
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
    private var currentDevicePosition: Int = -1

    private var scanTime: Long = 0
    private var mwaitdlg: ProgressDialog? = null

    private var rssiDevice : HashMap<BluetoothDevice,Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_list)

        initView()
        initData()
        checkPermissionAndScanDevice()
    }

    private fun initData() {
        mHandler = Handler()
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        val list = mutableListOf(mBluetoothAdapter?.bondedDevices?.distinct())
        if ((mBluetoothAdapter?.bondedDevices?.size)!! > 0) {
            mDeviceListForBondAdapter.setNewData(mBluetoothAdapter?.bondedDevices?.distinct())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        registerLoginBroadcast()
        mwaitdlg = ProgressDialog(this)
        mwaitdlg?.isIndeterminate = false //循环滚动

        mwaitdlg?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mwaitdlg?.setMessage(resources.getString(R.string.binding_bluetooth))
        mwaitdlg?.setCancelable(false) //false不能取消显示，true可以取消显示


        fragment_title_des.text = getString(R.string.app_name_module) + AppUtils.getAppVersionName()
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        number_progress_bar.max = SCAN_PERIOD.toInt()
        tv_search.setOnClickListener(this)
        tv_search.visibility = View.VISIBLE
        iv_return.setOnClickListener(this)
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

        val dialogAnimationUp = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0F, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f, TranslateAnimation.RELATIVE_TO_SELF, 0f)
        dialogAnimationUp.duration = 400L //设置动画的过渡时间
        val dialogAnimationDown = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0F, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 0f, TranslateAnimation.RELATIVE_TO_SELF, 1f)
        dialogAnimationDown.duration = 400L //设置动画的过渡时间
        tv_cancel.setOnClickListener {
            closeBottomDialog(dialogAnimationDown)
        }

        tv_bond.setOnClickListener {
            when (currentDevice?.bondState) {
                BluetoothDevice.BOND_BONDED -> {
                    if (ClsUtils.removeBond(BluetoothDevice::class.java, currentDevice)) {
                        ToastUtils.showLong(resources.getString(R.string.success_to_unbind))
                        mDeviceListAdapter.notifyItemChanged(currentDevicePosition)
                    } else {
                        ToastUtils.showLong("该系统不支持在app内解除蓝牙绑定，请自行解绑")
                        object : Thread() {
                            override fun run() {
                                super.run()
                                sleep(2000) //休眠3秒
                                val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                                startActivity(intent)
                            }
                        }.start()

                    }
                }
                BluetoothDevice.BOND_BONDING -> {
                    ToastUtils.showLong("正在绑定中")
                }
                BluetoothDevice.BOND_NONE -> {
                    currentDevice?.createBond()
                }
            }
            closeBottomDialog(dialogAnimationDown)
        }
        v_transparent_black.setOnClickListener { }

        mDeviceListAdapter.setOnItemLongClickListener { adapter, view, position ->
            val myBluetoothDevice = adapter.data[position] as MyBluetoothDevice
            currentDevice = myBluetoothDevice.device
            currentDevicePosition = position
            if (bleBonded(myBluetoothDevice.device)) {
                //底部弹窗解绑设备
                LogUtils.d("弹窗解绑")
                showBottomDialog(resources.getString(R.string.un_bind), dialogAnimationUp)
            } else {
                //底部弹窗绑定设备
                LogUtils.d("弹窗绑定")
                showBottomDialog(resources.getString(R.string.bind), dialogAnimationUp)
            }
            true
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
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


    /**
     *
     */
    private fun scanDevice(enable: Boolean) {
        if (enable) {
            scanTime = System.currentTimeMillis()
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

    val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, _ ->
        runOnUiThread {
            number_progress_bar.progress = (System.currentTimeMillis() - scanTime).toInt()
            if (number_progress_bar.progress > number_progress_bar.max - 300) number_progress_bar.progress = number_progress_bar.max

            rssiDevice[device] = rssi
            val myBluetoothDevice = MyBluetoothDevice()
            myBluetoothDevice.device = device
            myBluetoothDevice.rssi = rssi
            mDeviceListAdapter.addDevice(myBluetoothDevice)

            tv_count.text = "${mDeviceListAdapter.mLeDevices?.size} " + resources.getString(R.string.ALL_DEVICE_FOUND)

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search -> {

                scanDevice(!mScanning)
                if (mScanning) {
                    //如果是开始扫描，就先清除设备，然后添加绑定好的设备
                    mDeviceListAdapter.clear()
                    addBondedDevice()
                }
            }
            R.id.iv_return -> {
                finish()
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
     * 蓝牙配对广播
     */
    inner class BlueToothBondReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                // 找到设备后获取其设备
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when (device?.bondState) {
                    BluetoothDevice.BOND_BONDING -> {
                        //正在配对
                        LogUtils.d("正在配对")
                        if (!mwaitdlg?.isShowing!!) {
                            mwaitdlg?.show()
                        }
                    }
                    BluetoothDevice.BOND_BONDED -> {
                        //配对结束
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
                    }
                    BluetoothDevice.BOND_NONE -> {
                        //取消配对/未配对
                        LogUtils.d("取消配对/未配对")
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
                    //1.确认配对
//                    ClsUtils.setPairingConfirmation(BluetoothDevice::class.java, currentDevice,true)
                    //2.终止有序广播
                    abortBroadcast() //如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    val ret = ClsUtils.setPin(BluetoothDevice::class.java, currentDevice, "951357")
                    Log.d("zxcv", "配对结果$ret")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //取消注册
    private fun unRegisterLoginBroadcast() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //取消注册
        unRegisterLoginBroadcast()
    }
}