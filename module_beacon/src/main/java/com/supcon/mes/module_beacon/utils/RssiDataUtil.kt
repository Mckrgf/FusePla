package com.supcon.mes.module_beacon.utils

import android.bluetooth.BluetoothDevice
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supcon.mes.module_beacon.AMEND_RSSI
import com.supcon.mes.module_beacon.COUNT_EMPTY_RSSI_LIST
import com.supcon.mes.module_beacon.bean.RssiData
import org.json.JSONObject


/**
 * @author : yaobing
 * @date   : 2020/8/11 20:16
 * @desc   : 单例模式，私有化构造函数。负责保存信标rssi强度以及距离的相关数据
 */
class RssiDataUtil private constructor() {
    //用户需要添加的不同距离的信标rssi
    val mRssiDataList: ArrayList<RssiData> = ArrayList()

    //收集到的信标rssi数据
    val rssiCollector = HashMap<String, ArrayList<Int>>()

    //RSSI平均值
    var averageRssi = 0

    /**
     * 生成空数据（强制第一个为1米基准）
     */
    fun createEmptyRssiList(): ArrayList<RssiData> {
        mRssiDataList.clear()
        for (index in 0 until COUNT_EMPTY_RSSI_LIST) {
            if (index == 0) {
                val rssiData = RssiData()
                rssiData.distance = 1f
                rssiData.editable = false
                mRssiDataList.add(rssiData)
            } else {
                mRssiDataList.add(RssiData())
            }
        }
        return mRssiDataList
    }

    var templateDataList : ArrayList<Int> = ArrayList()

    /**
     * 生成空数据（强制第一个为1米基准）
     */
    fun createTemplateRssiList(): ArrayList<RssiData> {
        val aa= 6
        val rssi0 = -45
        mRssiDataList.clear()
        for (index in 0 until getTemplate().size()) {
            val rssiData = RssiData()
            rssiData.distance = getTemplate()[index].asFloat
            rssiData.editable = true
            rssiData.rssi = (rssi0 - (6 * index)).toFloat()
            mRssiDataList.add(rssiData)
        }
        return mRssiDataList
    }

    /**
     * 生成模拟数据
     */
    fun createMockRssiList(): ArrayList<RssiData> {
        mRssiDataList.clear()
        for (index in 0 until COUNT_EMPTY_RSSI_LIST) {
            val rssiData = RssiData()
            rssiData.distance = index.toFloat() + 1
            rssiData.editable = true
            rssiData.rssi = (-50 - (5 * index)).toFloat()
            mRssiDataList.add(rssiData)
        }
        return mRssiDataList
    }

    /**
     * 判断是否全部数据都填写完成
     */
    fun allDataPrepared(): Boolean {
        //老版本检测五组数据是否全部填写
        //新版本检测1米强度即可
        for (index in 0 until mRssiDataList.size) {
            if (mRssiDataList[index].rssi != 0f && mRssiDataList[index].distance == 1f) {
                return true
            }
        }
        return false
    }

    fun mockSPData() {
        val mapValue = JSONObject()
        mapValue.put("mac", "80:6F:B0:AD:A8:98")
        mapValue.put("n", "2.1")
        mapValue.put("floor", "2")
        mapValue.put("x", "11")
        mapValue.put("y", "22")
        mapValue.put("z", "33")

        val mapValue1 = JSONObject()
        mapValue1.put("mac", "80:6F:B0:AD:A6:F0")
        mapValue1.put("n", "2.1")
        mapValue1.put("floor", "2")
        mapValue1.put("x", "11")
        mapValue1.put("y", "22")
        mapValue1.put("z", null)

        SPUtils.getInstance().put("80:6F:B0:AD:A8:98", mapValue.toString())
        SPUtils.getInstance().put("80:6F:B0:AD:A6:F0", mapValue1.toString())

        var dd = SPUtils.getInstance().getString("80:6F:B0:AD:A8:98")
        var ddd = SPUtils.getInstance().getString("80:6F:B0:AD:A6:F0")
        val returnData: JsonObject = JsonParser().parse(dd).asJsonObject
        val returnData1: JsonObject = JsonParser().parse(ddd).asJsonObject
    }

    /**
     * 根据设备地址获取该设备的全部rssi信息
     * 获取最后四个rssi的值
     */
    fun getAverageRssiByDeviceSuccessfully(device: BluetoothDevice): Boolean {
        val rssiList = rssiCollector[device.address]
        Log.d(TAG, "$device 信标信号数量：${rssiList?.size}")
        return if (rssiList?.size!! > 10) {
            val averageRssiList = rssiList.subList(rssiList.size - 4, rssiList.size)
            var sumRssi = 0
            for (index in 0 until averageRssiList.size) {
                sumRssi += averageRssiList[index]
            }
            averageRssi = sumRssi / averageRssiList.size
            true
        } else {
            false
        }
    }

    fun getP0(): Float {
        for (index in 0 until mRssiDataList.size) {
            if (mRssiDataList[index].distance == 1f) {
                return mRssiDataList[index].rssi + 127
            }
        }
        return -1f
    }

    /**
     * 根据扫描结果进行收集数据
     */
    fun collectRssi(device: BluetoothDevice, rssi: Int) {
        if (null != rssiCollector[device.address]) {
            val rssiList: ArrayList<Int>? = rssiCollector[device.address]
            rssiList!!.add(rssi + AMEND_RSSI)
            rssiCollector[device.address] = rssiList
        } else {
            rssiCollector[device.address] = ArrayList()
        }
    }

    fun getN(): Double {
        return 2.0
    }

    fun getJsonForUpdateBeacon(n: String, p0: Float, floor: String, lat: Double?, lon: Double?, hei: Double?): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("n", n)
        jsonObject.put("p0", p0)
        jsonObject.put("floor", floor)
        jsonObject.put("x", lat)
        jsonObject.put("y", lon)
        jsonObject.put("z", hei)
        return jsonObject
    }

    fun getJsonForAddBeacon(n: String, p0: Float, floor: String, lat: Double?, lon: Double?, hei: Double?,sn:String,mac:String): JSONObject {
        val jsonObject = JSONObject()

        //后台接口不允许以下三项为空
        jsonObject.put("battery", 0)
        jsonObject.put("createdAt", System.currentTimeMillis())
        jsonObject.put("updatedAt", System.currentTimeMillis() + 1)

        //以下四项后台说不用管
        jsonObject.put("distance", "")
        jsonObject.put("factoryMarkId", "")
        jsonObject.put("id", "")
        jsonObject.put("rssi", "")

        jsonObject.put("n", n)
        jsonObject.put("p0", p0)
        jsonObject.put("floor", floor)
        jsonObject.put("x", lat)
        jsonObject.put("y", lon)
        jsonObject.put("z", hei)
        jsonObject.put("sn", sn)
        jsonObject.put("mac", mac)
        return jsonObject
    }

    fun getTemplate() : JsonArray {
        var dataS = SPUtils.getInstance().getString("templeData")
        if (TextUtils.isEmpty(dataS)) {
            val dataSArray = JsonArray()
            dataSArray.add(1)
            dataSArray.add(1.5)
            dataSArray.add(2)
            dataSArray.add(2.5)
            dataSArray.add(3)
            dataS = dataSArray.toString()
            SPUtils.getInstance().put("templeData",dataS)
            return dataSArray
        }
        return Gson().fromJson(dataS,JsonArray::class.java)
    }


    fun updateRssi(mCurrentPosition: Int, rssis1: Double) {
        mRssiDataList[mCurrentPosition].rssi = rssis1.toFloat()
    }

    fun setTemplate(data:String) {

        SPUtils.getInstance().put("templeData",data)
    }

    fun getDistance(pi_rssi:Double) : Double {


        //功率1
        val a = 127.0

        val rssi_a = pi_rssi
        val rssi = a + rssi_a

        val n = 2.0

        val rawDistance = Math.pow(10.0, (Math.abs(rssi - 127) - Math.abs(getP0() - 127)) / (10 * n))
        return rawDistance
    }

    companion object {
        private var instance: RssiDataUtil? = null
            get() {
                if (field == null) {
                    field = RssiDataUtil()
                }
                return field
            }

        fun get(): RssiDataUtil {
            return instance!!
        }

        private const val TAG = "RssiDataUtil"
    }
}