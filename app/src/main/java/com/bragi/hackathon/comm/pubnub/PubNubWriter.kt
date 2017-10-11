package com.bragi.hackathon.comm.pubnub

import android.util.Log
import com.bragi.hackathon.comm.RemoteWriter
import com.pubnub.api.callbacks.PNCallback
import com.pubnub.api.models.consumer.PNPublishResult
import com.pubnub.api.models.consumer.PNStatus

class PubNubWriter : RemoteWriter {

    private val pubNub = PubNubInstance.pubNub

    override fun write(content: String) {
        pubNub.publish()
                .message(content)
                .channel(PubNubConfig.CHANNEL_ID)
                .async(object : PNCallback<PNPublishResult>() {
                    override fun onResponse(result: PNPublishResult, status: PNStatus) {
                        Log.w(javaClass.simpleName, "$result")
                    }
                })
    }

}