package com.supcon.mes.module_beacon.rssiCorrection

import com.supcon.mes.module_beacon.BasePresenter
import com.supcon.mes.module_beacon.BaseView

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