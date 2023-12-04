package com.example.mad.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        const val IMAGES = "https://image.tmdb.org/t/p/w500"

        val movieClient by lazy { createApi(BASE_URL) }

        private fun createApi(baseUrl: String): ApiService {
            val client = OkHttpClient.Builder().apply {
                // API key interceptor
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                readTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
            }.build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

