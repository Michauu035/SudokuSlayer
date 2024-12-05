package com.example.sudokuslayer.presentation.screen.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.data.SudokuCellData
import com.example.sudokuslayer.domain.data.SudokuGrid
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SudokuGameUiState(
	val sudoku: SudokuGrid = SudokuGrid(),
	val selectedCell: SudokuCellData? = null
)

class SudokuGameViewModel : ViewModel(){
	private val _uiState = MutableStateFlow<SudokuGameUiState>(SudokuGameUiState())
	val uiState: StateFlow<SudokuGameUiState> = _uiState
	val generator = ClassicSudokuGenerator()

	sealed interface Event {
		data object GenerateSudoku: Event
		data class SelectCell(val row: Int, val col: Int): Event
	}

	fun onEvent(event: Event) {
		when(event) {
			is Event.GenerateSudoku -> {
				regenerateSudoku()
			}
			is Event.SelectCell -> {
				selectCell(event.row, event.col)
			}
		}
	}

	private fun regenerateSudoku(){
		viewModelScope.launch(Dispatchers.Default) {
			_uiState.update {
				it.copy(
					sudoku = generator.createSudoku(53)
				)
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
}