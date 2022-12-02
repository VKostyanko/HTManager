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

        @PUT("tasks")
        fun updateHttpTask(
            @Header("Authorization") token: String,
            @Query("url") url: String,
            @Body task: Task
        ): Call<List<Task>>

        @DELETE("tasks")
        fun deleteHttpTask(
            @Header("Authorization") token: String,
            @Query("url") url: String
        ): Call<List<Task>>

        @GET("tasks")
        fun getHttpTask(
            @Header("Authorization") token: String,
            @Query("url") url: String
        ): Call<List<Task>>

        @GET("subscriptions")                //todo: delete with Models.Subscriptions
        fun getHttpTaskSubscriptions(
            @Header("Authorization") token: String,
            @Query("taskId") taskId: String
        ): Call<List<Subscriptions>>
    }
}



