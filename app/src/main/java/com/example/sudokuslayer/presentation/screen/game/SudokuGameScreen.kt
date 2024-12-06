package com.example.sudokuslayer.presentation.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel.Event
import com.example.sudokuslayer.presentation.screen.game.components.KeyPad
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard
import com.example.sudokuslayer.presentation.screen.game.components.VictoryDialog

@Composable
fun SudokuGameScreen(viewModel: SudokuGameViewModel, modifier: Modifier = Modifier) {
	val uiState = viewModel.uiState.collectAsState().value
	val sudoku = uiState.sudoku

	LaunchedEffect(Unit) {
		viewModel.onEvent(Event.GenerateSudoku)
	}

	VictoryDialog(
		isVisible = uiState.gameState == GameState.VICTORY,
		onDismissRequest = { viewModel.onEvent(Event.DismissVictoryDialog) }
	)


	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row {
			TextField(
				value = uiState.cellsToRemove.toString(),
				label = {
					Text("Cells to remove")
				},
				onValueChange = { num -> viewModel.onEvent(Event.InputCellsToRemove(if (num.isNotEmpty()) num.toInt() else 1)) },
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
				)
			)
			Button(onClick = { viewModel.onEvent(Event.GenerateSudoku) }) {
				Text("New sudoku")
			}
		}
		SudokuBoard(
			sudoku = sudoku,
			onCellClick = { row, col -> viewModel.onEvent(Event.SelectCell(row, col)) },
		)
		KeyPad(
			onNumberClick = { viewModel.onEvent(Event.InputNumber(it)) },
			onClearClick = { viewModel.onEvent(Event.ClearCell) },
			onUndoClick = { viewModel.onEvent(Event.Undo) },
			onRedoClick = { viewModel.onEvent(Event.Redo) },
			onNumberSwitchClick = { },
			onNoteSwitchClick = { },
			onColorSwitchClick = { },
			onHintClick = { viewModel.onEvent(Event.ShowHint) },
			onShowMistakesClick = { viewModel.onEvent(Event.ShowMistakes) },
			onResetClick = { viewModel.onEvent(Event.Reset) },
		)
	}
}

@Preview
@Composable
private fun SudokuGameScreenPreview() {
	val viewModel = SudokuGameViewModel()
	SudokuGameScreen(viewModel)
}