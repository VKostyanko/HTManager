package com.ktrack.htmanager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface TrackerTaskListener {
    /**
     * Я буду сохранять всё шо сюда прииходит в свою базу и связывать жто с
     * Приложением - шоб можно было смотреть логи
     */
    fun onStateChanged(/*todo state internalAppID & UP or DOWN status*/)
}

@Component
class TrackerTaskListenerImpl : TrackerTaskListener {
    override fun onStateChanged() {

    }

}

@Component
class HostTracker @Autowired constructor(
    val TrackerTaskListener: TrackerTaskListener
) {

    //getTasksList
    //CRUD task (in update "watch up" or switch "watch down")
    fun createTask(
        appId: Long,
        packageName: String,
        huaweiAppId: String,
        versionName: String,
        waitingState: WaitingFor = WaitingFor.Up
        ) {
        onAppCreate(
            packageName = packageName,
            huawei_id = huaweiAppId,
            keyword = versionName,
            appId = appId,
            waitingFor = waitingState
        )
    }


//    data class Task(
//      internalId
//      packageNme
//      huaeiAppId
    //  lookingFor -> UP/DOWN
//    )

    //fun createTask(Task): Bool -> isSuccess

}

data class HostTracerTask(
    val internalId: Long,
    val packageName: String,
    val huaweiAppId: String,
    val waitingState: WaitingFor
)

enum class WaitingFor {
    Down, Up
}
