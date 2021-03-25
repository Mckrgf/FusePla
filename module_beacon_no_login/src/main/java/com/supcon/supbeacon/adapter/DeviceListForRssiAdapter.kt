package com.supcon.supbeacon.adapter

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supcon.supbeacon.BLE_SELECTOR
import com.supcon.supbeacon.R
import com.supcon.supbeacon.beaconManage.DialogMapActivity
import com.supcon.supbeacon.bean.MyBluetoothDevice
import com.supcon.supbeacon.powerSetting.DeviceListForScanActivity
import com.supcon.supbeacon.rssiCorrection.RssiCorrectionActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:27
 * @desc   : 收集rssi用的搜索列表适配器
 */
class DeviceListForRssiAdapter(activity: RssiCorrectionActivity) : BaseQuickAdapter<MyBluetoothDevice,BaseViewHolder>(R.layout.item_ble_device) {
    var mLeDevices: ArrayList<MyBluetoothDevice>? = null
    private var mActivity: RssiCorrectionActivity = activity

    init {
        mLeDevices = ArrayList()
    }
    override fun convert(holder: BaseViewHolder, item: MyBluetoothDevice) {

        holder.getView<TextView>(R.id.tv_name).text = item.device.name
        holder.getView<TextView>(R.id.tv_address).text = "信号强度：" + item.rssi

        holder.getView<ImageView>(R.id.iv_map).setOnClickListener {
            val device = data[holder.adapterPosition] as BluetoothDevice
            val intent = Intent(mActivity, DialogMapActivity::class.java)
            intent.putExtra("device",device)
            mActivity.startActivity(intent)
        }

        val d = SPUtils.getInstance().getString(item.device.address)
        if (!TextUtils.isEmpty(d)) {
            val returnData: JsonObject = JsonParser().parse(d).asJsonObject
            holder.getView<TextView>(R.id.tv_n).text = returnData["n"].toString()
            holder.getView<TextView>(R.id.tv_floor).text = returnData["floor"].toString()
            if (TextUtils.isEmpty(returnData["x"].toString()) && TextUtils.isEmpty(returnData["y"].toString())&& TextUtils.isEmpty(returnData["z"].toString())) {
                holder.getView<ImageView>(R.id.iv_map).setImageResource(R.mipmap.iv_map_a)
            }else {
                holder.getView<ImageView>(R.id.iv_map).setImageResource(R.mipmap.iv_map_b)
            }
        }else {
            holder.getView<TextView>(R.id.tv_n).text = "信号强度-无数据"
            holder.getView<TextView>(R.id.tv_floor).text = "楼层-无数据"
            holder.getView<ImageView>(R.id.iv_map).setImageResource(R.mipmap.iv_map_b)
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
        if (null != mLeDevices && mLeDevices?.size!! > 0) {
            for (i in 0 until mLeDevices!!.size) {
                if ((mLeDevices!![i].device.name.toLowerCase(Locale.ROOT)).contains(name.toLowerCase(Locale.ROOT))) {
                    mLeDevicesSearched?.add(mLeDevices!![i])
                }
            }
            setList(mLeDevicesSearched)
        }

    }
}