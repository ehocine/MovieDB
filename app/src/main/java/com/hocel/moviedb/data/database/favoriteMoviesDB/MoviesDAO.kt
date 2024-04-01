package com.hocel.moviedb.data.database.favoriteMoviesDB

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity)

    @Query("SELECT * FROM FAVORITE_MOVIES_TABLE ORDER BY id ASC")
    fun readFavoriteMovies(): Flow<List<FavoriteMovieEntity>>

    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity)

    @Query("DELETE FROM FAVORITE_MOVIES_TABLE")
    suspend fun deleteAllFavoriteMovies()
}