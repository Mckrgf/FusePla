package com.supcon.mes.module_beacon_no_login.net

import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.supcon.mes.module_beacon_no_login.BaseActivity
import com.supcon.mes.module_beacon_no_login.bean.OkResponse
import com.supcon.mes.module_beacon_no_login.event.HttpEvent
import com.supcon.mes.module_beacon_no_login.utils.NetUtil
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * @author : yaobing
 * @date   : 2020/8/6 10:37
 * @desc   : 网咯请求管理
 */
class HttpRequest {

    /**
     * 根据sn码获取设备信息
     */
    fun getBeacon(baseActivity: BaseActivity?, sn: String) {
        OkGo.get<String>(NetUtil().getBeacon()  + sn)
            .tag(baseActivity)
            .execute(object : StringDialogCallback(baseActivity,true) {
                override fun onSuccess(response: Response<String>) {
                    val mResponse = Gson().fromJson(response.body().toString(), OkResponse::class.java)
                    val event = HttpEvent()
                    event.code = mResponse.code
                    event.data = mResponse.data
                    event.msg = mResponse.msg
                    event.requestCode = REQUEST_CODE_GET_BEACON
                    EventBus.getDefault().post(event)
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    ToastUtils.showLong("获取信标信息失败")
                }
            })
    }

    /**
     * 根据设备sn更新设备
     *
     * @param baseActivity
     */
    fun updateBeacon(baseActivity: BaseActivity?, jsonObject: String,sn: String) {
        OkGo.put<String>(NetUtil().updateBeacon()+ sn)
            .tag(baseActivity)
            .upJson(jsonObject)
            .execute(object : StringDialogCallback(baseActivity,true) {
                override fun onSuccess(response: Response<String>) {
                    val mResponse = Gson().fromJson(response.body().toString(), OkResponse::class.java)
                    val event = HttpEvent()
                    event.code = mResponse.code
                    event.data = mResponse.data
                    event.msg = mResponse.msg
                    event.requestCode = REQUEST_CODE_UPDATE_BEACON
                    EventBus.getDefault().post(event)
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    ToastUtils.showLong("更新信标信息失败")
                }
            })
    }
    /**
     * 根据设备sn更新设备
     *
     * @param baseActivity
     */
    fun addBeacon(baseActivity: BaseActivity?, jsonObject: String) {
        OkGo.post<String>(NetUtil().addBeacon())
            .tag(baseActivity)
            .upJson(jsonObject.toString())
            .execute(object : StringDialogCallback(baseActivity,true) {
                override fun onSuccess(response: Response<String>) {
                    val mResponse = Gson().fromJson(response.body().toString(), OkResponse::class.java)
                    val event = HttpEvent()
                    event.code = mResponse.code
                    event.data = mResponse.data
                    event.msg = mResponse.msg
                    event.requestCode = REQUEST_CODE_ADD_BEACON
                    EventBus.getDefault().post(event)
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    ToastUtils.showLong("添加信标信息失败")
                }
            })
    }

    /**
     *  获取设备列表
     */
    fun getBeaconList(baseActivity: AppCompatActivity?) {
        OkGo.get<String>(NetUtil().getBeaconList())
            .tag(baseActivity)
            .execute(object : StringDialogCallback(baseActivity,true) {
                override fun onSuccess(response: Response<String>) {
                    val mResponse = Gson().fromJson(response.body().toString(), OkResponse::class.java)
                    val event = HttpEvent()
                    event.code = mResponse.code
                    event.data = mResponse.data
                    event.msg = mResponse.msg
                    event.requestCode = REQUEST_CODE_GET_BEACON_LIST
                    EventBus.getDefault().post(event)
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    ToastUtils.showLong("获取信标信息失败")
                }
            })
    }

    /**
     * 根据设备sn删除设备
     *
     * @param baseActivity
     */
    fun deleteBeacon(baseActivity: AppCompatActivity?, sn: String) {
        OkGo.delete<String>(NetUtil().deleteBeacon() + sn)
            .tag(baseActivity)
            .execute(object : StringDialogCallback(baseActivity,true) {
                override fun onSuccess(response: Response<String>) {
                    val mResponse = Gson().fromJson(response.body().toString(), OkResponse::class.java)
                    val event = HttpEvent()
                    event.code = mResponse.code
                    event.data = mResponse.data
                    event.msg = mResponse.msg
                    event.requestCode = REQUEST_CODE_DELETE_BEACON
                    EventBus.getDefault().post(event)
                }

                override fun onError(response: Response<String>) {
                    super.onError(response)
                    ToastUtils.showLong("删除信标信息失败")
                }
            })
    }


    companion object {
        val REQUEST_CODE_GET_BEACON = 1
        val REQUEST_CODE_UPDATE_BEACON = 2
        val REQUEST_CODE_ADD_BEACON = 3
        val REQUEST_CODE_GET_BEACON_LIST = 4
        val REQUEST_CODE_DELETE_BEACON = 5
    }
}