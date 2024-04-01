package com.hocel.moviedb.data.database.favoriteMoviesDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hocel.moviedb.data.models.movieDetails.MovieDetails

class MoviesTypeConverter {
    var gson = Gson()


    @TypeConverter
    fun resultToString(foodRecipeResult: MovieDetails): String {
        return gson.toJson(foodRecipeResult)
    }

    @TypeConverter
    fun stringToResult(data: String): MovieDetails {
        val listType = object : TypeToken<MovieDetails>() {}.type
        return gson.fromJson(data, listType)
    }
}