package com.hocel.moviedb.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hocel.moviedb.ui.theme.TextColor

@Composable
fun FailedView(
    modifier: Modifier = Modifier,
    retryable: Boolean = false,
    onRetry: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(65.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = TextColor
            )
            Row(
                modifier = Modifier.padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Something went wrong, ",
                    color = TextColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                )
                if (retryable) {
                    Text(
                        modifier = Modifier.clickable(onClick = { onRetry?.invoke() }),
                        text = "retry",
                        color = TextColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600,
                        style = TextStyle(textDecoration = TextDecoration.Underline)
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun test(){
    FailedView(retryable = true)
}