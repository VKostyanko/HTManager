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

fun createHttpTask(task: Task): Task {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.createHttpTask(
        token = token,
        task = task
    ).execute().body() ?: throw Exception("Same task already exists")
}


fun deleteHttpTask(url: String): List<Task> {
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

    task.subscriptions.forEach{ println(it) }

    val updatedSubscriptions = task.subscriptions.map {
        Subscriptions(
            alertTypes = arrayListOf("Down"),
            taskIds = it.taskIds,
            contactIds = it.contactIds
        )
    } as ArrayList

    task = task.copy(subscriptions = updatedSubscriptions)

    task.subscriptions.forEach{ println(it) }

    return HostTrackerService.instance.updateHttpTask(
        token = token,
        url = task.url!!,
        task = task
    ).execute().body().also {
        println(it?.first())
    } ?: throw Exception("No task with this url")
}

fun getHttpTask(url: String): List<Task> {
    val token = "bearer " + getToken()
    return HostTrackerService.instance.getHttpTask(
        token = token,
        url = url
    ).execute().body()
        ?: throw Exception("No task with this url")
}

/*fun main() {
    val testSub = Subscriptions(
        alertTypes = arrayListOf("Up", "Down"),
        taskIds = arrayListOf("Daily"),
        contactIds = arrayListOf("f04e569f-945d-ec11-93f7-00155d45084f", "6847a325-e56f-ed11-9e59-00155d455476")
    )

    val testTask = Task(
        url = "https://www.google.com",
        httpMethod = "Get",
        timeout = 10000,
        interval = 60,
        enabled = true,
        name = "testing web api on private resourse",
        agentPools = arrayListOf("westeurope"),
        subscriptions = arrayListOf(testSub)
    )

    val test = createHttpTask(testTask)
    println(test)


    val updTestSub = Subscriptions(
        alertTypes = arrayListOf("Down"),
        taskIds = arrayListOf("Daily"),
        contactIds = arrayListOf("f04e569f-945d-ec11-93f7-00155d45084f", "6847a325-e56f-ed11-9e59-00155d455476")
    )

    println(updateHttpTask(test.copy(url = "1111", subscriptions = arrayListOf(updTestSub))))

    //println( deleteHttpTask("https://www.google.com") )
}*/
