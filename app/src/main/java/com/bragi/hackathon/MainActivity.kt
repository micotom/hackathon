package com.bragi.hackathon

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bragi.hackathon.comm.dash.DashChannel
import com.bragi.hackathon.comm.dash.DashConnector
import com.bragi.hackathon.comm.dash.DataChannel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DashConnector.lateInit(this)
        scheduleSubscriptions()
    }

    private fun scheduleSubscriptions() {
        subscriptions.addAll(
                subscribeForSdkState(),
                subscribeForDashConnectionState(),
                subscribeForNetChannel()
        )
    }

    private fun subscribeForNetChannel(): Disposable? {
        return DataChannel.incomingMessageObservable.subscribe {
            data_text.text = it
        }
    }

    private fun subscribeForDashConnectionState(): Disposable? {
        return DashChannel.connectionStateObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            onNewConnectionState(it)
        }
    }

    private fun subscribeForSdkState(): Disposable? {
        return DashChannel.sdkStateSubjectObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            onNewSdkState(it)
        }
    }

    override fun onDestroy() {
        subscriptions.dispose()
        super.onDestroy()
    }

    private fun onNewConnectionState(it: DashChannel.ConnectionState?) {
        when (it) {
            DashChannel.ConnectionState.CONNECTED -> {
                dash_connection_status_text.text = getString(R.string.connected)
                DashConnector.readAcc()
                DashConnector.readHr()
                DashConnector.readHeadGestures()
            }
            DashChannel.ConnectionState.CONNECTION_LOST -> {
                dash_connection_status_text.text = getString(R.string.connection_lost)
            }
            DashChannel.ConnectionState.NO_DEVICE -> {
                dash_connection_status_text.text = getString(R.string.no_device)
            }
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
