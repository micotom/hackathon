package com.bragi.hackathon

import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

class PubNubReader : RemoteReader, SubscribeCallback() {

    private val pubNub = PubNubInstance.pubNub

    init {
        pubNub.addListener(this)
    }

    override fun onDataRead(content: String) {
        DataChannel.writeIncomingMessage(content)
    }

    override fun status(pubnub: PubNub?, status: PNStatus?) {
    }

    override fun presence(pubnub: PubNub?, presence: PNPresenceEventResult?) {
    }

    override fun message(pubnub: PubNub?, message: PNMessageResult?) {
        message?.let {
            onDataRead(message.message.toString())
        }
    }

}