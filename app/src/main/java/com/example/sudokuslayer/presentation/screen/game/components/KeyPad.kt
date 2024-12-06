package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudokuslayer.presentation.screen.game.components.keypadparts.ActionPad
import com.example.sudokuslayer.presentation.screen.game.components.keypadparts.NumberPad
import com.example.sudokuslayer.presentation.screen.game.components.keypadparts.VerticalActionPadLeft
import com.example.sudokuslayer.presentation.screen.game.components.keypadparts.VerticalActionPadRight

@Composable
fun KeyPad(
	onNumberClick: (Int) -> Unit,
	onClearClick: () -> Unit,
	onUndoClick: () -> Unit,
	onRedoClick: () -> Unit,
	onNumberSwitchClick: () -> Unit,
	onNoteSwitchClick: () -> Unit,
	onColorSwitchClick: () -> Unit,
	onHintClick: () -> Unit,
	onShowMistakesClick: () -> Unit,
	onResetClick: () -> Unit,
) {
	Row() {
		VerticalActionPadLeft(
			onHintClick = onHintClick,
			onShowMistakesClick = onShowMistakesClick,
			onResetClick = onResetClick
		)
		Column {
			NumberPad(
				onButtonClick = onNumberClick,
			)
			ActionPad(
				onClearClick = onClearClick,
				onUndoClick = onUndoClick,
				onRedoClick = onRedoClick
			)
		}
		VerticalActionPadRight(
			onNumberSwitchClick = onNumberSwitchClick,
			onNoteSwitchClick = onNoteSwitchClick,
			onColorSwitchClick = onColorSwitchClick
		)
	}
}

@Preview
@Composable
private fun KeyPadPreview() {
	KeyPad(
		onNumberClick = { },
		onClearClick = { },
		onUndoClick = { },
		onRedoClick = { },
		onNumberSwitchClick = { },
		onNoteSwitchClick = { },
		onColorSwitchClick = { },
		onHintClick = { },
		onShowMistakesClick = { },
		onResetClick = { }
	)
}