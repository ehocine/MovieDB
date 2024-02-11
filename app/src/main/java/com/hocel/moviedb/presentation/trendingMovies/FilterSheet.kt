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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hocel.moviedb.data.models.genres.Genres
import com.hocel.moviedb.ui.theme.BackgroundColor
import com.hocel.moviedb.ui.theme.ButtonColor
import com.hocel.moviedb.ui.theme.TextColor
import com.hocel.moviedb.utils.DropDownOptions

@Composable
fun FilterSheetContent(list: Genres) {

    val scope = rememberCoroutineScope()

    val listOfGenres = list.genres.map { it.name }
    val l = listOfGenres.toMutableList().add(0, "All")

    val listOfRating = listOf(
        "All",
        "9+",
        "8+",
        "7+",
        "6+",
        "5+",
        "4+",
        "3+",
        "2+",
        "1+"
    )

    Column(Modifier.background(BackgroundColor)) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Genre",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            DropDownOptions(
                label = listOfGenres.first(),
                optionsList = listOfGenres,
                onOptionSelected = {
//                    mainViewModel.selectedGenre.value = it
                })
        }
        Column(Modifier.padding(15.dp)) {
            Text(
                text = "Minimum rating",
                fontSize = typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = TextColor
            )
            DropDownOptions(
                label = listOfRating.first(),
                optionsList = listOfRating,
                onOptionSelected = {
//                    mainViewModel.selectedMinimumRating.value =
//                        it.replace("+", "")
                })
        }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(ButtonColor)
            ) {
                Text(
                    text = "Apply",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}