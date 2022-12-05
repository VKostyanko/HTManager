package com.ktrack.htmanager

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class Controller @Autowired constructor(
    var hostTrackerTaskListener:HostTrackerTaskListener
){

    @GetMapping("/")
    fun test() = "Hello world :("

    @PostMapping("/")
    fun hostTrackerPostback(
        @RequestBody hostTrackerPostback: HostTrackerPostback
    ) {
        println(hostTrackerPostback)

        hostTrackerTaskListener.onStateChanged(
            HostTrackerTask(
                internalId = hostTrackerPostback.internalAppId.toLong(),
                taskStatus = if (hostTrackerPostback.isUp) TaskStatus.Up else TaskStatus.Down
            )
        )
    }
}

data class HostTrackerPostback(
    @JsonProperty("internal_app_id") val internalAppId: String,
    @JsonProperty("is_up") val isUp: Boolean,
    @JsonProperty("internal_error") val internalError:String
)