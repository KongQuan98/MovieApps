package com.example.movieapps.domain.datamodel

import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("Search")
    val search: List<MovieListModel>,
    @SerializedName("totalResults")
    val totalResult: Int,
    @SerializedName("Response")
    val response: Boolean
)

data class MovieListModel(
    @SerializedName("Title")
    val title: String,
    @SerializedName("Year")
    val year: Int,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Poster")
    val poster: String
)