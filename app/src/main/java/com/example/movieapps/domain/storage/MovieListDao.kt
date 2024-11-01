package com.example.movieapps.domain.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movieapps.domain.datamodel.MovieListEntity

@Dao
interface MovieListDao {
    @Insert
    suspend fun insertRecentMovieListPayload(movieList: List<MovieListEntity>)

    @Query("SELECT * FROM movielist")
    suspend fun getRecentMovieListPayload(): List<MovieListEntity>?

    @Query("DELETE FROM movielist")
    suspend fun deleteAll()
}
