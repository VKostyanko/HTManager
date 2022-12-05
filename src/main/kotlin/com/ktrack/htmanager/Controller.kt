package com.ktrack.htmanager

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class Controller {
    @GetMapping("/")
    fun test() = "Hello world :("

    @PostMapping("/")
    fun hostTrackerPostback(
        @RequestBody hostTrackerPostback: HostTrackerPostback
    ) {
        println(hostTrackerPostback)

        if (hostTrackerPostback.isUp)
            println("Up")
//            onPostbackUp(hostTrackerPostback.taskUrl)
        else
            println("Down")
//            onPostbackDown(hostTrackerPostback.taskUrl)
    }
}

data class HostTrackerPostback(
    @JsonProperty("task_url") val taskUrl: String,
    @JsonProperty("is_up") val isUp: Boolean
    //todo add name with internalAppId
)