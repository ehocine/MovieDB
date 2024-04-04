package com.hocel.moviedb.presentation.moviesList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.data.models.moviesList.Movie
import com.hocel.moviedb.data.repository.RemoteRepository
import com.hocel.moviedb.utils.SortByTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private var _pagedMoviesList: MutableStateFlow<PagingData<Movie>> =
        MutableStateFlow(value = PagingData.empty())
    val pagedMoviesList: MutableStateFlow<PagingData<Movie>> get() = _pagedMoviesList

    private var _listOfGenres: MutableState<Genres> = mutableStateOf(Genres())
    val listOfGenres: State<Genres> = _listOfGenres

    var minRating: MutableState<Float> = mutableFloatStateOf(0f)
        private set

    var selectedGenre: MutableState<String> = mutableStateOf("")
        private set

    var sortBy: MutableState<SortByTypes> = mutableStateOf(SortByTypes.PopularityDesc)
        private set

    init {
        getMoviesListPaged()
        viewModelScope.launch {
            _listOfGenres.value = remoteRepository.getListOfGenres()
        }
    }

    fun setFilters(genre: String, rating: Float, sortByType: SortByTypes) {
        selectedGenre.value = genre
        minRating.value = rating
        sortBy.value = sortByType
    }

    fun getMoviesListPaged() {
        viewModelScope.launch {
            remoteRepository.getMoviesListPaged(
                minRating = minRating.value, genre = selectedGenre.value, sortBy = sortBy.value.code
            ).cachedIn(viewModelScope).collect {
                _pagedMoviesList.value = it
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            remoteRepository.searchMovies(query = query).cachedIn(viewModelScope).collect {
                _pagedMoviesList.value = it
            }
        }
    }


}