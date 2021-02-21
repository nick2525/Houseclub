package me.grishka.houseclub.api.methods

import me.grishka.houseclub.api.ClubhouseAPIRequest
import me.grishka.houseclub.api.model.Event

class GetEvent(id: String) : ClubhouseAPIRequest<GetEvent.Response>("POST", "get_event", Response::class.java) {
    private class Body(var eventHashid: String)
    class Response {
        var event: Event? = null
    }

    init {
        requestBody = Body(id)
    }
}