package com.onoffrice.marvel_comics.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddHeaderInterceptor : Interceptor {
    private val REQUEST_HEADER_AUTHENTICATION =
        listOf(
            RequestHeaderInterceptor(
                "Client-ID",
                ""
            )

        )

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()
        var request  = original

            request = original.newBuilder().also { request ->
                REQUEST_HEADER_AUTHENTICATION.forEach {
                    request.addHeader(it.name, it.value)
                }
            }
                .method(original.method(), original.body())
                .build()

        return chain.proceed(request)
    }
}