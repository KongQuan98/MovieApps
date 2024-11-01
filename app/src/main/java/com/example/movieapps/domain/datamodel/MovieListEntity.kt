package com.example.movieapps.domain.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movielist")
data class MovieListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val year: Int,
    val imdbID: String,
    val type: String,
    val poster: String
)