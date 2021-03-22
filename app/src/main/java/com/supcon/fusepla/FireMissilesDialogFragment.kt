package com.supcon.fusepla

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.blankj.utilcode.util.ToastUtils

/**
 * @author : yaobing
 * @date   : 2021/3/22 10:59
 * @desc   :
 */
class FireMissilesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("xcxx")
                .setPositiveButton(
                    "确定"
                ) { dialog, id ->
                    ToastUtils.showShort("ok")
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    ToastUtils.showShort("ok")
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}