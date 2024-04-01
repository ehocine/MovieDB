package com.hocel.moviedb.data.database.favoriteMoviesDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hocel.moviedb.data.models.movieDetails.MovieDetails
import com.hocel.moviedb.utils.Constants.FAVORITE_MOVIES_TABLE

@Entity(tableName = FAVORITE_MOVIES_TABLE)
class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var movie: MovieDetails
)