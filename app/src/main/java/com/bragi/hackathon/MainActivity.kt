package com.bragi.hackathon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bragi.hackathon.comm.dash.DashChannel
import com.bragi.hackathon.comm.dash.DashConnector
import com.bragi.hackathon.comm.dash.DataChannel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DashConnector.lateInit(this)

        connect_button.isEnabled = false
        DashChannel.sdkStateSubjectObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            onNewSdkState(it)
        }

        DashChannel.connectionStateObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            onNewConnectionState(it)
        }

        DataChannel.incomingMessageObservable.subscribe {
            data_text.text = it
        }
    }

    private fun onNewConnectionState(it: DashChannel.ConnectionState?) {
        when (it) {
            DashChannel.ConnectionState.CONNECTED -> {
                dash_connection_status_text.text = getString(R.string.connected)
                DashConnector.readAcc()
                DashConnector.readHr()
            }
            else -> {
            } // ignore
        }
    }

    private fun onNewSdkState(it: DashChannel.SdkState?) {
        if (it == DashChannel.SdkState.INITIALIZED) {
            connect_button.isEnabled = true
            connect_button.setOnClickListener {
                DashConnector.connect(this)
            }
        } else {
            connect_button.isEnabled = false
        }
    }

}
