package com.supcon.mes.module_beacon_no_login.rssiCorrection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Handler
import com.supcon.mes.module_beacon_no_login.SCAN_PERIOD
import com.supcon.mes.module_beacon_no_login.utils.RssiDataUtil
import com.supcon.supbeacon.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.all_title.*

class RssiCorrectionPresenter(context: RssiCorrectionActivity,iView : RssiCorrectionContract.View) : RssiCorrectionContract.Presenter {
    private var scanTime: Long = 0
    private var mHandler: Handler? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private val mContext = context
    override fun scanDevice(enable:Boolean) {
            if (enable) {
                scanTime = System.currentTimeMillis()
                RssiDataUtil.get().rssiCollector.clear()
                mHandler!!.postDelayed({
//                    mScanning = false
                    mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
//                    bt_ble_status.text = getString(R.string.START_SCAN)

                }, SCAN_PERIOD)
//                mScanning = true
                mBluetoothAdapter!!.startLeScan(mLeScanCallback)
//                bt_ble_status.text = getString(R.string.STOP_SCAN)
            } else {
//                mScanning = false
//                bt_ble_status.text = getString(R.string.START_SCAN)
//                mBluetoothAdapter!!.stopLeScan(mLeScanCallback)
            }
//            invalidateOptionsMenu()

    }

    override fun result(requestCode: Int, responseCode: Int) {

    }

    override fun getBeacon(sn: String) {

    }

    override fun addBeacon(beaconJson: String) {

    }

    override fun updateBeacon(beaconJson: String) {

    }

    override fun start() {
        mHandler = Handler()
        val bluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
//        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createEmptyRssiList())
//        mRssiDataListAdapter.setNewData(RssiDataUtil.get().createMockRssiList())
    }

    private val mLeScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, _ ->
        mContext.runOnUiThread {
//            number_progress_bar.progress = (System.currentTimeMillis() - scanTime).toInt()
//            if (number_progress_bar.progress > 9500) number_progress_bar.progress = 10000
//            RssiDataUtil.get().collectRssi(device, rssi)
//            mDeviceListAdapter.addDevice(device)
//            mDeviceListAdapter.notifyDataSetChanged()
        }
    }
}