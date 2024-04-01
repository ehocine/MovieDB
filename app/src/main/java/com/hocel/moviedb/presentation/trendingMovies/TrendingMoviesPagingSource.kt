package com.hocel.moviedb.presentation.trendingMovies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hocel.moviedb.data.api.MoviesService
import com.hocel.moviedb.data.models.trendingMovies.Result
import javax.inject.Inject

class TrendingMoviesPagingSource @Inject constructor(
    private val api: MoviesService,
    private val minRating: Float,
    private val genre: String
) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = api.getTrendingMovies(page = page, rating = minRating, genre = genre)

            LoadResult.Page(
                data = response.results!!.ifEmpty { emptyList() },
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.results.size < 20) null else page.plus(1),
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}