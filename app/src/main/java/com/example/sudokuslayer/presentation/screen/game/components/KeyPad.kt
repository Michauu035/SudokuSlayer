package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudokuslayer.presentation.screen.game.components.KeyPadParts.ActionPad
import com.example.sudokuslayer.presentation.screen.game.components.KeyPadParts.NumberPad

@Composable
fun KeyPad(
	onNumberClick: (Int) -> Unit,
	onClearClick: () -> Unit,
	onUndoClick: () -> Unit,
	onRedoClick: () -> Unit,
) {
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
}

@Preview
@Composable
private fun KeyPadPreview() {
	KeyPad(
		onNumberClick = { },
		onClearClick = { },
		onUndoClick = { },
		onRedoClick = { }
	)
}