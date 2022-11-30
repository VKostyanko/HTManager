package com.ktrack.htmanager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

object HostTrackerService {
    val instance by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.host-tracker.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HostTrackerService::class.java)
    }

    interface HostTrackerService {

        @POST("/api/web/v1/users/token")
        fun getToken(login: String, password: String): UserToken
    }
}

data class UserToken(
    val token: String,
    val expirationTime: String,
    val expirationUnixTim: Long,
)


