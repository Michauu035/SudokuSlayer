package com.example.sudokuslayer.presentation.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.domain.data.SudokuGrid
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator

@Composable
fun SudokuBoard(numbers: Array<Int>, modifier: Modifier = Modifier) {
	val cellBorderColor = MaterialTheme.colorScheme.onBackground
	val subgridBorderColor = MaterialTheme.colorScheme.primary

	LazyVerticalGrid(
		columns = GridCells.Fixed(9),
		userScrollEnabled = false,
		modifier = modifier
			.aspectRatio(1f)
			.drawWithContent {
				drawContent()
				repeat(8) { index ->
					val color = if ((index + 1) % 3 == 0) subgridBorderColor else cellBorderColor
					val width = if ((index + 1) % 3 == 0) 2.dp.toPx() else 1.dp.toPx()

					drawLine(
						color = color,
						strokeWidth = width,
						start = Offset(size.width / 9 * (index + 1), 0f),
						end = Offset(size.width / 9 * (index + 1), size.height)
					)
					drawLine(
						color = color,
						strokeWidth = width,
						start = Offset(0f, size.height / 9 * (index + 1)),
						end = Offset(size.width, size.height / 9 * (index + 1))
					)
				}
			}
	) {
		items(numbers) { number ->
			Box() {
				SudokuCell(
					number = number,
					onClick = { }
				)
			}
		}
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun SudokuBoardPreview() {
	var grid = SudokuGrid()
//	return SudokuBoard(grid)
}