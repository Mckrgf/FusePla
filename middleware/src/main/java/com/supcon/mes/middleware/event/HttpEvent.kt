package com.supcon.mes.middleware.event

import com.supcon.mes.middleware.event.Base1Event

class HttpEvent : Base1Event() {
    var requestCode = -1
    var data: Any? = null
    var code : Int = -1
    var msg : String = ""
}