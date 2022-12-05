package com.ktrack.htmanager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface HostTrackerTaskListener {
    /**
     * Я буду сохранять всё шо сюда прииходит в свою базу и связывать это с
     * Приложением - шоб можно было смотреть логи
     */
    fun onStateChanged(HostTracerTask: HostTrackerTask )
}

@Component
class HostTrackerTaskListenerImpl : HostTrackerTaskListener {
    override fun onStateChanged(hostTracerTask: HostTrackerTask) {
        println(hostTracerTask)
    }

}

@Component
class HostTracker @Autowired constructor(
    val HostTrackerTaskListener: HostTrackerTaskListener
) {

    //TODO getTasksList
    //CRUD task (in update "watch up" or switch "watch down")
    //TODO update disable
    fun createTask(
        appId: Long,
        packageName: String,
        huaweiAppId: String,
        versionName: String,
        waitingFor: TaskStatus = TaskStatus.Up
    ): Boolean = isSuccess {
        onAppCreate(
            packageName = packageName,
            huawei_id = huaweiAppId,
            keyword = versionName,
            appId = appId,
            waitingFor = waitingFor
        )
    }


    fun getTaskByAppId(appId: Long) = getHttpTaskByAppId(appId)


    fun updateTask(
        appId: Long,
        waitingFor: TaskStatus,

        packageName: String? = null,
        huawei_id: String? = null,
        keyword: String? = null,
    ) = isSuccess {
        onAppUpdate(appId, waitingFor, packageName, huawei_id, keyword)
    }

    fun deleteTask(appId: Long) = isSuccess {
        onAppDelete(appId)
    }




//    data class Task(
//      internalId
//      packageNme
//      huaeiAppId
    //  lookingFor -> UP/DOWN
//    )

    //fun createTask(Task): Bool -> isSuccess

}

data class HostTrackerTask(
    val internalId: Long,
    val taskStatus: TaskStatus
)

enum class TaskStatus {
    Down, Up
}
