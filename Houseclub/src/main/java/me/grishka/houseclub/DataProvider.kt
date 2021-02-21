package me.grishka.houseclub

import me.grishka.houseclub.api.model.Channel

object DataProvider {
    private var channelCache: Channel? = null
    fun getChannel(id: String?) = when (channelCache?.channel) {
        id -> channelCache
        else -> null
    }

    fun saveChannel(channel: Channel?) {
        channelCache = channel
    }
}