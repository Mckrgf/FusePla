package com.supcon.mes.module_beacon_no_login.bean

import android.bluetooth.BluetoothDevice

/**
 * @author : yaobing
 * @date   : 2020/12/1 15:22
 * @desc   :
 */
class MyBluetoothDevice {
    lateinit var device:BluetoothDevice
    var rssi:Int = 0
}