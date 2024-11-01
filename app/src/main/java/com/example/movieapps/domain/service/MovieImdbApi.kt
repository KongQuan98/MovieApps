package com.example.movieapps.domain.service

import com.example.movieapps.domain.datamodel.MovieDetailsResponse
import com.example.movieapps.domain.datamodel.MovieListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
interface MovieImdbApi {
    @GET("/")
    suspend fun getMovieList(
        @Query("apiKey") key: String = "6fc87060",
        @Query("s") searchQuery: String,
        @Query("type") type: String = "movie"
    ): Response<MovieListResponse>

    @GET("/")
    suspend fun getMovieDetails(
        @Query("apiKey") key: String = "6fc87060",
        @Query("i") movieId: String
    ): Response<MovieDetailsResponse>
}