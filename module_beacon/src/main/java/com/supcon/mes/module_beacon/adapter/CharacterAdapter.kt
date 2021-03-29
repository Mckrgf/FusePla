package com.supcon.mes.module_beacon.adapter

import android.os.LocaleList
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.supcon.mes.R
import com.supcon.mes.module_beacon.powerSetting.DeviceControlActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @author : yaobing
 * @date   : 2020/1/1 15:27
 * @desc   : 网络获取的
 */
@Suppress("UNCHECKED_CAST")
class CharacterAdapter(activity: DeviceControlActivity) : BaseQuickAdapter<HashMap<String, String>, BaseViewHolder>(R.layout.item_ble_charactere) {
    private val mActivity = activity
    private var dataAll: ArrayList<HashMap<String, String>> = ArrayList()
    override fun convert(holder: BaseViewHolder, item: HashMap<String, String>) {

        val locale: LocaleList = mContext.resources.configuration.locales
        if (locale[0].toString().contains("en_")) {
            when(item["NAME"]) {
                "信号强度" -> {
                    item["NAME"] = "rssi"
                }
                "广播间隔" -> {
                    item["NAME"] = "broadcast interval"
                }
                "单位距离RSSI" -> {
                    item["NAME"] = "rssi/m"
                }
                "设备ID" -> {
                    item["NAME"] = "device ID"
                }
                "UUID" -> {
                    item["NAME"] = "UUID"
                }
                "密码" -> {
                    item["NAME"] = "password"
                }
            }
        }
        holder.getView<TextView>(R.id.tv_character_name).text = item["NAME"]

        var aaa = item["VALUE"]?.trim()
        aaa = aaa?.replace(" ","")

        val digitsHex = "0123456789abcdefABCDEF"
        val digits = "0123456789"

        if (item["NAME"]?.equals("设备ID")!!) {
            holder.getView<TextView>(R.id.tv_character_value).text = item["VALUE"]?.trim()
            holder.getView<TextView>(R.id.tv_character_value).inputType = InputType.TYPE_CLASS_TEXT
            holder.getView<TextView>(R.id.tv_character_value).keyListener = DigitsKeyListener.getInstance(digitsHex)
        }else {
            holder.getView<TextView>(R.id.tv_character_value).keyListener = DigitsKeyListener.getInstance(digits)
        }
        holder.getView<TextView>(R.id.tv_character_value).text = aaa
        holder.getView<Button>(R.id.tv_character_get).setOnClickListener {
            mActivity.getValue(holder.adapterPosition,item["NAME"])
        }
        holder.getView<TextView>(R.id.tv_character_value).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                data[holder.adapterPosition]["VALUE"] = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}