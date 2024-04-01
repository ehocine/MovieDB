package com.hocel.moviedb.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hocel.moviedb.data.api.MoviesService
import com.hocel.moviedb.data.database.favoriteMoviesDB.FavoriteMovieEntity
import com.hocel.moviedb.data.database.favoriteMoviesDB.MoviesDAO
import com.hocel.moviedb.data.models.genres.Genre
import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.data.models.movieDetails.MovieDetails
import com.hocel.moviedb.data.models.trendingMovies.MoviesResponse
import com.hocel.moviedb.presentation.trendingMovies.SearchMoviesPagingSource
import com.hocel.moviedb.presentation.trendingMovies.TrendingMoviesPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val api: MoviesService,
    private val moviesDAO: MoviesDAO
) {

    suspend fun insertFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        return moviesDAO.insertFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
    }

    fun readFavoriteMovies(): Flow<List<FavoriteMovieEntity>> {
        return moviesDAO.readFavoriteMovies()
    }

    suspend fun deleteFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity) {
        return moviesDAO.deleteFavoriteMovie(favoriteMovieEntity = favoriteMovieEntity)
    }

    suspend fun deleteAllFavoriteMovies() {
        return moviesDAO.deleteAllFavoriteMovies()
    }

    fun getTrendingMoviesPaged(minRating: Float, genre: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            TrendingMoviesPagingSource(api, minRating, genre)
        }
    ).flow

    suspend fun getMovieDetails(movieId: Int): Response<MovieDetails> {
        return api.getMovieDetails(movieId)
    }

    fun searchMovies(query: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 1
        ),
        pagingSourceFactory = {
            SearchMoviesPagingSource(api, query)
        }
    ).flow

    suspend fun getMovieRecommendations(movieId: Int): Response<MoviesResponse> {
        return api.movieRecommendations(movieId)
    }

    suspend fun getListOfGenres(): Genres = api.getListOfGenres()
}