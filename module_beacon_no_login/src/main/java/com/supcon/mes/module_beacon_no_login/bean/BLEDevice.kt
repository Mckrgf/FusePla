package com.supcon.mes.module_beacon_no_login.bean

import android.bluetooth.BluetoothDevice

/**
 * @author : yaobing
 * @date   : 2020/8/3 15:28
 * @desc   :
 */
class BLEDevice{
    var deviceName: String = ""
    var deviceAddress: String = ""
    var uuid:String = ""
    var rssi: Int = 0
}