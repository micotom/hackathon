package com.bragi.hackathon

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

object ChannelState {

    private val dataInSubject = PublishSubject.create<String>()

    val incomingMessageObservable = dataInSubject.hide().observeOn(AndroidSchedulers.mainThread())

    fun writeIncomingMessage(content: String) {
        dataInSubject.onNext(content)
    }

}