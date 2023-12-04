package com.example.mad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mad.ui.MovieScreens
import com.example.mad.ui.screens.details.MovieDetailScreen
import com.example.mad.ui.screens.main.MainScreen
import com.example.mad.ui.screens.main.MoviesViewModel
import com.example.mad.ui.theme.MADTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel = remember { MoviesViewModel(application) }

            NavHost(navController = navController, startDestination = MovieScreens.MovieSearchScreen.route) {
                composable(MovieScreens.MovieSearchScreen.route) {
                    MainScreen(navController, viewModel)
                }
                // Define a composable for the movie detail screen, taking the movie index as a route parameter.
                composable("${MovieScreens.MovieDetailScreen.route}/{movieIndex}") { backStackEntry ->
                    // Retrieve the movie index from the back stack entry arguments, defaulting to 0 if not found.
                    val movieIndex = backStackEntry.arguments?.getString("movieIndex")?.toIntOrNull() ?: 0
                    // Call the MovieDetailScreen composable with the retrieved movie index and view model.
                    MovieDetailScreen(movieIndex, viewModel)
                }

            }


        }
    }
}
