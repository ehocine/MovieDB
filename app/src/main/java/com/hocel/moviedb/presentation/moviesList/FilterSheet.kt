package com.hocel.moviedb.presentation.moviesList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hocel.moviedb.data.models.genres.Genre
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.ButtonColor
import com.hocel.moviedb.ui.theme.TextColor
import com.hocel.moviedb.utils.DropDownOptions
import com.hocel.moviedb.utils.SortByTypes

@Composable
fun FilterSheetContent(
    mSelectedGenre: Genre,
    sortByDefault: SortByTypes,
    minRating: Float,
    genresList: List<Genre>,
    sortByList: List<SortByTypes>,
    onApplyClicked: (genreId: String, minRating: Float) -> Unit
) {
    val list = listOf(Genre(0, "All")) + genresList

    val listOfRating = listOf(
        "All", "9+", "8+", "7+", "6+", "5+", "4+", "3+", "2+", "1+"
    )

    var selectedGenre by remember { mutableStateOf(mSelectedGenre) }
    var selectedSortByType by remember { mutableStateOf(sortByDefault) }
    var selectedMinRating by remember { mutableFloatStateOf(minRating) }

    Column(Modifier.background(BackgroundColor)) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Genre",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            val genreLabel: (Genre) -> String = { it.name }
            DropDownOptions(
                defaultValue = selectedGenre.name,
                optionsList = list,
                labelExtractor = genreLabel,
                onOptionSelected = {
                    selectedGenre = it
                })
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Sort by",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            val labelExtractor: (SortByTypes) -> String = { it.value }
            DropDownOptions(
                defaultValue = sortByDefault.value,
                optionsList = sortByList,
                labelExtractor = labelExtractor,
                onOptionSelected = {
                    selectedSortByType = it
                })
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Minimum rating",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            val l: (String) -> String = { it }
            DropDownOptions(
                defaultValue = if (minRating == 0f) "All" else "${minRating.toInt()}+",
                optionsList = listOfRating,
                labelExtractor = l,
                onOptionSelected = {
                    selectedMinRating = if (it == "All") {
                        0f
                    } else {
                        it.replace("+", "").toFloat()
                    }
                }
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp), contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onApplyClicked(selectedGenre.id.toString(), selectedMinRating)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(ButtonColor)
            ) {
                Text(
                    text = "Apply", fontSize = 16.sp, color = Color.White
                )
            }
        }
    }
}