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
    keyword: String,
    siteName: String
): Task {
    val url = packageName//if (huawei_id.isNullOrEmpty())
//        "https://play.google.com/store/apps/details?id=$packageName"
//    else
//        "https://appgallery.huawei.com/app/$huawei_id"

    val task = Task(
        url = url,
        httpMethod = "Get",
        timeout = 10000,
        interval = 1, //todo: utochnit'
        enabled = true,
        name = siteName,
        agentPools = arrayListOf("westeurope"),
        subscriptions = arrayListOf(
            Subscriptions(
                alertTypes = arrayListOf("Up"),
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
    packageName: String,
    keyword: String
): List<Task> {
    val url = packageName//"https://play.google.com/store/apps/details?id=$packageName"
        //"https://github.com/grigoriy322/HostTrackerTest/$packageName"
    var task = getHttpTask(url = url).firstOrNull() ?: throw Exception("No task with this url")

    task = task.copy(
        keywords = arrayListOf(keyword),

        //todo: delete
        subscriptions = arrayListOf(
            Subscriptions(
                alertTypes = arrayListOf("Up"),
                taskIds = arrayListOf("Daily"),
                contactIds = arrayListOf("f04e569f-945d-ec11-93f7-00155d45084f", "6847a325-e56f-ed11-9e59-00155d455476")
            )
        )
        //todo: -------

    )

    println("---------------" + task)

    return HostTrackerService.instance.updateHttpTask(
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

fun onAppDelete(
    url: String // todo: url -> packageName
): List<Task> {
    return HostTrackerService.instance
        .deleteHttpTask(url = url).execute().body() ?: throw Exception("No task with this url")
}

fun onPostbackUp(url: String): List<Task> {
    val token = "bearer " + getToken()

    var task = getHttpTask(url = url).firstOrNull() ?: throw Exception("No task with this url")
    val subscriptions = HostTrackerService.instance
        .getHttpTaskSubscriptions(token = token, taskId = task.id!!)
        .execute()
        .body() ?: throw Exception("No subscriptions with this id")

    val updatedSubscriptions = subscriptions.map {
        Subscriptions(
            alertTypes = arrayListOf("Down"),
            taskIds = it.taskIds,
            contactIds = it.contactIds
        )
    } as ArrayList

    /*val updatedSubscriptions = Subscriptions(
        alertTypes = arrayListOf("Down"),
        taskIds = arrayListOf("Daily")
    )*/

    task = task.copy(subscriptions = updatedSubscriptions)


    return HostTrackerService.instance.updateHttpTask(
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}


fun getHttpTask(url: String): List<Task> {
    return HostTrackerService.instance
        .getHttpTask(url = url).execute().body() ?: throw Exception("No task with this url")
}

fun onPostbackDown(url: String): List<Task> {
    val token = "bearer " + getToken()

    var task = getHttpTask(url = url).firstOrNull() ?: throw Exception("No task with this url")

    task = task.copy(enabled = false, name = "DISABLED " + task.name)

    return HostTrackerService.instance.updateHttpTask(
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

fun main() {


    //println(onPostbackDown("https://github.com/grigoriy322/HostTrackerTest/tree/main"))
        println(
            onAppUpdate(
                packageName = "https://github.com/grigoriy322/HostTrackerTest/tree/main",
                keyword = "kek"
            )
        )

    //println(onPostbackUp(test.url!!))

    //println( deleteHttpTask("https://www.google.com") )
}
