package com.supcon.supbeacon.webviewTest

import android.util.Log
import android.webkit.JavascriptInterface

/**
 * @author : yaobing
 * @date   : 2020/8/10 14:05
 * @desc   :
 */
class AndroidToJS : Object() {
    @JavascriptInterface
    fun mobilejs(aaa:String) {
        Log.d("zxcv",aaa)
    }
}