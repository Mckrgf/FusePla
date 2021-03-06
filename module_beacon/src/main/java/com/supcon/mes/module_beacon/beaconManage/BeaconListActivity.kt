package com.supcon.mes.module_beacon.beaconManage

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.internal.LinkedTreeMap
import com.supcon.common.BaseConstant
import com.supcon.mes.module_beacon.BaseActivity
import com.supcon.mes.module_beacon.R
import com.supcon.mes.middleware.PLAApplication
import com.supcon.mes.middleware.constant.Constant
import com.supcon.mes.module_beacon.adapter.DeviceListForNetAdapter
import com.supcon.mes.middleware.event.HttpEvent
import com.supcon.mes.module_beacon.net.HttpRequest
import kotlinx.android.synthetic.main.activity_beacon_list.et_search
import kotlinx.android.synthetic.main.activity_beacon_list.rv_devices
import kotlinx.android.synthetic.main.activity_device_list.*
import kotlinx.android.synthetic.main.all_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BeaconListActivity : BaseActivity() {
    private val mDeviceListAdapter = DeviceListForNetAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon_list)
        initView()
        initData()
    }

    private fun initData() {
        HttpRequest().getBeaconList(this)
    }

    private fun initView() {
        iv_return.setOnClickListener { finish() }
        rv_devices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_devices.adapter = mDeviceListAdapter
        mDeviceListAdapter.setOnItemClickListener { adapter, view, position ->
            val data:LinkedTreeMap<String,Any> = mDeviceListAdapter.data[position] as LinkedTreeMap<String, Any>
            val sn = data["sn"].toString()
            val intent = Intent(this, BeaconDetailActivity::class.java)
            intent.putExtra("sn",sn)
            intent.putExtra("data",data)
            val bundle = Bundle()
            bundle.putString(BaseConstant.WEB_COOKIE, PLAApplication.getCooki())
            bundle.putString(BaseConstant.WEB_AUTHORIZATION, PLAApplication.getToken())
            bundle.putString(BaseConstant.WEB_URL, "www.baidu.com")
            bundle.putBoolean(Constant.WebUrl.HAS_TITLE, false)
            intent.putExtras(bundle)
            startActivityForResult(intent,100)
        }


        fragment_title_des.text = resources.getString(R.string.BEACON_MANAGE)
        fragment_title_des.setTextColor(resources.getColor(R.color.black))
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mDeviceListAdapter.refresh(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HttpEvent?) {
        when (event?.requestCode) {
            HttpRequest.REQUEST_CODE_GET_BEACON_LIST ->  {
                when(event.code) {
                    200 -> {
                        val data = event.data as LinkedTreeMap<*, *>
                        val list = data["list"] as java.util.ArrayList< LinkedTreeMap<String, Any>>
                        mDeviceListAdapter.setdata(list)
                        tv_count.text = "${mDeviceListAdapter.data?.size} " + resources.getString(R.string.ALL_DEVICE_FOUND)

                    }
                    else -> {
                        ToastUtils.showLong(resources.getString(com.supcon.mes.middleware.R.string.operation_failed) + event.msg)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            100 -> {
                when(resultCode) {
                    200 -> {
                        HttpRequest().getBeaconList(this)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}