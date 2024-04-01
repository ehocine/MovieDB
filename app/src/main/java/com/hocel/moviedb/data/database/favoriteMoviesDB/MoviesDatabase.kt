package com.hocel.moviedb.data.database.favoriteMoviesDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoviesTypeConverter::class)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun moviesDAO(): MoviesDAO

}