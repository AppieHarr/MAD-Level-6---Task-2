package com.example.mad.ui

import androidx.annotation.StringRes
import com.example.mad.R

/**
 * You can consider a sealed class as an enum++
 * Sealed classes can also contain state - making it very useful for different network response
 * @author Pim Meijer
 */
sealed class MovieScreens(val route: String) {
    object MovieSearchScreen : MovieScreens("movie_search_screen")
    object MovieDetailScreen : MovieScreens("movie_details/{movieIndex}")
}


