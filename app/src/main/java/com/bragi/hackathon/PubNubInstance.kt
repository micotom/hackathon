package com.bragi.hackathon

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import java.util.*

object PubNubInstance {

    val pubNub: PubNub

    init {
        val config = PNConfiguration().apply {
            subscribeKey = PubNubConfig.SUBSCRIBE_KEY
            publishKey = PubNubConfig.PUBLISH_KEY
        }
        pubNub = PubNub(config)
        pubNub.addListener(PubNubReader())
        pubNub.subscribe().channels(Arrays.asList(PubNubConfig.CHANNEL_ID)).execute();
    }

}