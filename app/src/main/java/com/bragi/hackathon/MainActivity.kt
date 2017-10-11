package com.bragi.hackathon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DashConnector.lateInit(this)

        connect_button.isEnabled = false
        DashChannel.sdkStateSubjectObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == DashChannel.SdkState.INITIALIZED) {
                        connect_button.isEnabled = true
                        connect_button.setOnClickListener {
                            DashConnector.connect(this)
                        }
                    }
                    else {
                        connect_button.isEnabled = false
                    }
                }

        DashChannel.connectionStateObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it) {
                DashChannel.ConnectionState.CONNECTED -> {
                    dash_connection_status_text.text = "Connected"
                    DashConnector.readAcc()
                    DashConnector.readHr()
                }
                else -> {} // ignore
            }
        }

        DataChannel.incomingMessageObservable.subscribe {
            data_text.text = it
        }
    }

}
