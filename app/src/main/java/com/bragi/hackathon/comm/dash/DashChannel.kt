package com.bragi.hackathon.comm.dash

import io.reactivex.subjects.PublishSubject

object DashChannel {

    enum class SdkState {
        INITIALIZED,
        NOT_INITIALIZED
    }

    enum class ConnectionState {
        CONNECTED,
        NO_DEVICE,
        CONNECTION_LOST
    }

    private val sdkStateSubject = PublishSubject.create<SdkState>()
    private val connectionStateSubject = PublishSubject.create<ConnectionState>()

    init {
        sdkStateSubject.onNext(SdkState.NOT_INITIALIZED)
        connectionStateSubject.onNext(ConnectionState.NO_DEVICE)
    }

    val sdkStateSubjectObservable = sdkStateSubject.hide()
    val connectionStateObservable = connectionStateSubject.hide()

    fun setSdkState(state: SdkState) {
        sdkStateSubject.onNext(state)
    }

    fun setConnectionState(state: ConnectionState) {
        connectionStateSubject.onNext(state)
    }

}