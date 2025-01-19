package com.example.sudokuslayer.presentation.screen.game.model

import com.example.sudoku.model.SudokuCellData
import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.solver.Hint
import com.example.sudokuslayer.presentation.screen.sudokucreator.SudokuDifficulty

data class SudokuGameUiState(
	val sudoku: SudokuGrid = SudokuGrid(),
	val selectedCell: Pair<Int, Int>? = null,
	val gameState: GameState = GameState.PLAYING,
	val inputMode: InputMode = InputMode.NUMBER,
	val difficulty: SudokuDifficulty = SudokuDifficulty.EASY,
	val hint: Hint? = null,
	val hintLogs: List<String> = emptyList()
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
