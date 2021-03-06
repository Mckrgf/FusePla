package com.supcon.mes.module_beacon.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.supcon.mes.module_beacon.R
import com.supcon.mes.module_beacon.bean.RssiData
import com.supcon.mes.module_beacon.rssiCorrection.BluetoothDetailActivity
import com.supcon.mes.module_beacon.utils.RssiDataUtil
import java.lang.Exception

/**
 * @author : yaobing
 * @date   : 2020/8/11 15:27
 * @desc   :
 */
class RssiDataListAdapter : BaseQuickAdapter<RssiData, BaseViewHolder>(R.layout.item_rssi_data) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: RssiData) {
        holder.getView<EditText>(R.id.et_distance).setText(if (item.distance == 0f) {""}else {item.distance.toString()})
        holder.getView<EditText>(R.id.et_rssi_average).setText(if (item.rssi == 0f) {""}else {item.rssi.toString()})
        val pos_1:Int = holder.adapterPosition + 1
        holder.getView<TextView>(R.id.tv_position).text =  pos_1.toString()
        holder.getView<TextView>(R.id.et_distance).isEnabled = item.editable

        holder.getView<EditText>(R.id.et_rssi_average).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    if (!TextUtils.isEmpty(s.toString())) {
                        RssiDataUtil.get().mRssiDataList[holder.adapterPosition].rssi = s.toString().toFloat()
                    }else {
                        RssiDataUtil.get().mRssiDataList[holder.adapterPosition].rssi = 0f
                    }
                }catch (e:Exception) {}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.getView<EditText>(R.id.et_distance).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    if (!TextUtils.isEmpty(s.toString())) {
                        RssiDataUtil.get().mRssiDataList[holder.adapterPosition].distance = s.toString().toFloat()
                    }else {
                        RssiDataUtil.get().mRssiDataList[holder.adapterPosition].distance = 0f
                    }
                }catch (e:Exception) { }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        holder.getView<Button>(R.id.bt_scan).setOnClickListener {
            val activity:BluetoothDetailActivity = mContext as BluetoothDetailActivity
            activity.checkPermissionAndScanDevice(holder.adapterPosition)
        }
        holder.getView<Button>(R.id.bt_calculate).setOnClickListener {
            try {
                val rssi = holder.getView<EditText>(R.id.et_rssi_average).text.toString().toDouble()
                val dis = RssiDataUtil.get().getDistance(rssi)
                ToastUtils.showLong(mContext.resources.getString(R.string.distance) + "???$dis")
            } catch (e: Exception) {
                ToastUtils.showLong("$e")
            }
        }

    }

}