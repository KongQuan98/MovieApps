package com.example.movieapps.domain

import com.example.movieapps.domain.datamodel.MovieDetailsResponse
import com.example.movieapps.domain.datamodel.MovieListModel
import com.example.movieapps.domain.datamodel.MovieListResponse
import com.example.movieapps.domain.datamodel.toEntityModel
import com.example.movieapps.domain.datamodel.toResponseModel
import com.example.movieapps.domain.service.MovieImdbApi
import com.example.movieapps.domain.storage.MovieListDao

class MovieImdbRepository(
    private val movieImdbApi: MovieImdbApi,
    private val movieListDao: MovieListDao
) {
    suspend fun searchMovies(query: String): List<MovieListModel> {
        val response =
            movieImdbApi.getMovieList(searchQuery = query)
        return try {
            if (response.isSuccessful) {
                // add movie list response to cache
                deleteMovieListCache()
                response.body()?.let { insertMovieListCache(it) }
                response.body()?.search ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMovieDetails(movieId: String): MovieDetailsResponse? {
        val response =
            movieImdbApi.getMovieDetails(movieId = movieId)
        return try {
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun insertMovieListCache(movieListResponse: MovieListResponse) {
        movieListDao.insertRecentMovieListPayload(movieListResponse.toEntityModel())
    }

    suspend fun deleteMovieListCache() {
        movieListDao.deleteAll()
    }

    suspend fun getMovieListCache(): MovieListResponse? {
        return movieListDao.getRecentMovieListPayload()?.toResponseModel()
    }
}