package com.example.sudokuslayer.presentation.screen.game.components.keypadparts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.presentation.screen.game.model.InputMode
import com.example.sudokuslayer.presentation.ui.theme.extendedColorScheme

@Composable
fun NumberPad(
	onButtonClick: (Int) -> Unit,
	inputMode: InputMode
) {
	val keyboardNumbers = listOf(
		listOf(1, 2, 3),
		listOf(4, 5, 6),
		listOf(7, 8, 9),
	)

	val keyColor = when(inputMode) {
		InputMode.NUMBER -> MaterialTheme.extendedColorScheme.lavender.colorContainer
		InputMode.NOTE -> MaterialTheme.extendedColorScheme.pink.colorContainer
		InputMode.COLOR -> MaterialTheme.extendedColorScheme.rosewater.colorContainer
	}

	val textColor = when(inputMode) {
		InputMode.NUMBER -> MaterialTheme.extendedColorScheme.lavender.onColorContainer
		InputMode.NOTE -> MaterialTheme.extendedColorScheme.pink.onColorContainer
		InputMode.COLOR -> MaterialTheme.extendedColorScheme.rosewater.onColorContainer
	}

	Column(
		modifier = Modifier.padding(8.dp)
	) {
		for (row in keyboardNumbers) {
			Row(
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically
			) {
				for (number in row) {
					KeyPadItem(
						text = number.toString(),
						onClick = { onButtonClick(number) },
						bgColor = keyColor,
						textColor = textColor
					)
					Spacer(modifier = Modifier.width(8.dp))
				}
			}
			Spacer(modifier = Modifier.height(8.dp))
		}
	}
}

@Preview
@Composable
private fun NumberPadPreview() {
	NumberPad(
		onButtonClick = { },
		inputMode = InputMode.NUMBER
	)
}