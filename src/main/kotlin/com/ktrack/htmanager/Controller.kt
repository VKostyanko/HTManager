package com.ktrack.htmanager

import org.springframework.boot.jackson.JsonComponent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class Controller {
    @GetMapping("/")
    fun test(): String {
        return "Hello world :("
    }

    @PostMapping("/")
    fun hostTrackerPostback(
        @RequestBody hostTrackerPostback: HostTrackerPostback
    ): String {
        println(hostTrackerPostback)

        if (hostTrackerPostback.isUp)
            onPostbackUp(hostTrackerPostback.taskUrl)
        else{}
            //todo: onPostbackDown   (disable app)


        return hostTrackerPostback.taskUrl
    }
}

data class HostTrackerPostback(
    val taskUrl: String,
    val isUp: Boolean
)