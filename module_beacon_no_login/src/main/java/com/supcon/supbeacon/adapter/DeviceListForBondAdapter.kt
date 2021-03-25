package com.supcon.supbeacon.adapter

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.supcon.supbeacon.R
import com.supcon.supbeacon.powerSetting.DeviceControlActivity
import com.supcon.supbeacon.powerSetting.DeviceListForScanActivity
import com.supcon.supbeacon.utils.ClsUtils
import java.util.*

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:27
 * @desc   : 收集rssi用的搜索列表适配器
 */
class DeviceListForBondAdapter(activity: DeviceListForScanActivity) : BaseQuickAdapter<BluetoothDevice,BaseViewHolder>(R.layout.item_ble_device_for_bond) {
    private var mActivity:DeviceListForScanActivity = activity


    private var mLeDevices: ArrayList<BluetoothDevice>? = null
    init {
        mLeDevices = ArrayList()
    }
    override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
        holder.getView<TextView>(R.id.tv_name).text = item.name
        holder.getView<TextView>(R.id.tv_address).text = item.address
        holder.getView<Button>(R.id.bt_unbind).setOnClickListener {
            ClsUtils.removeBond(BluetoothDevice::class.java,item)
        }

        holder.getView<Button>(R.id.bt_connect).setOnClickListener {
            val intent = Intent(context, DeviceControlActivity::class.java)
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, item.getName())
            intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, item.getAddress())
            if (mActivity.mScanning) {
                mActivity.mBluetoothAdapter?.stopLeScan(mActivity.mLeScanCallback)
                mActivity.mScanning = false
            }
            context.startActivity(intent)
        }
    }

}