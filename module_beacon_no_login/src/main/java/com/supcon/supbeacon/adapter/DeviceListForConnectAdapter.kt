package com.supcon.supbeacon.adapter

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.supcon.supbeacon.BLE_SELECTOR
import com.supcon.supbeacon.R
import com.supcon.supbeacon.beaconManage.DialogMapActivity
import com.supcon.supbeacon.bean.MyBluetoothDevice
import com.supcon.supbeacon.powerSetting.DeviceControlActivity
import com.supcon.supbeacon.powerSetting.DeviceListForScanActivity
import java.util.*

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:27
 * @desc   : 搜索到准备绑定（bond）的搜索列表适配器
 */
class DeviceListForConnectAdapter(activity: DeviceListForScanActivity) : BaseQuickAdapter<MyBluetoothDevice, BaseViewHolder>(R.layout.item_ble_device_for_connect) {
    private var mActivity: DeviceListForScanActivity = activity
    private var mLeDevices: ArrayList<MyBluetoothDevice>? = null
    private var currentClick = -1

    init {
        mLeDevices = ArrayList()
    }

    override fun convert(holder: BaseViewHolder, item: MyBluetoothDevice) {
        holder.getView<TextView>(R.id.tv_name).text = item.device.name
//        holder.getView<TextView>(R.id.tv_address).text = item.device.address
        var bondState = ""
        var canBeBonded = false
        when (item.device.bondState) {
            BluetoothDevice.BOND_BONDED -> {
                bondState = "已经绑定"
                canBeBonded = false
            }
            BluetoothDevice.BOND_BONDING -> {
                bondState = "正在绑定..."
                canBeBonded= false
            }
            BluetoothDevice.BOND_NONE -> {
                bondState = "未绑定"
                canBeBonded = true
            }
        }
        holder.getView<TextView>(R.id.tv_address).text = "信号强度：" + item.rssi

        holder.getView<TextView>(R.id.tv_device_bond_state).text = bondState
        holder.getView<ImageView>(R.id.iv_map).setOnClickListener {
            val device = data[holder.adapterPosition] as BluetoothDevice
            val intent = Intent(mActivity, DialogMapActivity::class.java)
            intent.putExtra("device",device)
            mActivity.startActivity(intent)
        }
    }

    fun addDevice(currentDevice: MyBluetoothDevice?) {
        if (!mLeDevices!!.contains(currentDevice!!) && !TextUtils.isEmpty(currentDevice.device.name) && currentDevice.device.name.contains("")) {
            var deviceUnique = true //默认设备是唯一的
            var repeatPosition = -1
            for (i in 0 until mLeDevices!!.size) {
                val device = mLeDevices!![i]
                if (device.device == currentDevice!!.device) {
                    deviceUnique = false
                    repeatPosition = i
                    break
                }
            }
            if (deviceUnique) {
                //设备唯一，则直接添加
                mLeDevices!!.add(currentDevice!!)
            }else {
                //设备不唯一，则更新rssi
                mLeDevices!!.removeAt(repeatPosition)
                mLeDevices!!.add(repeatPosition,currentDevice!!)
            }
            setList(mLeDevices)
        }
    }


    fun clear() {
        mLeDevices?.clear()
        notifyDataSetChanged()
    }

    fun refresh(name: String) {
        val mLeDevicesSearched: ArrayList<MyBluetoothDevice>? = ArrayList()
        if (null != mLeDevices && mLeDevices?.size!! > 0)
            for (i in 0 until mLeDevices?.size!!) {
                val data : MyBluetoothDevice = mLeDevices!![i] as MyBluetoothDevice
                val s = data.device.name
                if (s.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                    mLeDevicesSearched?.add(data)
                }
            }
        setNewData(mLeDevicesSearched)
    }
}