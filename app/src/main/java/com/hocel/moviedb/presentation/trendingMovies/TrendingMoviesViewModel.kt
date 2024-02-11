package com.hocel.moviedb.presentation.trendingMovies

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hocel.moviedb.data.models.genres.Genres
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

    private var _listOfGenres: MutableState<Genres> = mutableStateOf(Genres())
    val listOfGenres: State<Genres> = _listOfGenres

    init {
        getTrendingMoviesPaged()
        viewModelScope.launch {
            _listOfGenres.value = repository.getListOfGenres()
        }
    }

    fun getTrendingMoviesPaged() {
        viewModelScope.launch {
            repository.getTrendingMoviesPaged().cachedIn(viewModelScope).collect {
                _pagedTrendingMovies.value = it
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            repository.searchMovies(query = query).cachedIn(viewModelScope).collect {
                _pagedTrendingMovies.value = it
            }
        }
    }


}