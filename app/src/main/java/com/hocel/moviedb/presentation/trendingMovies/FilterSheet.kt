package com.hocel.moviedb.presentation.trendingMovies

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

@Composable
fun FilterSheetContent(
    mSelectedGenre: Genre,
    minRating: Float,
    genresList: List<Genre>,
    onApplyClicked: (genreId: String, minRating: Float) -> Unit
) {
    val list = listOf(Genre(0, "All")) + genresList

    val listOfRating = listOf(
        "All", "9+", "8+", "7+", "6+", "5+", "4+", "3+", "2+", "1+"
    )

    var selectedGenre by remember { mutableStateOf(mSelectedGenre) }
    var selectedMinRating by remember { mutableFloatStateOf(minRating) }

    Column(Modifier.background(BackgroundColor)) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Genre",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            val labelExtractor: (Genre) -> String = { it.name }
            DropDownOptions(
                defaultValue = selectedGenre.name,
                optionsList = list,
                labelExtractor = labelExtractor,
                onOptionSelected = {
                    selectedGenre = it
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