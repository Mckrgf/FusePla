package com.supcon.mes.module_beacon_no_login.rssiCorrection

import com.supcon.mes.module_beacon_no_login.BasePresenter
import com.supcon.mes.module_beacon_no_login.BaseView

interface RssiCorrectionContract {
    interface View : BaseView<Presenter> {
        fun updateAScanResult()
        fun updateScanState()
    }

    interface Presenter : BasePresenter {
        fun scanDevice(enable : Boolean)
        fun result(requestCode: Int, responseCode: Int)
        fun getBeacon(sn: String)
        fun addBeacon(beaconJson:String)
        fun updateBeacon(beaconJson:String)
    }


}