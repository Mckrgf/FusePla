package com.supcon.mes.module_beacon.adapter

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.supcon.mes.R
import com.supcon.mes.module_beacon.bean.Function

/**
 * @author : yaobing
 * @date   : 2020/8/11 15:27
 * @desc   :
 */
class FunctionListAdapter : BaseQuickAdapter<Function, BaseViewHolder>(R.layout.item_function) {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: Function) {
        holder.getView<ImageView>(R.id.iv_function).setImageResource(item.image)
        holder.getView<TextView>(R.id.tv_function).text = item.nama
    }

}