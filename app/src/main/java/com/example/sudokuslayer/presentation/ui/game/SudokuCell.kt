package com.example.sudokuslayer.presentation.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SudokuCell(
	number: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	selected: Boolean = false
) {
	Box(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surfaceContainerLow)
			.padding(1.dp)
			.aspectRatio(1f)
			.clickable(
				onClick = onClick
			),
		contentAlignment = Alignment.Center

	) {
		Text(
			text = if (number != 0) number.toString() else "",
			modifier = Modifier
				.fillMaxSize()
				.wrapContentHeight(align = Alignment.CenterVertically),
			style = MaterialTheme.typography.bodyLarge,
			textAlign = TextAlign.Center
		)
	}
}
