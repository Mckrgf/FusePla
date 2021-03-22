package com.supcon.testlibrary

/**
 * @author : yaobing
 * @date : 2020/9/22 13:44
 * @desc :
 */
object Aaa {
    @JvmStatic
    fun main(args: Array<String>) {

        //p0：用于计算一米强度校正后的正值
        val a = 127
        val p0_a = -68
        val p0 = a + p0_a

        //当前信号强度
        val rssi = -68

        //噪声
        val n = 1

        //距离
        val aa = Math.pow(10.0, (Math.abs(rssi - 127) - Math.abs(p0 - 127)) / (10 * n).toDouble())
        val dis_int = aa.toInt()
        println("距离" + dis_int)
    }
}