package com.example.movieapps.domain.datamodel

fun MovieListResponse.toEntityModel(): List<MovieListEntity> {
    return search.map {
        MovieListEntity(
            title = it.title,
            year = it.year,
            imdbID = it.imdbID,
            type = it.type,
            poster = it.poster
        )
    }
}

fun List<MovieListEntity>.toResponseModel(): MovieListResponse {
    return MovieListResponse(
        search = this.map { movieDBModel ->
            MovieListModel(
                title = movieDBModel.title,
                year = movieDBModel.year,
                imdbID = movieDBModel.imdbID,
                type = movieDBModel.type,
                poster = movieDBModel.poster
            )
        },
        totalResult = this.size,
        response = false
    )
}
