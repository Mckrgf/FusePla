package com.supcon.mes.module_beacon.bean

import java.io.Serializable

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:28
 * @desc   :
 */
class BLEDevice : Serializable{
    var deviceName: String = ""
    var deviceAddress: String = ""
    var uuid:String = ""
    var rssi: Int = 0
}