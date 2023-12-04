package com.example.mad.ui.repository

import android.util.Log
import com.example.mad.data.api.Api
import com.example.mad.data.api.ApiService
import com.example.mad.data.api.util.Resource
import com.example.mad.data.model.Movie
import com.example.mad.data.model.MoviesResponse
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MoviesRepository {
    private val movieApiService: ApiService = Api.movieClient

    // Suspend function to search for movies using the provided query and API key.
    suspend fun searchMovies(query: String, apiKey: String): Resource<MoviesResponse> {
        return try {
            // Perform the API call with a 5-second timeout.
            val response = withTimeout(5_000) {
                movieApiService.searchMovies(query, apiKey)
            }

            // If the results are empty or null, return an empty resource; otherwise, return a success resource.
            if (response.results.isNullOrEmpty()) {
                Resource.Empty()
            } else {
                Resource.Success(response)
            }
        } catch (e: Exception) {
            // Log and return an error resource if an exception occurs.
            Log.e("MoviesRepository", e.message ?: "No exception message available")
            Resource.Error("An unknown error occurred")
        }
    }

    // Set up the Retrofit API instance.
    private val api = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    // function to get movie details for a specific movie ID and API key.
    suspend fun getMovieDetails(movieId: Int, apiKey: String): Resource<Movie> {
        return try {
            // Perform the API call to get movie details.
            val response = api.getMovieDetails(movieId, apiKey)
            if (response.isSuccessful) {
                // If the response is successful and the movie object is not null, return a success resource.
                val movie = response.body()
                if (movie != null) {
                    Resource.Success(movie)
                } else {
                    Resource.Error("Unable to fetch movie details")
                }
            } else {
                // If the response is unsuccessful, return an error resource with an appropriate message.
                Resource.Error("Unable to fetch movie details: ${response.message()}")
            }
        } catch (e: Exception) {
            // Return an error resource with an appropriate message if an exception occurs.
            Resource.Error("Error fetching movie details: ${e.message}")
        }
    }

}




