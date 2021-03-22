package com.supcon.mes.module_beacon.event

class BleReceiverEvent : BaseEvent() {
    var requestCode = -1
    var data: Int? = null
    var code : Int = -1
    var msg : String = ""
}