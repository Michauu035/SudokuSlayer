package com.example.sudokuslayer.presentation.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.data.SudokuCellData
import com.example.sudokuslayer.domain.data.SudokuGrid
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator
import com.example.sudokuslayer.domain.model.ClassicSudokuSolver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SudokuGameUiState(
	val sudoku: SudokuGrid = SudokuGrid(),
	val selectedCell: SudokuCellData? = null,
	val gameState: GameState = GameState.PLAYING,
	val cellsToRemove: Int = 20 // Temporarly for testing purposes
)

enum class GameState {
	PLAYING,
	VICTORY,
}

class SudokuGameViewModel : ViewModel(){
	private val _uiState = MutableStateFlow<SudokuGameUiState>(SudokuGameUiState())
	val uiState: StateFlow<SudokuGameUiState> = _uiState
	val generator = ClassicSudokuGenerator()



	sealed interface Event {
		data object GenerateSudoku: Event
		data class SelectCell(val row: Int, val col: Int): Event
		data class InputNumber(val number: Int): Event
		data object ClearCell: Event
		data object Undo: Event
		data object Redo: Event
		data object ShowHint: Event
		data object ShowMistakes: Event
		data object Reset: Event
		data object DismissVictoryDialog: Event
		data class InputCellsToRemove(val number: Int): Event
	}

	fun onEvent(event: Event) {
		when(event) {
			is Event.GenerateSudoku -> {
				regenerateSudoku()
			}
			is Event.SelectCell -> {
				selectCell(event.row, event.col)
			}
			is Event.InputNumber -> {
				inputNumber(event.number)
			}
			is Event.ClearCell -> {
				inputNumber(0)
			}
			is Event.Undo -> { }
			is Event.Redo -> { }
			is Event.Reset -> {
				resetGame()
			}
			is Event.ShowHint -> { }
			is Event.ShowMistakes -> { }
			is Event.DismissVictoryDialog -> {
				handleDismissVictoryDialog()
			}
			is Event.InputCellsToRemove -> {
				_uiState.update {
					it.copy(
						cellsToRemove = event.number
					)
				}
			}
		}
	}

	private fun regenerateSudoku(){
		val cellsToRemove = _uiState.value.cellsToRemove
		if (cellsToRemove > 0 && cellsToRemove < 60) {
			viewModelScope.launch(Dispatchers.Default) {
				_uiState.update {
					it.copy(
						sudoku = generator.createSudoku(cellsToRemove)
					)
				}
			}
		}
	}

	private fun selectCell(row: Int, col: Int) {
		val updatedSudoku = _uiState.value.sudoku.clone()
		val lastSelected = _uiState.value.selectedCell
		lastSelected?.let {
			updatedSudoku.removeAttribute(it.row, it.col, CellAttributes.SELECTED)
		}
		updatedSudoku.addAttribute(row, col, CellAttributes.SELECTED)
		_uiState.update {
			it.copy(
				sudoku = updatedSudoku,
				selectedCell = updatedSudoku[row, col]
			)
		}
	}

	private fun inputNumber(number: Int) {
		val updatedSudoku = _uiState.value.sudoku.clone()
		val selectedCell = _uiState.value.selectedCell
		if (selectedCell != null) {
			if (!selectedCell.attributes.contains(CellAttributes.GENERATED)) {
				updatedSudoku[selectedCell.row, selectedCell.col] = number
				_uiState.update {
					it.copy(
						sudoku = updatedSudoku
					)
				}
			}
		}
		if (_uiState.value.sudoku.getEmptyCellsCount() == 0) {
			handleAllCellsFilled()
		}
	}

	private fun resetGame() {
		var updatedSudoku = _uiState.value.sudoku.clone()
		val grid  =
			updatedSudoku.getArray().map { if (it.attributes.contains(CellAttributes.GENERATED)) it else it.copy(number = 0) }.toTypedArray()
		updatedSudoku.set(grid)
		_uiState.update {
			it.copy(
				sudoku = updatedSudoku
			)
		}
	}

	private fun handleAllCellsFilled() {
		val result = ClassicSudokuSolver.isValidSolution(_uiState.value.sudoku)
		if (result) {
			_uiState.update {
				it.copy(
					gameState = GameState.VICTORY
				)
			}
		}
	}

	private fun handleDismissVictoryDialog() {
		_uiState.update {
			it.copy(
				gameState = GameState.PLAYING
			)
		}
		regenerateSudoku()
	}
}