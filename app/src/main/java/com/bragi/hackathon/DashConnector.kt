package com.bragi.hackathon

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.bragi.dash.sdk.SdkManager
import com.bragi.dash.sdk.init.InitializedInfo
import com.bragi.dash.sdk.init.SdkInitCallback
import com.bragi.dash.sdk.init.auth.ApiKey
import com.bragi.dash.sdk.listener.AccelerometerListener
import com.bragi.dash.sdk.listener.ConnectionStatusListener
import com.bragi.dash.sdk.listener.HeartRateListener
import com.bragi.dash.sdk.model.Connectivity
import com.bragi.dash.sdk.model.HeartRate
import com.bragi.dash.sdk.model.RawMotionData
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
object DashConnector : SdkInitCallback, ConnectionStatusListener() {

    private val device = SdkManager.bragiDevice
    private val pubNubWriter = PubNubWriter()

    fun lateInit(context: Context) {
        if (!SdkManager.isInitialized) {
            SdkManager.init(
                    context,
                    MobileSdkConfig.APP_ID,
                    ApiKey(MobileSdkConfig.API_KEY),
                    DashConnector
            )
        }
    }

    override fun onSdkInitSuccess(p0: InitializedInfo.Success?) {
        Timber.d("sdk init success")
        DashChannel.setSdkState(DashChannel.SdkState.INITIALIZED)
    }

    fun connect(context: Context) {
        SdkManager.connectDevice(
                PendingIntent.getActivity(
                        context, 0x0,
                        Intent(context, MainActivity::class.java),
                        PendingIntent.FLAG_CANCEL_CURRENT),
                this
        )
    }

    fun readAcc() {
        if (device.deliversData) {
            device.registerForAccelerometer(object: AccelerometerListener() {
                override fun onNewAccelerometerReading(accelerometerReading: RawMotionData) {
                    pubNubWriter.write(
                            "[acc&${accelerometerReading.x}&${accelerometerReading.y}&${accelerometerReading.z}]"
                    )
                }
            })
        }
        else {
            Timber.e("no data")
        }
    }

    fun readHr() {
        if (device.deliversData) {
            device.registerForHeartRate(object: HeartRateListener() {
                override fun onNewHeartRateValue(heartRate: HeartRate) {
                    pubNubWriter.write(
                            "[hr&${heartRate.value}]"
                    )
                }

            })
        }
        else {
            Timber.e("no data")
        }
    }

    override fun onSdkInitError(p0: InitializedInfo.Error?) {
        Timber.d("sdk init error: $p0")
    }

    override fun onNewConnectionStatus(status: Connectivity) {
        val state = when (status.status) {
            Connectivity.Status.SUCCESS -> DashChannel.ConnectionState.CONNECTED
            Connectivity.Status.CONNECTION_LOST -> DashChannel.ConnectionState.CONNECTION_LOST
            Connectivity.Status.NO_DEVICE -> DashChannel.ConnectionState.NO_DEVICE
        }
        DashChannel.setConnectionState(state)
    }

}