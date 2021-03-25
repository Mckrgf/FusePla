package com.supcon.supbeacon.utils

import android.os.Build
import com.supcon.supbeacon.bean.MyBluetoothDevice
import java.util.*

/**
 * @author : yaobing
 * @date : 2020/12/30 10:27
 * @desc : 搜索、排序等计算方法
 */
object CalculateUtil {

    fun sortBLEDevice(list: MutableList<MyBluetoothDevice>): MutableList<MyBluetoothDevice> {
            list!!.sortWith(Comparator { o1, o2 ->
                var date1 = 0
                var date2 = 0
                if (o1 is MyBluetoothDevice) {
                    val item: MyBluetoothDevice = o1
                    date1 = item.rssi
                } else {
                    val item: MyBluetoothDevice = o1 as MyBluetoothDevice
                    date1 = item.rssi
                }
                if (o2 is MyBluetoothDevice) {
                    val item: MyBluetoothDevice = o2 as MyBluetoothDevice
                    date2 = item.rssi
                } else {
                    val item: MyBluetoothDevice = o2 as MyBluetoothDevice
                    date2 = item.rssi
                }

                if (date1 >= date2) {
                    -1
                } else {
                    1
                }
            })

        return list
    }
}