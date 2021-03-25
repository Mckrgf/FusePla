package com.supcon.supbeacon.rssiCorrection

import com.supcon.supbeacon.BasePresenter
import com.supcon.supbeacon.BaseView

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