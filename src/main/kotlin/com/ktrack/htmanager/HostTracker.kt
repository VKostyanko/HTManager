package com.ktrack.htmanager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface HowsTrackerTaskListener {
    /**
     * Я буду сохранять всё шо сюда прииходит в свою базу и связывать это с
     * Приложением - шоб можно было смотреть логи
     */
    fun onStateChanged(/*todo state internalAppID & UP or DOWN status*/)
}


@Component
class HostTracker @Autowired constructor(
    HowsTrackerTaskListener : HowsTrackerTaskListener
){

    //getTasksList
    //CRUD task (in update "watch up" or switch "watch down")


//    data class Task(
//      internalId
//      packageNme
//      huaeiAppId
    //  lookingFor -> UP/DOWN
//    )

    //fun createTask(Task): Bool -> isSuccess



}