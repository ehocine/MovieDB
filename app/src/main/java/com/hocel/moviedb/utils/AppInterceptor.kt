package com.hocel.moviedb.utils

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInterceptor @Inject constructor() : Interceptor {
    private var openaiAPIKey: String? = null
    fun setOpenaiAPIKey(accessToken: String?) {
        this.openaiAPIKey = accessToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $ap")
//        openaiAPIKey?.let {
//            requestBuilder.addHeader("Authorization", "Bearer $it")
//        }

        return chain.proceed(requestBuilder.build())
    }
}

private val ap =
    "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlYjhmMGIxYTEzNGRhMmM0OTE5ZDM2ZTIxOTg5YTczNiIsInN1YiI6IjY1NzU3ZTNkZTkzZTk1MjE5MDBjMGIyNiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Keroh744Llzb2CUVpP4cp5wEAg_SqZIkhW_jSdPnAeI"