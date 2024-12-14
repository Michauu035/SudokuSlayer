package com.example.sudokuslayer.presentation.screen.game.model

import com.example.sudokuslayer.domain.data.SudokuCellData
import com.example.sudokuslayer.domain.data.SudokuGrid

data class SudokuGameUiState(
	val sudoku: SudokuGrid = SudokuGrid(),
	val selectedCell: SudokuCellData? = null,
	val gameState: GameState = GameState.PLAYING,
	val inputMode: InputMode = InputMode.NUMBER,
	val cellsToRemove: Int = 20 // Temporarly for testing purposes
)

data class SudokuMove(
	val previousCellData: SudokuCellData,
	val newCellData: SudokuCellData
)

enum class GameState {
	PLAYING,
	VICTORY,
}

enum class InputMode {
	NUMBER,
	NOTE,
	COLOR
}
