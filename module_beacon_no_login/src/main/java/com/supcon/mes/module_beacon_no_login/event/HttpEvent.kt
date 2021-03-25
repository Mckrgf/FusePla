package com.supcon.mes.module_beacon_no_login.event

class HttpEvent : BaseEvent() {
    var requestCode = -1
    var data: Any? = null
    var code : Int = -1
    var msg : String = ""
}