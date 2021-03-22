package com.supcon.mes.middleware.util

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.supcon.mes.middleware.KEY_IP
import com.supcon.mes.middleware.KEY_PORT
import com.supcon.mes.middleware.PLAApplication

/**
 * @author : yaobing
 * @date   : 2020/8/4 18:43
 * @desc   : 管理IP地址/端口号
 */
open class NetUtil {

    open fun save(ip: String, port: String) {
        SPUtils.getInstance().put(KEY_IP, ip)
        SPUtils.getInstance().put(KEY_PORT, port)
    }

    open fun getIP(): String {
        return SPUtils.getInstance().getString(KEY_IP,PLAApplication.getIp())
    }

    open fun getPort(): String {
        return SPUtils.getInstance().getString(KEY_PORT,PLAApplication.getPort())
    }

    open fun getBaseIp(): String {
        return "http://" + getIP() + ":" + getPort() + "/"
    }

    open fun getBeacon(): String {
        return getBaseIp() + "msService/public/VxPLS/beacon/detail/"
    }

    open fun addBeacon(): String {
        return getBaseIp() + "msService/public/VxPLS/beacon/add/"
    }

    open fun deleteBeacon(): String {
        return getBaseIp() + "msService/public/VxPLS/beacon/delete/"
    }

    open fun updateBeacon(): String {
        return getBaseIp() + "msService/public/VxPLS/beacon/edit/"
    }


    open fun getBeaconList(): String {
        return getBaseIp() + "msService/public/VxPLS/beacon/list?currentPage=1&pageSize=1300"
    }
    //http://192.168.95.177:8080/msService/SESGISConfig/themeConfig/mapLocation/index?layerCode=envirProLayer&id=SESECOMonitor_1.0.0_spotManage_SpotManage_1000&type=point&displayLabel=测试
    open fun getBeaconMapUrl(id:String,name:String): String {
        return getBaseIp() + "msService/SESGISConfig/themeConfig/mapLocation/index?layerCode=beaconLayer&id=$id&type=point&displayLabel=$name"
    }
    //http://192.168.95.177:8080/msService/SESGISConfig/themeConfig/mapLocation/index?layerCode=envirProLayer&id=SESECOMonitor_1.0.0_spotManage_SpotManage_1000&type=point&displayLabel=测试
    open fun getBeaconMapUrlForCoor(): String {
        return getBaseIp() + "msService/SESGISConfig/themeConfig/mobileMapSelect/index?type=point&mode=edit"
    }

}