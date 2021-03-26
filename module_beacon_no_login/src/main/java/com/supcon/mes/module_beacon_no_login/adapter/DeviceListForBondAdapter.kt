package com.supcon.mes.module_beacon_no_login.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.supcon.mes.module_beacon_no_login.powerSetting.DeviceControlActivity
import com.supcon.mes.module_beacon_no_login.powerSetting.DeviceListForScanActivity
import com.supcon.mes.module_beacon_no_login.utils.ClsUtils
import com.supcon.supbeacon.R
import java.util.*

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:27
 * @desc   : 收集rssi用的搜索列表适配器
 */
@Suppress("DEPRECATION")
class DeviceListForBondAdapter(activity: DeviceListForScanActivity) : BaseQuickAdapter<BluetoothDevice,BaseViewHolder>(R.layout.item_ble_device_for_bond) {
    private var mActivity:DeviceListForScanActivity = activity


    private var mLeDevices: ArrayList<BluetoothDevice>? = null
    init {
        mLeDevices = ArrayList()
    }
    @SuppressLint("MissingPermission")
    override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
        holder.getView<TextView>(R.id.tv_name).text = item.name
        holder.getView<TextView>(R.id.tv_address).text = item.address
        holder.getView<Button>(R.id.bt_unbind).setOnClickListener {
            ClsUtils.removeBond(BluetoothDevice::class.java,item)
        }

        holder.getView<Button>(R.id.bt_connect).setOnClickListener {
            val intent = Intent(mActivity, DeviceControlActivity::class.java)
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, item.getName())
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, item.getAddress())
            if (mActivity.mScanning) {
                mActivity.mBluetoothAdapter?.stopLeScan(mActivity.mLeScanCallback)
                mActivity.mScanning = false
            }
            mActivity.startActivity(intent)
        }
    }

}