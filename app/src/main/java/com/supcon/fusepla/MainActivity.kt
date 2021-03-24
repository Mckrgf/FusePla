package com.supcon.fusepla

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var newFragment: CustomDialogFragment
    var aa = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newFragment = CustomDialogFragment()

        rg_all.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_a -> {
                    checkConfirm(0)
                }
                R.id.rb_b -> {
                    checkConfirm(1)
                }
                R.id.rb_c -> {
                    checkConfirm(2)

                }
            }
        }

        when (SPUtils.getInstance().getString(SP_KEY_APP_MODE, "")) {
            "" -> {
                ToastUtils.showShort("还未选择APP模式")
            }
            SP_APP_MODE_A -> {
                ToastUtils.showShort("模式A已经选择")
                rb_a.isChecked = true
            }
            SP_APP_MODE_B -> {
                ToastUtils.showShort("还未整合B")
                rb_b.isChecked = true
            }
            SP_APP_MODE_C -> {
                ToastUtils.showShort("还未整合C")
                rb_c.isChecked = true
            }
        }
    }

    private fun checkConfirm(i: Int) {
        aa++
        if (aa > 1) {
            when (i) {
                0 -> {
                    newFragment.show(supportFragmentManager, SP_APP_MODE_A)
                }
                1 -> {
                    newFragment.show(supportFragmentManager, SP_APP_MODE_B)
                }
                2 -> {
                    newFragment.show(supportFragmentManager, SP_APP_MODE_C)
                }
            }
        }

    }

    companion object {
        val SP_KEY_APP_MODE = "SP_KEY_APP_MODE"
        val SP_APP_MODE_A = "SP_APP_MODE_A"
        val SP_APP_MODE_B = "SP_APP_MODE_B"
        val SP_APP_MODE_C = "SP_APP_MODE_C"
    }
}