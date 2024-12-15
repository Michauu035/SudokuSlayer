package com.example.sudokuslayer.presentation.screen.sudokucreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.domain.model.SudokuGrid
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random


data class SudokuCreatorUiState(
	val difficulty: SudokuDifficulty = SudokuDifficulty.EASY,
	val screenState: ScreenState = ScreenState.INITIAL,
	val sudoku: SudokuGrid? = null,
	val hasSavedData: Boolean = false
)

enum class ScreenState {
	INITIAL,
	LOADING,
	DONE
}

enum class SudokuDifficulty {
	EASY,
	MEDIUM,
	HARD,
	EXPERT;

	fun decrease(): SudokuDifficulty {
		return entries.toTypedArray().getOrElse(ordinal - 1) { this }
	}

	fun increase(): SudokuDifficulty {

		return entries.toTypedArray().getOrElse(ordinal + 1) { this }
	}
}

class SudokuCreatorViewModel(
	private val dataStoreRepository: SudokuDataStoreRepository
) : ViewModel() {
	private val _uiState = MutableStateFlow<SudokuCreatorUiState>(SudokuCreatorUiState())
	val uiState: StateFlow<SudokuCreatorUiState> = _uiState.asStateFlow()

	init {
		viewModelScope.launch {
			dataStoreRepository.sudokuGridProto.firstOrNull()?.let { sudoku ->
				val hasData = sudoku.getArray().isNotEmpty()
				_uiState.update {
					it.copy(hasSavedData = hasData)
				}
			} ?: _uiState.update {
				it.copy(hasSavedData = false)
			}
		}
	}

	sealed interface Event {
		data class ChangeDifficulty(val num: Int): Event
		data object NewGame: Event
		data object LoadSudoku: Event
	}

	fun onEvent(event: Event) {
		when (event) {
			is Event.ChangeDifficulty -> handleChangeDifficulty(event.num)
			Event.NewGame -> { handleNewGame() }
			Event.LoadSudoku -> { handleLoadGame() }
		}
	}

	private fun handleChangeDifficulty(num: Int) {
		viewModelScope.launch {
			_uiState.update {
				it.copy(
					difficulty = if (num < 0) it.difficulty.decrease() else it.difficulty.increase()
				)
			}
		}
	}

	private fun handleNewGame() {
		_uiState.update {
			it.copy(screenState = ScreenState.LOADING)
		}
		viewModelScope.launch(Dispatchers.IO) {
			val generator = ClassicSudokuGenerator()
			val cellsToRemove: Int = when(_uiState.value.difficulty) {
				SudokuDifficulty.EASY -> Random.nextInt(30, 40)
				SudokuDifficulty.MEDIUM -> Random.nextInt(41, 50)
				SudokuDifficulty.HARD -> Random.nextInt(51, 60)
				SudokuDifficulty.EXPERT -> Random.nextInt(61, 64)
			}

			val sudoku = generator.createSudoku(cellsToRemove)

			_uiState.update {
				it.copy(
					screenState = ScreenState.DONE,
					sudoku = sudoku
				)
			}

			dataStoreRepository.updateData(_uiState.value.sudoku!!)
		}
	}

	private fun handleLoadGame() {
		_uiState.update {
			it.copy(screenState = ScreenState.LOADING)
		}
		viewModelScope.launch {
			_uiState.update {
				it.copy(
					sudoku = dataStoreRepository.sudokuGridProto.first(),
					screenState = ScreenState.DONE
				)
			}
		}
	}
}

class SudokuCreatorViewModelFactory(private val dataStoreRepository: SudokuDataStoreRepository) :
		ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel> create(modelClass: Class<T>): T = SudokuCreatorViewModel(dataStoreRepository) as T
		}