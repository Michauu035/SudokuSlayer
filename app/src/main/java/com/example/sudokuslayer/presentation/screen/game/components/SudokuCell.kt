package com.example.sudokuslayer.presentation.screen.game.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.data.SudokuCellData

@Composable
fun SudokuCell(
	cellData: SudokuCellData,
	onClick: () -> Unit,
	isGenerated: Boolean,
	modifier: Modifier = Modifier,
	selected: Boolean = false,
) {
	val bgColor = if (selected) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow
	val weight = if (isGenerated) FontWeight.Bold else FontWeight.Normal
	val textColor = if (isGenerated) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface

	Box(
		modifier = Modifier
			.background(bgColor)
			.padding(1.dp)
			.aspectRatio(1f)
			.clickable(
				onClick = onClick
			),
		contentAlignment = Alignment.Center

	) {
		Text(
			text = if (cellData.number != 0) cellData.number.toString() else "",
			modifier = Modifier
				.fillMaxSize()
				.wrapContentHeight(align = Alignment.CenterVertically),
			style = MaterialTheme.typography.bodyLarge,
			textAlign = TextAlign.Center,
			fontWeight = weight,
			color = textColor
		)
	}
}
