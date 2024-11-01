package com.example.movieapps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapps.domain.MovieImdbRepository
import com.example.movieapps.domain.datamodel.MovieDetailsResponse
import com.example.movieapps.domain.datamodel.MovieListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieImdbRepository: MovieImdbRepository
) : ViewModel() {

    private val _searchMovieListResult = MutableLiveData<NetworkResponse<List<MovieListModel>>>()
    val movieListResult: LiveData<NetworkResponse<List<MovieListModel>>> = _searchMovieListResult

    private val _getMovieDetailResult = MutableLiveData<NetworkResponse<MovieDetailsResponse>>()
    val getMovieDetailResult: LiveData<NetworkResponse<MovieDetailsResponse>> =
        _getMovieDetailResult

    fun performSearch(query: String) {
        viewModelScope.launch {
            _searchMovieListResult.value = NetworkResponse.Loading
            try {
                val response = movieImdbRepository.searchMovies(query)
                if (response.isNotEmpty()) {
                    _searchMovieListResult.value = NetworkResponse.Success(response)
                } else {
                    _searchMovieListResult.value = NetworkResponse.Error("No movies found")
                }
            } catch (e: Exception) {
                _searchMovieListResult.value = NetworkResponse.Error("No movies found")
            }
        }
    }

    fun performGetMovieDetails(movieId: String) {
        viewModelScope.launch {
            _getMovieDetailResult.value = NetworkResponse.Loading
            try {
                val response = movieImdbRepository.getMovieDetails(movieId)
                if (response != null) {
                    _getMovieDetailResult.value = NetworkResponse.Success(response)
                } else {
                    _getMovieDetailResult.value =
                        NetworkResponse.Error("Error fetching movie details.")
                }
            } catch (e: Exception) {
                _getMovieDetailResult.value = NetworkResponse.Error("Error fetching movie details.")
            }
        }
    }

    fun getCacheDataOnFirstLaunch() {
        viewModelScope.launch {
            val cacheData = movieImdbRepository.getMovieListCache()
            _searchMovieListResult.value =
                if (cacheData != null) NetworkResponse.Success(cacheData.search) else null
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            movieImdbRepository.deleteMovieListCache()
        }
    }
}

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val errorMessage: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}