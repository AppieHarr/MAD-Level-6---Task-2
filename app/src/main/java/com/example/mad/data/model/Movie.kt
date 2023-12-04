package com.example.mad.data.model

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val overview: String,
    val poster_path: String?,
    val backdropPath: String?,
    val vote_average: Double,
    val vote_count: Int,
    val genres: List<Genre>
)