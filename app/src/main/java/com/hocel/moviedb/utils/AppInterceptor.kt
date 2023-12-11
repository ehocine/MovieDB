package com.hocel.moviedb.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        return chain.proceed(requestBuilder.build())
    }
}

private val accessToken =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlYjhmMGIxYTEzNGRhMmM0OTE5ZDM2ZTIxOTg5YTczNiIsInN1YiI6IjY1NzU" +
            "3ZTNkZTkzZTk1MjE5MDBjMGIyNiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Keroh744Ll" +
            "zb2CUVpP4cp5wEAg_SqZIkhW_jSdPnAeI"