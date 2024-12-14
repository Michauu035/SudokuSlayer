package com.example.sudokuslayer.presentation.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.model.ClassicSudokuSolver
import com.example.sudokuslayer.presentation.screen.game.model.GameState
import com.example.sudokuslayer.presentation.screen.game.model.InputMode
import com.example.sudokuslayer.presentation.screen.game.model.SudokuGameUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SudokuGameViewModel(
	private val dataStoreRepository: SudokuDataStoreRepository
) : ViewModel() {
	private val _uiState = MutableStateFlow<SudokuGameUiState>(SudokuGameUiState())
	val uiState: StateFlow<SudokuGameUiState> = _uiState
	private val _isLoading = MutableStateFlow(false)
	val isLoading = _isLoading
		.onStart { loadData() }
		.stateIn(
			viewModelScope,
			SharingStarted.WhileSubscribed(5000L),
			false
		)

	sealed interface Event {
		data class SelectCell(val row: Int, val col: Int) : Event
		data class InputNumber(val number: Int) : Event
		data object ClearCell : Event
		data object Undo : Event
		data object Redo : Event
		data object ShowHint : Event
		data object ShowMistakes : Event
		data object Reset : Event
		data object DismissVictoryDialog : Event
		data object NumberSwitch : Event
		data object NoteSwitch : Event
		data object ColorSwitch : Event
	}

	fun onEvent(event: Event) {
		when (event) {
			is Event.SelectCell -> {
				selectCell(event.row, event.col)
			}

			is Event.InputNumber -> {
				inputNumber(event.number)
			}

			is Event.ClearCell -> {
				inputNumber(0)
			}

			is Event.Undo -> {}
			is Event.Redo -> {}
			is Event.Reset -> {
				resetGame()
			}

			is Event.ShowHint -> {}
			is Event.ShowMistakes -> {}
			is Event.DismissVictoryDialog -> {
				handleDismissVictoryDialog()
			}

			Event.ColorSwitch -> {
				switchInputMode(InputMode.COLOR)
			}

			Event.NoteSwitch -> {
				switchInputMode(InputMode.NOTE)
			}

			Event.NumberSwitch -> {
				switchInputMode(InputMode.NUMBER)
			}
		}
	}

	private fun loadData() {
		viewModelScope.launch(Dispatchers.IO) {
			_isLoading.value = true
			dataStoreRepository.sudokuGridProto.firstOrNull()?.let { gridData ->
				_uiState.update {
					it.copy(
						sudoku = gridData
					)
				}
			} ?: throw Exception("Proto Sudoku not found!")
			_isLoading.value = false
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
		val currentState = _uiState.value
		val updatedSudoku = currentState.sudoku.clone()
		val selectedCell = currentState.selectedCell

		if (selectedCell == null || selectedCell.attributes.contains(CellAttributes.GENERATED)) {
			return
		}

		val (row, col) = selectedCell
		val cell = updatedSudoku[row, col]

		when (currentState.inputMode) {
			InputMode.NUMBER -> {
				if (number == 0) {
					updatedSudoku[row, col] = 0
					updatedSudoku.clearCornerNotes(row, col)
				} else {
					updatedSudoku[row, col] = if (cell.number == number) 0 else number
				}
			}

			InputMode.NOTE -> {
				if (number == 0) {
					updatedSudoku[row, col] = 0
					updatedSudoku.clearCornerNotes(row, col)
				} else if (number in cell.cornerNotes) {
					updatedSudoku.removeCornerNote(row, col, number)
				} else {
					updatedSudoku.addCornerNote(row, col, number)
				}
			}

			InputMode.COLOR -> { /* TODO */
			}
		}

		_uiState.update {
			it.copy(sudoku = updatedSudoku)
		}

		viewModelScope.launch {
			dataStoreRepository.updateCell(
				row = row,
				col = col,
				newCellData = updatedSudoku[row, col]
			)
		}

		if (updatedSudoku.getEmptyCellsCount() == 0) {
			handleAllCellsFilled()
		}
	}

	private fun resetGame() {
		viewModelScope.launch {
			var updatedSudoku = _uiState.value.sudoku.clone()
			val grid =
				updatedSudoku.getArray()
					.map {
						if (it.attributes.contains(CellAttributes.GENERATED)) it else it.copy(
							number = 0,
							cornerNotes = emptySet()
						)
					}
					.toTypedArray()

			updatedSudoku.set(grid)
			_uiState.update {
				it.copy(
					sudoku = updatedSudoku
				)
			}

			dataStoreRepository.updateData(updatedSudoku)
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
	}

	private fun switchInputMode(mode: InputMode) {
		_uiState.update {
			it.copy(
				inputMode = mode
			)
		}
	}
}

class SudokuGameViewModelFactory(
	private val dataStoreRepository: SudokuDataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel> create(modelClass: Class<T>): T =
		SudokuGameViewModel(dataStoreRepository) as T
}