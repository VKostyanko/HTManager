package com.ktrack.htmanager

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object HostTrackerService {
    val instance by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.host-tracker.com/api/web/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            //.client(okHttpClient)
            .build()

        retrofit.create(HostTrackerService::class.java)
    }

    interface HostTrackerService {

        @POST("users/token")
        fun getToken(
            @Body userCredentials: UserCredentials = UserCredentials(
                login = "offerwall",
                password = "asd32redDQwd"
            )
        ): Call<UserToken>

        @POST("tasks/http")
        fun createHttpTask(
            @Header("Authorization") token: String,
            @Body task: Task
        ): Call<Task>

        @PUT("tasks/{TASK_ID}")
        fun updateHttpTask(
            @Header("Authorization") token: String,
            @Path("TASK_ID") id: String,
            @Body task: Task
        ): Call<Task>

        @DELETE("tasks/{TASK_ID}")
        fun deleteHttpTask(
            @Header("Authorization") token: String,
            @Path("TASK_ID") id: String
        ): Call<Task>
    }
}



