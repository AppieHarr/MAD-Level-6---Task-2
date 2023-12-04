package com.example.mad.ui.screens.main

import android.annotation.SuppressLint
import android.media.ImageReader
import android.os.Bundle
import android.provider.Settings.System.putInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.mad.Constants.API_KEY
import com.example.mad.R
import com.example.mad.data.api.Api
import com.example.mad.data.api.Api.Companion.IMAGES
import com.example.mad.data.api.ApiService
import com.example.mad.data.api.util.Resource
import com.example.mad.data.model.Movie
import com.example.mad.ui.MovieScreens

// Suppress lint warning for unused scaffold padding parameter.
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
// Opt-in for experimental Compose UI API.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navController: NavController, viewModel: MoviesViewModel) {
    // Remember the search query state.
    val searchQueryState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    // Observe movies resource from the view model.
    val moviesResource by viewModel.moviesResource.observeAsState()

    // Remember the error state.
    val errorState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar() {
            SearchView(
                searchQueryState = searchQueryState,
                keyboardController = LocalSoftwareKeyboardController.current,
                onSearch = { query ->
                    // Search for movies with the given query.
                    viewModel.searchMovies(query, API_KEY)
                },
                viewModel = viewModel,
                errorState = errorState
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Handle different resource states.
            when (val resource = moviesResource) {
                is Resource.Success -> {
                    val movies = resource.data?.results
                    if (movies != null) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(movies.size) { index ->
                                val movie = movies[index]
                                MovieCard(movie) { selectedMovie ->
                                    // Navigate to the movie detail screen when a movie is selected.
                                    val movieIndex = movies.indexOf(selectedMovie)
                                    navController.navigate("${MovieScreens.MovieDetailScreen.route}/$movieIndex")
                                }
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Text(text = resource.message ?: "An unknown error occurred",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                is Resource.Loading -> {
                    // Display a loading indicator.
                    CircularProgressIndicator()
                }
                is Resource.Empty -> {
                    Text(text = errorState.value,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
                else -> {}
            }
        }
    }
}


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SearchView(
        searchQueryState: MutableState<TextFieldValue>,
        keyboardController: SoftwareKeyboardController?,
        onSearch: (query: String) -> Unit,
        errorState: MutableState<String>,
        viewModel: MoviesViewModel
    ) {
        TextField(
            value = searchQueryState.value,
            onValueChange = { value ->
                searchQueryState.value = value
                onSearch(value.text)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.subtitle1,
            leadingIcon = {
                IconButton(onClick = {
                    if (searchQueryState.value.text == ""){
                        errorState.value = "Search query cant be empty"

                    }else{
                        errorState.value = ""
                        viewModel.searchMovies(
                            API_KEY, searchQueryState.value.text
                        )
                    }
                    keyboardController?.hide()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon_description),
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                if (searchQueryState.value.text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchQueryState.value = TextFieldValue("")
                            onSearch("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search_icon_description),
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search_hint),
                    style = MaterialTheme.typography.subtitle1.copy(
                        color = Color.White.copy(
                            alpha = 0.6f
                        ),
                    )
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                placeholderColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @Composable
    fun MovieCard(movie: Movie, onSelectMovie: (movie: Movie) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onSelectMovie(movie)
                },

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.7f)
                    .background(Color.Black)
            ) {
                val imagePainter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = IMAGES + movie.poster_path)
                        .apply(
                            block = fun ImageRequest.Builder.() {
                                memoryCachePolicy(CachePolicy.ENABLED)
                            }
                        ).build()
                )
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

    }


