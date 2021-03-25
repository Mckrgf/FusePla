package com.supcon.mes.module_beacon_no_login

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon_no_login.event.BleReceiverEvent
import com.supcon.mes.module_beacon_no_login.utils.ClsUtils
import kotlinx.android.synthetic.main.activity_bluetooth_detail.*
import org.greenrobot.eventbus.EventBus

/**
 * @author : yaobing
 * @date   : 2020/11/16 19:00
 * @desc   :
 */
open class BluetoothConnectionActivity : BaseActivity() {
    var mwaitdlg: ProgressDialog? = null
    var mBluetoothAdapter: BluetoothAdapter? = null
    var isBonded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mwaitdlg = ProgressDialog(this)
        mwaitdlg?.isIndeterminate = false //循环滚动
        mwaitdlg?.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mwaitdlg?.setMessage("正在连接蓝牙")
        mwaitdlg?.setCancelable(false) //false不能取消显示，true可以取消显示

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter

        registerLoginBroadcast()
    }

    private var mReceiver: BroadcastReceiver? = null
    fun registerLoginBroadcast() {
        if (mReceiver == null) {
            mReceiver = BlueToothBondReceiver()
            val intentFilter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            registerReceiver(mReceiver, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterLoginBroadcast()
    }

    //取消注册
    fun unRegisterLoginBroadcast() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
            mReceiver = null
        }
    }

    fun removeBond(bleDevice : BluetoothDevice) {
        if (ClsUtils.removeBond(BluetoothDevice::class.java, bleDevice)) {
            ToastUtils.showLong("解除绑定成功")
            bt_bind.visibility = View.VISIBLE
            isBonded = false
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
    lateinit var mListener : BluetoothReceiverReceive


    interface BluetoothReceiverReceive  {
        fun onReeciver(state: Int) {

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
                        LogUtils.d("配对成功，刷新界面")
                        val event = BleReceiverEvent()
                        event.data = BluetoothDevice.BOND_BONDED
                        EventBus.getDefault().post(event)
                    }
                    BluetoothDevice.BOND_NONE -> {
                        //取消配对/未配对
                        LogUtils.d("取消配对/未配对")
                        if (mwaitdlg?.isShowing!!) {
                            mwaitdlg?.dismiss()
                        }
                    }
                }
            }
        }
    }
}