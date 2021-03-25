package com.supcon.mes.app

import android.os.Bundle
import com.app.annotation.apt.Router
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.supcon.app.CustomDialogFragment
import com.supcon.common.view.base.activity.BaseActivity
import com.supcon.mes.R
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_login.IntentRouter
import kotlinx.android.synthetic.main.activity_main1.*

@Router(value = Constant.Router.ROOTACTIVITY)
class MainActivity : BaseActivity() {
    private lateinit var newFragment: CustomDialogFragment
    override fun getLayoutID(): Int {
        return R.layout.activity_main1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                IntentRouter.go(this@MainActivity, Constant.Router.WELCOME, Bundle())
            }
            SP_APP_MODE_B -> {
                // TODO: 2021/3/25 跳转到B模块
            }
            SP_APP_MODE_C -> {
                // TODO: 2021/3/25 跳转到scanwriter
            }
        }
    }

    private fun checkConfirm(i: Int) {
        when (i) {
            0 -> {
                newFragment.show(
                    supportFragmentManager,
                    SP_APP_MODE_A
                )
            }
            1 -> {
                newFragment.show(
                    supportFragmentManager,
                    SP_APP_MODE_B
                )
            }
            2 -> {
                newFragment.show(
                    supportFragmentManager,
                    SP_APP_MODE_C
                )
            }

        }

    }

    fun goNext(tag: String) {
        when(tag) {
            SP_APP_MODE_A -> {
                IntentRouter.go(this@MainActivity, Constant.Router.WELCOME, Bundle())
            }
            SP_APP_MODE_A -> {

            }
            SP_APP_MODE_A -> {

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