package com.supcon.app

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.blankj.utilcode.util.SPUtils
import com.supcon.mes.R
import com.supcon.mes.app.MainActivity

/**
 * @author : yaobing
 * @date   : 2021/3/22 10:59
 * @desc   :
 */
class CustomDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(resources.getString(R.string.confirm_to_choose_this_mode))
                .setPositiveButton(
                    resources.getString(R.string.confirm)
                ) { dialog, id ->
                    SPUtils.getInstance().put(MainActivity.SP_KEY_APP_MODE,tag!!)
                    var activity : MainActivity = activity as MainActivity
                    activity.goNext(tag!!)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}