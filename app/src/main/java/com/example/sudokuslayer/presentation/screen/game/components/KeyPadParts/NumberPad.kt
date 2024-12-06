package com.example.sudokuslayer.presentation.screen.game.components.KeyPadParts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(
	onButtonClick: (Int) -> Unit,
	modifier: Modifier = Modifier
) {
	val keyboardNumbers = listOf(
		listOf(1, 2, 3),
		listOf(4, 5, 6),
		listOf(7, 8, 9),
	)

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
		onButtonClick = { }
	)
}