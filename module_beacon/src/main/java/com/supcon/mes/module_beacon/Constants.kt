package com.supcon.mes.module_beacon

/**
 * @author : yaobing
 * @date   : 2020/7/22 10:23
 * @desc   : 公共参数管理区
 */

const val BLE_SELECTOR = ""                    //蓝牙地址筛选条件
const val SCAN_PERIOD: Long = 16 * 1000                 //BLE单次搜索时间
const val REQUEST_ENABLE_BT = 1                     //蓝牙权限请求码
const val REQUEST_GET_COORDINATE = 2                     //GPS坐标请求码
const val RESPONSE_GET_COORDINATE = 3                     //GPS坐标相应码
const val AMEND_RSSI = 0                          //蓝牙rssi修正参数

const val KEY_IP = "KEY_IP"                         //SP的IP地址的key
const val KEY_PORT = "KEY_PORT"                     //SP的PORT的key
const val COORDINATE_KEY = "coordinate"                     //SP的PORT的key
const val GPS_ADDRESS = "http://192.168.90.127:8080/msService/SESGISConfig/themeConfig/mobileMapSelect/index?type=point&mode=edit"
const val TEST_WEB_ADDRESS_00 = "file:///android_asset/web00.html"
const val TEST_WEB_ADDRESS_01 = "file:///android_asset/web01.html"

const val DEFAULT_MILLISECONDS: Long = 15000        //Okgo默认的超时时间

const val COUNT_EMPTY_RSSI_LIST = 2                 //默认需要填写的rssi强度列表的数量

const val MATCH_DEVICE_N = "MATCH_DEVICE_N"         //sp存设备信息





