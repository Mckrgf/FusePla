package com.supcon.supbeacon.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.supcon.supbeacon.R
import com.supcon.supbeacon.bean.Function
import com.supcon.supbeacon.bean.RssiData
import com.supcon.supbeacon.utils.RssiDataUtil
import java.lang.Exception

/**
 * @author : yaobing
 * @date   : 2020/8/11 15:27
 * @desc   :
 */
class FunctionListAdapter : BaseQuickAdapter<Function,BaseViewHolder>(R.layout.item_function) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: Function) {
        holder.getView<ImageView>(R.id.iv_function).setImageResource(item.image)
        holder.getView<TextView>(R.id.tv_function).text = item.nama
    }

}