package com.ktrack.htmanager

fun getToken(): String = if (
    TokenHolder.token.isEmpty() ||
    TokenHolder.expirationUnixTime * 1000 < System.currentTimeMillis()
) {
    val newToken = HostTrackerService.instance.getToken().execute().body()!!
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
    val token = "bearer " + getToken()

    val url = //if (huawei_id.isNullOrEmpty())
        "https://play.google.com/store/apps/details?id=$packageName"
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

    return HostTrackerService.instance.createHttpTask(
        token = token,
        task = task
    ).execute().body() ?: throw Exception("Same task already exists")
}

fun onAppUpdate(
    packageName: String,
    keyword: String
): List<Task> {
    val token = "bearer " + getToken()

    val url = //"https://play.google.com/store/apps/details?id=$packageName"
        "https://github.com/grigoriy322/HostTrackerTest/$packageName"
    var task = getHttpTask(url = url).firstOrNull() ?: throw Exception("No task with this url")

    Thread.sleep(1000)

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
        token = token,
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

fun onAppDelete(
    url: String // todo: url -> packageName
): List<Task> {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.deleteHttpTask(
        token = token,
        url = url
    ).execute().body()
        ?: throw Exception("No task with this url")
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

    Thread.sleep(1000)

    /*val updatedSubscriptions = Subscriptions(
        alertTypes = arrayListOf("Down"),
        taskIds = arrayListOf("Daily")
    )*/

    task = task.copy(subscriptions = updatedSubscriptions)


    return HostTrackerService.instance.updateHttpTask(
        token = token,
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

fun getHttpTask(url: String): List<Task> {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.getHttpTask(
        token = token,
        url = url
    ).execute().body()
        ?: throw Exception("No task with this url")
}

fun onPostbackDown(url: String): List<Task> {
    val token = "bearer " + getToken()

    var task = getHttpTask(url = url).firstOrNull() ?: throw Exception("No task with this url")

    task = task.copy(enabled = false, name = "DISABLED " + task.name)

    Thread.sleep(1000)

    return HostTrackerService.instance.updateHttpTask(
        token = token,
        url = task.url!!,
        task = task
    ).execute().body() ?: throw Exception("No task with this url")
}

/*fun retryRequestWrapper(count: Int, foo: () -> Unit){         todo
    if (count==0) throw Exception("No task with this url")
    try {
        foo()
    }catch (e:Exception){
        retryRequestWrapper(count-1, foo)
    }
}*/

fun main() {


    //println(onPostbackDown("https://github.com/grigoriy322/HostTrackerTest/tree/main"))

    println(
        onAppUpdate(
            packageName = "tree/main",
            keyword = "HostTrackerTest 2.0",
        )
    )


    //println(onPostbackUp(test.url!!))

    //println( deleteHttpTask("https://www.google.com") )
}
