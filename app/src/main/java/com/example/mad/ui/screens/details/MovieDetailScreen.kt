package com.example.mad.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.mad.Constants.API_KEY
import com.example.mad.data.api.Api
import com.example.mad.data.api.util.Resource
import com.example.mad.ui.screens.main.MoviesViewModel


@Composable
    fun MovieDetailScreen(movieIndex: Int, viewModel: MoviesViewModel) {
        viewModel.getMovie(movieIndex, API_KEY)

        val movieResource by viewModel.moviesResource.observeAsState()

        when (val resource = movieResource) {
            is Resource.Success<*> -> {
                val movie = resource.data?.results?.get(movieIndex)
                if (movie != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        // Banner
                        val imagePainter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = Api.IMAGES + movie.poster_path)
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
                                .fillMaxWidth()
                                .height(250.dp)
                                .align(Alignment.TopCenter),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 200.dp)
                            ) {
                                // Movie poster
                                Image(
                                    painter = imagePainter,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(92.dp, 138.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = movie.title,
                                        style = MaterialTheme.typography.h5,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating",
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = movie.vote_average.toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Overview",
                                        color = Color.White,
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = movie.overview,
                                        style = MaterialTheme.typography.body1,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "Movie data is not available.")
                }
            }
            is Resource.Error -> {
                Text(text = "An error occurred while fetching movie details.")
            }
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                Text(text = "No movie data available.")
            }
        }
}

