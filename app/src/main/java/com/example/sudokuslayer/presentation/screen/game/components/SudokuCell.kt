package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sudoku.model.SudokuCellData
import com.example.sudokuslayer.presentation.ui.theme.extendedColorScheme
import kotlinx.coroutines.delay

@Composable
fun SudokuCell(
	cellData: SudokuCellData,
	onClick: () -> Unit,
	isGenerated: Boolean = false,
	isNumberHighlighted: Boolean = false,
	isRowColumnHighlighted: Boolean = false,
	isSelected: Boolean = false,
	isBreakingRules: Boolean = false,
	isHintFocus: Boolean = false,
	isHintRevealed: Boolean = false
) {
	val baseBgColor = MaterialTheme.colorScheme.background
	val selectedBgColor = MaterialTheme.colorScheme.surfaceTint
	val highlightedRowColumnColor = MaterialTheme.colorScheme.surface
	val ruleBreakingColor = MaterialTheme.colorScheme.error
	val hintFocusColor = MaterialTheme.extendedColorScheme.yellow.colorContainer

	val bgColor = when {
		isSelected -> selectedBgColor
		isRowColumnHighlighted -> highlightedRowColumnColor
		else -> baseBgColor
	}

	val fontWeight = if (isGenerated) FontWeight.Bold else FontWeight.Normal

	val isNumberHighlightApplicable = (isBreakingRules || isHintRevealed || isNumberHighlighted) && cellData.number != 0

	val circleColor = when {
		isBreakingRules -> ruleBreakingColor
		isHintRevealed -> hintFocusColor
		isNumberHighlightApplicable -> MaterialTheme.colorScheme.tertiaryContainer
		else -> bgColor
	}

	val textColor = when {
		isHintRevealed -> MaterialTheme.extendedColorScheme.yellow.onColorContainer
		isHintFocus -> hintFocusColor
		isBreakingRules -> MaterialTheme.colorScheme.onError
		isNumberHighlightApplicable -> MaterialTheme.colorScheme.onTertiaryContainer
		isGenerated -> MaterialTheme.colorScheme.onSurface
		else -> MaterialTheme.colorScheme.secondary
	}

	val cornerCellModifier = when {
		cellData.row == 0 && cellData.col == 0 -> Modifier.clip(
			RoundedCornerShape(
				8.dp,
				0.dp,
				0.dp,
				0.dp
			)
		) // Top left
		cellData.row == 0 && cellData.col == 8 -> Modifier.clip(
			RoundedCornerShape(
				0.dp,
				8.dp,
				0.dp,
				0.dp
			)
		) // Top right
		cellData.row == 8 && cellData.col == 0 -> Modifier.clip(
			RoundedCornerShape(
				0.dp,
				0.dp,
				8.dp,
				0.dp
			)
		) // Bottom left
		cellData.row == 8 && cellData.col == 8 -> Modifier.clip(
			RoundedCornerShape(
				0.dp,
				0.dp,
				0.dp,
				8.dp
			)
		) // Bottom right
		else -> Modifier
	}



	val combinedModifier = cornerCellModifier
		.aspectRatio(1f)
		.clickable(onClick = onClick)

	Surface(
		color = bgColor,
		modifier = combinedModifier,
	) {
		if (isHintFocus) {
			val animationDuration = 5000
			val animationFrequency = 10
			val infiniteTransition = rememberInfiniteTransition()
			val animationColor by infiniteTransition.animateColor(
				initialValue = hintFocusColor,
				targetValue = MaterialTheme.colorScheme.background,
				animationSpec = infiniteRepeatable(
					animation = tween(animationDuration/animationFrequency, easing = LinearEasing),
					repeatMode = RepeatMode.Reverse
				)
			)

			var showAnimation by remember { mutableStateOf(true) }

			LaunchedEffect(Unit) {
				delay(animationDuration.toLong())
				showAnimation = false
			}

			if (showAnimation) {
				Canvas(modifier = Modifier.fillMaxSize().padding(2.dp)) {
					val strokeWidth = 4.dp.toPx()
					drawRect(
						color = animationColor,
						size = size,
						style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
					)
				}
			}

		}
		if (isNumberHighlightApplicable) {
			ElevatedSurface(
				color = circleColor,
				shadowElevation = 0.dp,
				modifier = Modifier
					.fillMaxSize()
					.padding(2.dp)
			) {

				CellContent(
					cellData = cellData,
					textColor = textColor,
					fontWeight = fontWeight
				)
			}
		} else {
			CellContent(
				cellData = cellData,
				textColor = textColor,
				fontWeight = fontWeight
			)
		}
	}
}

@Composable
fun ElevatedSurface(
	color: Color,
	shadowElevation: Dp = 6.dp,
	tonalElevation: Dp = 6.dp,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	Surface(
		color = color,
		shape = CircleShape,
		shadowElevation = shadowElevation,
		tonalElevation = tonalElevation,
		modifier = modifier,
	) {
		content()
	}
}


@Composable
fun CellContent(
	cellData: SudokuCellData,
	textColor: Color,
	fontWeight: FontWeight,
) {
	if (cellData.number != 0) {
		Text(
			text = cellData.number.toString(),
			modifier = Modifier
				.fillMaxSize()
				.wrapContentHeight(align = Alignment.CenterVertically),
			style = MaterialTheme.typography.bodyLarge,
			textAlign = TextAlign.Center,
			fontWeight = fontWeight,
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
					fontWeight = fontWeight,
					color = textColor
				)
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
