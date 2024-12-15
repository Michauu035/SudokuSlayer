package com.example.sudokuslayer.presentation.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.domain.model.CellAttributes
import com.example.sudokuslayer.domain.model.ClassicSudokuSolver
import com.example.sudokuslayer.domain.model.SudokuCellData
import com.example.sudokuslayer.domain.model.SudokuGrid
import com.example.sudokuslayer.presentation.screen.game.model.GameState
import com.example.sudokuslayer.presentation.screen.game.model.InputMode
import com.example.sudokuslayer.presentation.screen.game.model.SudokuGameUiState
import com.example.sudokuslayer.presentation.screen.game.model.SudokuMove
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
	private val lastMoves: ArrayDeque<SudokuMove> = ArrayDeque()
	private val futureMoves: ArrayDeque<SudokuMove> = ArrayDeque()

	sealed interface Event {
		data class SelectCell(val row: Int, val col: Int) : Event
		data class InputNumber(val number: Int) : Event
		data class SwitchInputMode(val inputMode: InputMode) : Event
		data object ClearCell : Event
		data object Undo : Event
		data object Redo : Event
		data object ShowHint : Event
		data object ShowMistakes : Event
		data object ResetGame : Event
		data object ResetNotes : Event
		data object DismissVictoryDialog : Event
	}

	fun onEvent(event: Event) {
		when (event) {
			is Event.SelectCell -> selectCell(event.row, event.col)
			is Event.InputNumber -> inputNumber(event.number)
			is Event.ClearCell -> inputNumber(0)
			is Event.Undo -> undoLastMove()
			is Event.Redo -> redoLastMove()
			is Event.ResetGame -> resetGame()
			is Event.ShowHint -> {}
			is Event.ShowMistakes -> {}
			is Event.DismissVictoryDialog -> handleDismissVictoryDialog()
			is Event.SwitchInputMode -> switchInputMode(event.inputMode)
			is Event.ResetNotes -> resetNotes()
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
		viewModelScope.launch {
			val updatedSudoku = _uiState.value.sudoku.clone()
			val lastSelected = _uiState.value.selectedCell
			lastSelected?.let {
				updatedSudoku.removeAttribute(it.row, it.col, CellAttributes.SELECTED)
				if (updatedSudoku[row, col].number != 0)
					updatedSudoku.clearHighlightedCells()
			}

			updatedSudoku.addAttribute(row, col, CellAttributes.SELECTED)
			val currentlySelected = updatedSudoku[row, col]
			updatedSudoku.highlightMatchingCells(currentlySelected.number)

			_uiState.update {
				it.copy(
					sudoku = updatedSudoku,
					selectedCell = currentlySelected
				)
			}
		}
	}

	private fun inputNumber(number: Int) {
		viewModelScope.launch {
			val currentState = _uiState.value
			val updatedSudoku = currentState.sudoku.clone()
			val selectedCell: SudokuCellData = currentState.selectedCell ?: return@launch

			if (selectedCell.attributes.contains(CellAttributes.GENERATED)) return@launch
			val backupCell = updatedSudoku[selectedCell.row, selectedCell.col]

			when (currentState.inputMode) {
				InputMode.NUMBER -> handleNumberInput(number, updatedSudoku, selectedCell)

				InputMode.NOTE -> handleNoteInput(number, updatedSudoku, selectedCell)

				InputMode.COLOR -> { }
			}

			saveMoveAndUpdateState(backupCell, updatedSudoku)
		}
	}

	private fun resetGame() {
		viewModelScope.launch {
			var updatedSudoku = _uiState.value.sudoku.clone()
			updatedSudoku.resetGame()
			_uiState.update {
				it.copy(
					sudoku = updatedSudoku
				)
			}

			dataStoreRepository.updateData(updatedSudoku)
		}
	}

	private fun resetNotes() {
		viewModelScope.launch {
			val updatedSudoku = _uiState.value.sudoku.clone()
			updatedSudoku.clearNotes()
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

	private fun handleMove(
		moveStack: ArrayDeque<SudokuMove>,
		targetStack: ArrayDeque<SudokuMove>
	) {
		if (moveStack.isEmpty())
			return

		viewModelScope.launch {
			val (previousCellData, newCellData) = moveStack.removeLast()
			val updatedSudoku = _uiState.value.sudoku.clone()

			Log.d("", "move: ${previousCellData.number}")
			updatedSudoku.replaceCell(previousCellData.row, previousCellData.col, previousCellData)
			targetStack.add(SudokuMove(newCellData, previousCellData))

			_uiState.update { it.copy(sudoku = updatedSudoku) }

			dataStoreRepository.updateCell(
				row = previousCellData.row,
				col = previousCellData.col,
				newCellData = updatedSudoku[previousCellData.row, previousCellData.col]
			)
		}
	}

	private fun undoLastMove() = handleMove(lastMoves, futureMoves)

	private fun redoLastMove() = handleMove(futureMoves, lastMoves)

	private fun handleNumberInput(number: Int, sudoku: SudokuGrid, cellData: SudokuCellData) {
		val (row, col) = cellData
		if (number == 0) {
			sudoku[row, col] = 0
			sudoku.clearCornerNotes(row, col)
		} else {
			sudoku[row, col] = if (cellData.number == number) 0 else number
			sudoku.clearHighlightedCells()
			sudoku.highlightMatchingCells(number)
		}
	}

	private fun handleNoteInput(number: Int, sudoku: SudokuGrid, cellData: SudokuCellData) {
		val (row, col) = cellData
		if (number == 0) {
			sudoku[row, col] = 0
			sudoku.clearCornerNotes(row, col)
		} else if (number in cellData.cornerNotes) {
			sudoku.removeCornerNote(row, col, number)
		} else {
			sudoku.addCornerNote(row, col, number)
		}
	}

	private fun saveMoveAndUpdateState(selectedCell: SudokuCellData, updatedSudoku: SudokuGrid) {
		val (row, col) = selectedCell
		lastMoves.add(
			SudokuMove(
				previousCellData = selectedCell,
				newCellData = updatedSudoku[row, col]
			)
		)

		Log.d("", "move: ${selectedCell.number}")
		_uiState.update {
			it.copy(sudoku = updatedSudoku)
		}

		viewModelScope.launch {
			dataStoreRepository.updateCell(row, col, updatedSudoku[row, col])
		}

		if (updatedSudoku.getEmptyCellsCount() == 0) {
			handleAllCellsFilled()
		}
	}
}

class SudokuGameViewModelFactory(
	private val dataStoreRepository: SudokuDataStoreRepository
) : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel> create(modelClass: Class<T>): T =
		SudokuGameViewModel(dataStoreRepository) as T
}