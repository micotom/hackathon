package com.bragi.hackathon

import android.app.Application
import com.bragi.hackathon.comm.dash.DashConnector
import timber.log.Timber



class HackathonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        DashConnector.lateInit(this)
    }


}