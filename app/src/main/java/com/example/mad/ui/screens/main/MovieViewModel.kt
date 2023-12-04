package com.example.mad.ui.screens.main

import android.app.Application
import androidx.lifecycle.*
import com.example.mad.data.api.util.Resource
import com.example.mad.data.model.Movie
import com.example.mad.data.model.MoviesResponse
import com.example.mad.ui.repository.MoviesRepository
import kotlinx.coroutines.launch

// ViewModel class for managing and storing UI-related data for movies
class MoviesViewModel(application: Application) : AndroidViewModel(application) {
    // Create an instance of MoviesRepository to fetch movie data
    private val moviesRepository = MoviesRepository()

    // Public LiveData for observing movie search results (MoviesResponse)
    val moviesResource: LiveData<Resource<MoviesResponse>>
        get() = _moviesResource

    // Private MutableLiveData for updating movie search results (MoviesResponse)
    private val _moviesResource: MutableLiveData<Resource<MoviesResponse>> = MutableLiveData(Resource.Empty())

    // Function to search movies using the MoviesRepository and update the LiveData
    fun searchMovies(query: String, apiKey: String) {
        // Set the LiveData value to Loading state
        _moviesResource.value = Resource.Loading()

        // Launch a coroutine to fetch movie search results
        viewModelScope.launch {
            val result = moviesRepository.searchMovies(query, apiKey)
            // Update the LiveData value with the fetched result
            _moviesResource.value = result
        }
    }

    // Private MutableLiveData for updating movie details (Movie)
    private val _movieResource: MutableLiveData<Resource<Movie>> = MutableLiveData(Resource.Empty())

    // Function to get movie details using the MoviesRepository and update the LiveData
    fun getMovie(movieId: Int, apiKey: String) {
        // Set the LiveData value to Loading state
        _movieResource.value = Resource.Loading()

        // Launch a coroutine to fetch movie details
        viewModelScope.launch {
            val result = moviesRepository.getMovieDetails(movieId, apiKey)
            // Update the LiveData value with the fetched result
            _movieResource.value = result
        }
    }
}


