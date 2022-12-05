package com.ktrack.htmanager


fun getToken(
    forcibly: Boolean = false
): String = if (
    forcibly ||
    TokenHolder.token.isEmpty() ||
    TokenHolder.expirationUnixTime * 1000 < System.currentTimeMillis()
) {
    val newToken = HostTrackerService.instance
        .getToken().execute().body() ?: throw Exception("getToken error")
    TokenHolder.apply {
        token = newToken.token
        expirationTime = newToken.expirationTime
        expirationUnixTime = newToken.expirationUnixTime
    }
    TokenHolder.token
} else {
    TokenHolder.token
}

fun onAppCreate(
    packageName: String,
    huawei_id: String? = null,
    appId: Long,
    keyword: String,
    waitingFor: WaitingFor
): Task {
    val token = "bearer " + getToken()

    val url = if (huawei_id != null) "https://appgallery.huawei.com/app/$huawei_id"
        else "https://play.google.com/store/apps/details?id=$packageName"

    val alertType = if (waitingFor == WaitingFor.Up) "Up" else "Down"

    val task = Task(
        url = url,
        httpMethod = "Get",
        timeout = 10000,
        interval = 1, //todo: utochnit'
        enabled = true,
        name = appId.toString(),
        agentPools = arrayListOf("westeurope"),
        subscriptions = arrayListOf(
            Subscriptions(
                alertTypes = arrayListOf(alertType),
                taskIds = arrayListOf("Daily"),
                contactIds = arrayListOf("f04e569f-945d-ec11-93f7-00155d45084f", "6847a325-e56f-ed11-9e59-00155d455476")
            )
        ),
        keywords = arrayListOf(keyword),
        ignoredStatuses = arrayListOf("408")
    )

    return HostTrackerService.instance
        .createHttpTask(task = task).execute().body() ?: throw Exception("Same task already exists")
}

fun onAppUpdate(
    appId: Long,
    waitingFor: WaitingFor,

    packageName: String? = null,
    huawei_id: String? = null,
    keyword: String? = null,
): List<Task> {
    val token = "bearer " + getToken()
    var task = HostTrackerService.instance.getHttpTaskByAppId(appId = appId.toString(), token = token)
        .execute().body()?.firstOrNull() ?: throw Exception("Same task already exists")

    val newAlertType = if (waitingFor == WaitingFor.Up) "Up" else "Down"

    val newKeyword = if (keyword.isNullOrEmpty()) {
        task.keywords
    } else {
        arrayListOf(keyword)
    }

    val newUrl = when{
        huawei_id != null -> "https://appgallery.huawei.com/app/$huawei_id"
        packageName != null ->  "https://play.google.com/store/apps/details?id=$packageName"
        else -> task.url
    }


    task = task.copy(
        url = newUrl,
        keywords = newKeyword,
        subscriptions = arrayListOf(
            Subscriptions(
                alertTypes = arrayListOf(newAlertType),
                taskIds = arrayListOf("Daily"),
                contactIds = arrayListOf("f04e569f-945d-ec11-93f7-00155d45084f", "6847a325-e56f-ed11-9e59-00155d455476")
            )
        )
    )

    println("---------------" + task)

    return HostTrackerService.instance.updateHttpTask(
        appId = appId.toString(),
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

fun onAppDelete(
    appId: Long
): List<Task> {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.deleteHttpTask(
        appId = appId.toString()
    ).execute().body()
        ?: throw Exception("No task with this url")
}

//fun onPostbackUp(url: String): List<Task> {
//    val token = "bearer " + getToken()
//
//    var task = getHttpTaskByUrl(url = url).firstOrNull() ?: throw Exception("No task with this url")
//    val subscriptions = HostTrackerService.instance
//        .getHttpTaskSubscriptions(token = token, taskId = task.id!!)
//        .execute()
//        .body() ?: throw Exception("No subscriptions with this id")
//
//    val updatedSubscriptions = subscriptions.map {
//        Subscriptions(
//            alertTypes = arrayListOf("Down"),
//            taskIds = it.taskIds,
//            contactIds = it.contactIds
//        )
//    } as ArrayList
//
//    Thread.sleep(1000)
//
//    /*val updatedSubscriptions = Subscriptions(
//        alertTypes = arrayListOf("Down"),
//        taskIds = arrayListOf("Daily")
//    )*/
//
//    task = task.copy(subscriptions = updatedSubscriptions)
//
//
//    return HostTrackerService.instance.updateHttpTask(
//        token = token,
//        url = task.url!!,
//        task = task
//    ).execute().body() ?: throw Exception("No task with this url")
//}

fun getHttpTaskByUrl(url: String): List<Task> {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.getHttpTaskByUrl(
        token = token,
        url = url
    ).execute().body()
        ?: throw Exception("No task with this url")
}

//fun onPostbackDown(url: String): List<Task> {
//    val token = "bearer " + getToken()
//
//    var task = getHttpTaskByUrl(url = url).firstOrNull() ?: throw Exception("No task with this url")
//
//    task = task.copy(enabled = false, name = "DISABLED " + task.name)
//
//    Thread.sleep(1000)
//
//    return HostTrackerService.instance.updateHttpTask(
//        token = token,
//        url = task.url!!,
//        task = task
//    ).execute().body() ?: throw Exception("No task with this url")
//}

fun main() {


    //println(onPostbackDown("https://github.com/grigoriy322/HostTrackerTest/tree/main"))
        println(
            onAppUpdate(
                packageName = "https://github.com/grigoriy322/HostTrackerTest/tree/main",
                keyword = "kek"
            )
        )

//    println(
//        onAppUpdate(
//            packageName = "tree/main",
//            keyword = "HostTrackerTest 2.0",
//        )
//    )
//    val token = "bearer " + getToken()
//    var task = HostTrackerService.instance.getHttpTaskByAppId(
//        token = token,
//        appId = 12.toString()
//    ).execute().body() ?: throw Exception("No task with this url")
//    println(task)


    //println(onPostbackUp(test.url!!))

//    val result = onAppUpdate(12, waitingFor = WaitingFor.Up, keyword = "2", huawei_id = "123", packageName = "asd")
//    println(result)
    println(onAppDelete(12))

    //println( deleteHttpTask("https://www.google.com") )
}
