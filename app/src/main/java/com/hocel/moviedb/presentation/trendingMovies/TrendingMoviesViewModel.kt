package com.hocel.moviedb.presentation.trendingMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hocel.moviedb.data.models.trendingMovies.Result
import com.hocel.moviedb.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TrendingMoviesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _pagedTrendingMovies: MutableStateFlow<PagingData<Result>> =
        MutableStateFlow(value = PagingData.empty())
    val pagedTrendingMovies: MutableStateFlow<PagingData<Result>> get() = _pagedTrendingMovies

    init {
        viewModelScope.launch {
            getTrendingMoviesPaged()
        }
    }

    private suspend fun getTrendingMoviesPaged() {
        repository.getTrendingMoviesPaged().cachedIn(viewModelScope).collect {
            _pagedTrendingMovies.value = it
        }
    }

}