package com.supcon.mes.module_beacon_no_login

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon_no_login.adapter.FunctionListAdapter
import com.supcon.mes.module_beacon_no_login.beaconManage.BeaconListActivity
import com.supcon.mes.module_beacon_no_login.powerSetting.DeviceListForScanActivity
import com.supcon.mes.module_beacon_no_login.rssiCorrection.RssiCorrectionActivity
import com.supcon.mes.module_beacon_no_login.utils.GPSUtil
import com.supcon.mes.module_beacon_no_login.utils.NetUtil
import com.supcon.supbeacon.R
import com.supcon.mes.module_beacon_no_login.bean.Function
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.all_title.*

class HomeActivity : BaseActivity() {
    var mFunctionListAdapter : FunctionListAdapter? = FunctionListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        iv_setting.setOnClickListener {
            showNetAddrDlg(this, "网络配置", NetUtil().getIP(), NetUtil().getPort())
        }

        initFunction()
        bt_rssi.visibility = View.GONE
        bt_ble_power.visibility = View.GONE
        bt_ip_setting.visibility = View.GONE
        bt_ble_status.visibility = View.GONE
        iv_return.visibility = View.GONE
        iv_setting.visibility = View.VISIBLE
        val gpsUtil = GPSUtil()
        if (!gpsUtil.isOPen(this)) {
            ToastUtils.showLong("请开启定位信息（不用于收集个人信息），如不知道密码，可以在系统下拉菜单上把 位置信息 按钮打开")
            gpsUtil.openGPS(this)
        }
    }

    private fun initFunction() {
        val functions = ArrayList<Function>()
        functions.add(Function("rssi收集",R.mipmap.function_rssi_collect))
        functions.add(Function("信标设置",R.mipmap.function_beacon_setting))
        functions.add(Function("信标管理", R.mipmap.function_beacon_manage))
        functions.add(Function("",R.mipmap.function_empty))
        rv_function.layoutManager = GridLayoutManager(this,4)
        rv_function.adapter = mFunctionListAdapter
        mFunctionListAdapter?.setNewData(functions)

        mFunctionListAdapter?.setOnItemClickListener { adapter, view, position ->
            when(position) {
                0 -> {
                    val intent = Intent(this, RssiCorrectionActivity::class.java)
                    startActivity(intent)
                }
                1 -> {
                    val intent = Intent(this, DeviceListForScanActivity::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this, BeaconListActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    ToastUtils.showLong("暂未开放")
                }
            }
        }
    }
}