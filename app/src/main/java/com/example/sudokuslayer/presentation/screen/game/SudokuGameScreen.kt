package com.example.sudokuslayer.presentation.screen.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel.Event
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard

@Composable
fun SudokuGameScreen(viewModel: SudokuGameViewModel, modifier: Modifier = Modifier) {
	val uiState = viewModel.uiState.collectAsState().value
	val sudoku = uiState.sudoku

	LaunchedEffect(Unit) {
		viewModel.onEvent(Event.GenerateSudoku)
	}

	Column(modifier = modifier) {
		Button(onClick = { viewModel.onEvent(Event.GenerateSudoku) }) {
			Text("New sudoku")
		}
		SudokuBoard(
			sudoku = sudoku,
			onCellClick = { row, col -> viewModel.onEvent(Event.SelectCell(row, col)) },
		)
	}
}

@Preview
@Composable
private fun SudokuGameScreenPreview() {
	val viewModel = SudokuGameViewModel()
	SudokuGameScreen(viewModel)
}