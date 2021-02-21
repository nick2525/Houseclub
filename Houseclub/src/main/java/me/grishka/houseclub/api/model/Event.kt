package me.grishka.houseclub.api.model

import java.util.Date

class Event(
    var channel: String? = null,
    var isExpired:Boolean = false,
    var timeStart: Date? = null,
    var description: String? = null,
    var name: String? = null,
    var eventId:Int = 0,
    var isMemberOnly:Boolean = false,
    var hosts: List<FullUser>? = null,
    var clubIsMember:Boolean = false,
    var clubIsFollower:Boolean = false
)