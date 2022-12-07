package com.ktrack.htmanager

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.jvm.Throws

interface HostTrackerTaskListener {
    /**
     * Я буду сохранять всё шо сюда прииходит в свою базу и связывать это с
     * Приложением - шоб можно было смотреть логи
     */
    fun onStateChanged(hostTracerTask: HostTrackerTask )
}

@Component
class HostTrackerTaskListenerImpl : HostTrackerTaskListener {
    override fun onStateChanged(hostTracerTask: HostTrackerTask) {
        println(hostTracerTask)
    }

}

@Component
class HostTracker @Autowired constructor(
    val hostTrackerTaskListener: HostTrackerTaskListener
) {
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

    @Throws(Exception::class)
    fun getTask(appId: Long) = getHttpTask(appId)


    fun updateTask(
        appId: Long,
        waitingFor: TaskStatus,

        packageName: String? = null,
        huawei_id: String? = null,
        keyword: String? = null,
        enable: Boolean? = null
    ) = isSuccess {
        onAppUpdate(appId, waitingFor, packageName, huawei_id, keyword, enable)
    }

    fun deleteTask(appId: Long) = isSuccess {
        onAppDelete(appId)
    }

    @Throws(Exception::class)
    fun getTasksList() = getHttpTasksList()
}

data class HostTrackerTask(
    val internalId: Long,
    val taskStatus: TaskStatus
)

enum class TaskStatus {
    Down, Up
}
