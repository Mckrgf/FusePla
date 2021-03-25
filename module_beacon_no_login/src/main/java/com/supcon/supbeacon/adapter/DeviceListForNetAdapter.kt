package com.supcon.supbeacon.adapter

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.internal.LinkedTreeMap
import com.supcon.supbeacon.R
import com.supcon.supbeacon.beaconManage.BeaconListActivity
import com.supcon.supbeacon.beaconManage.DialogMapActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author : yaobing
 * @date   : 2020/1/1 15:27
 * @desc   : 网络获取的
 */
@Suppress("UNCHECKED_CAST")
class DeviceListForNetAdapter(beaconListActivity: BeaconListActivity) : BaseQuickAdapter< LinkedTreeMap<String, Any>, BaseViewHolder>(R.layout.item_ble_device_for_net) {
    private var dataAll: ArrayList<LinkedTreeMap<String, Any>> = ArrayList()
    var activity = beaconListActivity

    override fun convert(holder: BaseViewHolder, item:  LinkedTreeMap<String, Any>) {
        val data : LinkedTreeMap<String, LinkedTreeMap<String, Any>> = item as LinkedTreeMap<String,  LinkedTreeMap<String, Any>>
        holder.getView<TextView>(R.id.tv_name).text = data["sn"].toString()
        holder.getView<TextView>(R.id.tv_address).text = data["mac"].toString()

        holder.getView<ImageView>(R.id.iv_map).setOnClickListener {
//            val device = data[holder.adapterPosition] as BluetoothDevice
            val intent = Intent(activity, DialogMapActivity::class.java)
            intent.putExtra("deviceName",data["sn"].toString())
            intent.putExtra("deviceAddress",data["mac"].toString())
            val bundle = Bundle()
//            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
//            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
//            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
//            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }
    fun refresh(name: String) {
        val mLeDevicesSearched: ArrayList<LinkedTreeMap<String, Any>>? = ArrayList()
        if (dataAll.size > 0)
            for (i in 0 until dataAll.size) {
                val data : LinkedTreeMap<String,Any> = dataAll[i] as LinkedTreeMap<String, Any>
                val s = data["sn"].toString()
                if (s.toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                    mLeDevicesSearched?.add(data)
                }
            }
        setNewData(mLeDevicesSearched)
    }

    fun setdata(data :  ArrayList<LinkedTreeMap<String, Any>>) {
        dataAll = data
        setNewData(dataAll)
    }

}