package com.example.sudokuslayer.presentation.screen.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel.Event
import com.example.sudokuslayer.presentation.screen.game.components.KeyPad
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard

@Composable
fun SudokuGameScreen(viewModel: SudokuGameViewModel, modifier: Modifier = Modifier) {
	val uiState = viewModel.uiState.collectAsState().value
	val sudoku = uiState.sudoku

	LaunchedEffect(Unit) {
		viewModel.onEvent(Event.GenerateSudoku)
	}

	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Button(onClick = { viewModel.onEvent(Event.GenerateSudoku) }) {
			Text("New sudoku")
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