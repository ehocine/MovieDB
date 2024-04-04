package com.hocel.moviedb.presentation.moviesList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hocel.moviedb.data.api.MoviesService
import com.hocel.moviedb.data.models.moviesList.Movie
import javax.inject.Inject

class MoviesListPagingSource @Inject constructor(
    private val api: MoviesService,
    private val minRating: Float,
    private val genre: String,
    private val sortBy: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = api.getTrendingMovies(
                page = page,
                rating = minRating,
                genre = genre,
                sortBy = sortBy
            )

            LoadResult.Page(
                data = response.movies!!.ifEmpty { emptyList() },
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.movies.size < 20) null else page.plus(1),
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}