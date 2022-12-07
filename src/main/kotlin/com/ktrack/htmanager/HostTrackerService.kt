package com.ktrack.htmanager

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*

object HostTrackerService {
    val instance by lazy {
        /*val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY*/
        val interceptor = HostTrackerInterceptor()
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.host-tracker.com/api/web/v1/")
            .addConverterFactory(JacksonConverterFactory.create())
            .client(okHttpClient)
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
            @Body task: Task
        ): Call<Task>

        @PUT("tasks/http")
        fun updateHttpTask(
            @Query("name") appId: String,
            @Body task: Task
        ): Call<List<Task>>

        @DELETE("tasks")
        fun deleteHttpTask(
            @Query("name") appId: String
        ): Call<List<Task>>

        @GET("tasks")
        fun getHttpTask(
            @Query("name") appId: String
        ): Call<List<Task>>

        @GET("subscriptions")
        fun getHttpTaskSubscriptions(//todo: delete with Models.Subscriptions
            @Query("taskId") taskId: String
        ): Call<List<Subscriptions>>

        @GET("tasks")
        fun getHttpTasksList(): Call<List<Task>>
    }
}



