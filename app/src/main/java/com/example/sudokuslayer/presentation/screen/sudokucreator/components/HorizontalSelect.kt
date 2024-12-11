package com.example.sudokuslayer.presentation.screen.sudokucreator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalSelect(
	text: String,
	onLeftClick: () -> Unit,
	onRightClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		IconButton(onClick = onLeftClick) {
			Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, "Previous")
		}
		Text(text)
		IconButton(onClick = onRightClick) {
			Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, "Next")
		}
	}
}

@Preview
@Composable
private fun HorizontalSelectPreview() {
	HorizontalSelect(
		text = "Easy",
		onLeftClick = { },
		onRightClick = { }
	)
}