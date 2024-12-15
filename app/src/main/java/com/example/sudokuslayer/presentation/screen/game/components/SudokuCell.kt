package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudokuslayer.domain.data.SudokuCellData

@Composable
fun SudokuCell(
	cellData: SudokuCellData,
	onClick: () -> Unit,
	isGenerated: Boolean = false,
	isHighlighted: Boolean = false,
	selected: Boolean = false,
) {
	val bgColor =
		if (selected) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.surfaceContainerLow
	val weight = if (isGenerated) FontWeight.Bold else FontWeight.Normal
	val textColor =
		if (isGenerated) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.tertiary
	val highlightedColor = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp)

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
		Surface(
			modifier = Modifier.padding(2.dp),
			color = if (isHighlighted && !selected) highlightedColor else bgColor,
			shape = if (isHighlighted && !selected) CircleShape else RoundedCornerShape(0.1.dp)
		) {
			if (cellData.number != 0) {
				Text(
					text = cellData.number.toString(),
					modifier = Modifier
						.fillMaxSize()
						.wrapContentHeight(align = Alignment.CenterVertically),
					style = MaterialTheme.typography.bodyLarge,
					textAlign = TextAlign.Center,
					fontWeight = weight,
					color = textColor
				)
			} else {
				LazyVerticalGrid(
					columns = GridCells.Fixed(3),
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.SpaceBetween
				) {
					items(cellData.cornerNotes.toList().sorted()) { note ->
						Text(
							text = note.toString(),
							modifier = Modifier
								.fillMaxSize()
								.wrapContentHeight(align = Alignment.CenterVertically),
							fontSize = 10.sp,
							lineHeight = 10.sp,
							textAlign = TextAlign.Center,
							fontWeight = weight,
							color = textColor
						)
					}
				}
			}

		}
	}
}

@Preview(name = "Empty cell", group = "SudokuCellPreview")
@Composable
private fun SudokuEmptyCellPreview() {
	SudokuCell(
		cellData = SudokuCellData(0, 0),
		onClick = { },
	)
}

@Preview(name = "Cell with generated number", group = "SudokuCellPreview")
@Composable
private fun SudokuGeneratedCellPreview() {
	SudokuCell(
		cellData = SudokuCellData(0, 0, 4),
		onClick = { },
		isGenerated = true
	)
}


@Preview(name = "Cell with users number", group = "SudokuCellPreview")
@Composable
private fun SudokuFilledCellPreview() {
	SudokuCell(
		cellData = SudokuCellData(0, 0, 6),
		onClick = { },
		isGenerated = false
	)
}

@Preview(name = "Cell with notes", group = "SudokuCellPreview")
@Composable
private fun SudokuNotesCellPreview() {
	SudokuCell(
		cellData = SudokuCellData(
			0, 0, 0,
			cornerNotes = mutableSetOf(1, 2, 5, 7, 9),
		),
		onClick = { },
	)
}
