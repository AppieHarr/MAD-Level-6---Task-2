package com.example.mad.data.api


import com.example.mad.data.model.Movie
import com.example.mad.data.model.MoviesResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String, @Query("api_key") apiKey: String): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie>
}

