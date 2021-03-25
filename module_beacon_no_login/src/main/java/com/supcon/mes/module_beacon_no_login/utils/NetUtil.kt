package com.supcon.mes.module_beacon_no_login.utils

import android.text.TextUtils
import com.blankj.utilcode.util.SPUtils
import com.supcon.mes.module_beacon_no_login.KEY_IP
import com.supcon.mes.module_beacon_no_login.KEY_PORT

/**
 * @author : yaobing
 * @date   : 2020/8/4 18:43
 * @desc   : 管理IP地址/端口号
 */
open class NetUtil {

    open fun isFirstOpen(): Boolean {
        val ip = SPUtils.getInstance().getString(KEY_IP)
        val port = SPUtils.getInstance().getString(KEY_PORT)
        return !(!TextUtils.isEmpty(ip) && !TextUtils.isEmpty(port))
    }

    open fun save(ip: String, port: String) {
        SPUtils.getInstance().put(KEY_IP, ip)
        SPUtils.getInstance().put(KEY_PORT, port)
    }

    open fun getIP(): String {
        return SPUtils.getInstance().getString(KEY_IP)
    }

    open fun getPort(): String {
        return SPUtils.getInstance().getString(KEY_PORT)
    }

    open fun getBaseIp() :String {
        return "http://"  + getIP() + ":" + getPort() + "/" + "api/"
    }
    open fun getBaseIp3000() :String {
        return "http://"  + getIP() + ":" + getPort() + "/"
    }
    open fun getBeacon() :String {
        //http://192.168.95.164:8080/msService/public/VxPLS/beacon/detail/asdfwr1
        val aa = getBaseIp() + "beacon/"
        return aa
    }
    open fun addBeacon() :String {
        //http://192.168.95.164:8080/msService/public/VxPLS/beacon/detail/asdfwr1
        val aa = getBaseIp() + "beacon/"
        return aa
    }

    open fun getBeaconList(): String {
        return getBaseIp() + "beacon?currentPage=1&pageSize=1300"
    }
    open fun updateBeacon() :String {
        return getBaseIp() + "beacon/"
    }
    open fun deleteBeacon(): String {
        return getBaseIp() + "beacon/"
    }
}