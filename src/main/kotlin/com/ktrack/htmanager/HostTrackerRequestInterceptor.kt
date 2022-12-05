package com.ktrack.htmanager

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class HostTrackerRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (chain.request().url == "https://www.host-tracker.com/api/web/v1/users/token".toHttpUrl()) {
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.code == 429) {
                response.close()
                Thread.sleep(1000)
                chain.proceed(request)
            } else response

        } else {
            val request = chain.request().newBuilder()
                .header("Authorization", "bearer " + getToken())
                .build()

            val response = chain.proceed(request)

            when {
                /**
                 * if (HTTP 429 Too Many Requests) wait 1 second and retry
                 * */
                response.code == 429 -> {
                    response.close()
                    Thread.sleep(1000)
                    chain.proceed(request)
                }

                /**
                 * if (other error) wait 1 second and retry with new token
                 * */
                response.isSuccessful.not() -> {
                    response.close()
                    Thread.sleep(1000)
                    request.newBuilder()
                        .header("Authorization", "bearer " + getToken(forcibly = true))
                        .build()
                    chain.proceed(request)
                }

                else -> response
            }
        }
    }
}