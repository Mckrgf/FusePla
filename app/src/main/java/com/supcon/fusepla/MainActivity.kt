package com.supcon.fusepla

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rg_all.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
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
    }

    private fun checkConfirm(i: Int) {
        when(i) {
            0 -> {
                val newFragment = FireMissilesDialogFragment()
                newFragment.show(supportFragmentManager, "missiles")

            }
        }
    }
}