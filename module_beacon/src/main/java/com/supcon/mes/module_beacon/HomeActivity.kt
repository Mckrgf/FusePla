package com.supcon.mes.module_beacon

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.app.annotation.apt.Router
import com.blankj.utilcode.util.ToastUtils
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.middleware.util.NetUtil
import com.supcon.mes.module_beacon.adapter.FunctionListAdapter
import com.supcon.mes.module_beacon.beaconManage.BeaconListActivity
import com.supcon.mes.module_beacon.beaconManage.BeaconListMVPActivity
import com.supcon.mes.module_beacon.bean.Function
import com.supcon.mes.module_beacon.powerSetting.DeviceListForScanActivity
import com.supcon.mes.module_beacon.rssiCorrection.RssiCorrectionActivity
import com.supcon.mes.module_beacon.utils.GPSUtil
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.all_title.*

@Router(value = Constant.Router.BEACON_HOME)
class HomeActivity : BaseActivity() {
    var mFunctionListAdapter : FunctionListAdapter? = FunctionListAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        iv_setting.setOnClickListener {
            showNetAddrDlg(this, resources.getString(R.string.network_setting), NetUtil().getIP(), NetUtil().getPort())
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
        functions.add(Function(resources.getString(R.string.RSSI_COLLECT), R.mipmap.function_rssi_collect))
        functions.add(Function(resources.getString(R.string.BEACON_SETTING), R.mipmap.function_beacon_setting))
        functions.add(Function(resources.getString(R.string.BEACON_MANAGE), R.mipmap.function_beacon_manage))
        functions.add(Function("", R.mipmap.function_empty))
        rv_function.layoutManager = GridLayoutManager(this,4)
        rv_function.adapter = mFunctionListAdapter
        mFunctionListAdapter?.setNewData(functions)

        mFunctionListAdapter?.setOnItemClickListener { _, _, position ->
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
//                    val intent = Intent(this, BeaconListMVPActivity::class.java)
                    val intent = Intent(this, BeaconListActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    ToastUtils.showLong(resources.getString(R.string.not_open))
                }
            }
        }
    }
}