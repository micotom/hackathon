package com.bragi.hackathon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bragi.dash.sdk.SdkManager
import com.bragi.dash.sdk.init.InitializedInfo
import com.bragi.dash.sdk.init.SdkInitCallback
import com.bragi.dash.sdk.init.auth.ApiKey
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val writer = PubNubWriter()

    private val dash = SdkManager.bragiDevice

    private val sdkManagerCallback = object: SdkInitCallback {
        override fun onSdkInitSuccess(p0: InitializedInfo.Success?) {
            Timber.d("sdk init success")
        }
        override fun onSdkInitError(p0: InitializedInfo.Error?) {
            Timber.d("sdk init error: $p0")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dash_connection_status_text.text = "Disconnected"

        if (!SdkManager.isInitialized) {
            Timber.d("init sdk")
            SdkManager.init(
                    baseContext,
                    MobileSdkConfig.APP_ID,
                    ApiKey(MobileSdkConfig.API_KEY),
                    sdkManagerCallback
            )
        }

        send_button.setOnClickListener {
            writer.write("tächno!")
        }

        ChannelState.incomingMessageObservable.subscribe {
            data_text.text = it
        }
    }

}
