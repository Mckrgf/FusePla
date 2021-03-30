package com.supcon.mes.module_beacon.adapter

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supcon.common.BaseConstant
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_beacon.bean.MyBluetoothDevice
import com.supcon.mes.module_beacon.rssiCorrection.BluetoothDetailActivity
import com.supcon.mes.module_beacon.rssiCorrection.DialogMapActivity
import com.supcon.mes.module_beacon.rssiCorrection.RssiCorrectionActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:27
 * @desc   : 收集rssi用的搜索列表适配器
 */
class DeviceListForRssiAdapter(rssiCorrectionActivity: RssiCorrectionActivity) : BaseQuickAdapter<MyBluetoothDevice, BaseViewHolder>(R.layout.item_ble_device) {
    var mLeDevices: ArrayList<MyBluetoothDevice>? = null
    init {
        mLeDevices = ArrayList()
    }
    var activity = rssiCorrectionActivity
    override fun convert(holder: BaseViewHolder, item: MyBluetoothDevice) {
        holder.getView<TextView>(R.id.tv_name).text = item.device.name
        holder.getView<TextView>(R.id.tv_address).text = item.device.address
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
            holder.getView<TextView>(R.id.tv_n).text = "null"
            holder.getView<TextView>(R.id.tv_floor).text = "null"
            holder.getView<ImageView>(R.id.iv_map).setImageResource(R.mipmap.iv_map_b)
        }

        holder.getView<TextView>(R.id.tv_address).text = mContext.resources.getString(R.string.rssi) + item.rssi

        holder.getView<ImageView>(R.id.iv_map).setOnClickListener {
            val device = data[holder.adapterPosition].device
            val intent = Intent(activity, DialogMapActivity::class.java)
            intent.putExtra("device",device)
            val bundle = Bundle()
            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
            activity.startActivity(intent)
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
            setNewData(mLeDevices)
        }
    }

    fun clear() {
        mLeDevices?.clear()
        notifyDataSetChanged()
    }

    fun refresh(name: String) {
        val mLeDevicesSearched: ArrayList<MyBluetoothDevice>? = ArrayList()
        if (null != mLeDevices && mLeDevices?.size!! > 0)
        for (i in 0 until mLeDevices!!.size) {
            if ((mLeDevices!![i].device.name.toLowerCase(Locale.ROOT)).contains(name.toLowerCase(Locale.ROOT))) {
                mLeDevicesSearched?.add(mLeDevices!![i])
            }
        }
        setNewData(mLeDevicesSearched)
    }
}