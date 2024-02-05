package com.example.bobjimmy.ui.theme.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.bobjimmy.ui.theme.BobJimmyTheme

@Composable
fun RegularText(
    text:String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.displayMedium,
        modifier = modifier,
        color = Color.Black
    )
}

@Preview(showBackground = true)
@Composable
fun RegularTextPreview() {
    BobJimmyTheme {
        RegularText("Test Regular Text")
    }
}
