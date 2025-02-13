package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.model.CellAttributes
import com.example.sudoku.model.SudokuGrid

@Composable
fun SudokuBoard(
	sudoku: SudokuGrid,
	onCellClick: (Int, Int) -> Unit,
	modifier: Modifier = Modifier
) {
	val cellBorderColor = MaterialTheme.colorScheme.outlineVariant
	val subgridBorderColor = MaterialTheme.colorScheme.outline

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

			.border(
				width = 1.dp,
				color = MaterialTheme.colorScheme.outlineVariant,
				shape = RoundedCornerShape(8.dp)
			)

	) {
		items(sudoku.getArray()) { cell ->

			SudokuCell(
				cellData = cell,
				onClick = { onCellClick(cell.row, cell.col) },
				isGenerated = cell.attributes.contains(CellAttributes.GENERATED),
				isNumberHighlighted = cell.attributes.contains(CellAttributes.NUMBER_MATCH_HIGHLIGHTED),
				isRowColumnHighlighted = cell.attributes.contains(CellAttributes.ROW_COLUMN_HIGHLIGHTED),
				isSelected = cell.attributes.contains(CellAttributes.SELECTED),
				isBreakingRules = cell.attributes.contains(CellAttributes.RULE_BREAKING),
				isHintFocus = cell.attributes.contains(CellAttributes.HINT_FOCUS),
				isHintRevealed = cell.attributes.contains(CellAttributes.HINT_REVEALED)
			)

		}
	}
}

@Preview(
	showBackground = true
)
@Composable
private fun SudokuBoardPreview() {
	var grid = SudokuGrid()
	return SudokuBoard(
		grid,
		onCellClick = { row, col -> },
	)
}