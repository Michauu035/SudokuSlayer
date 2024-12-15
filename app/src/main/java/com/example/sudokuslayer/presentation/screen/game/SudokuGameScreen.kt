package com.example.sudokuslayer.presentation.screen.game

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.core.rememberDialogState
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.data.datastore.sudokuGridDataStore
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel.Event
import com.example.sudokuslayer.presentation.screen.game.components.HintsDialog
import com.example.sudokuslayer.presentation.screen.game.components.KeyPad
import com.example.sudokuslayer.presentation.screen.game.components.ResetDialog
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard
import com.example.sudokuslayer.presentation.screen.game.components.VictoryDialog
import com.example.sudokuslayer.presentation.screen.game.model.GameState
import com.example.sudokuslayer.presentation.screen.game.model.InputMode

@Composable
fun SudokuGameScreen(
	context: Context,
	viewModel: SudokuGameViewModel = viewModel(
		factory = SudokuGameViewModelFactory(
			SudokuDataStoreRepository(
				context.sudokuGridDataStore
			)
		)
	),
	modifier: Modifier = Modifier
) {
	val uiState = viewModel.uiState.collectAsState().value
	val sudoku = uiState.sudoku

	val loading = viewModel.isLoading.collectAsState()
	var resetDialogVisible by remember { mutableStateOf(false) }
	var hintsDialogState = rememberDialogState(false)


	VictoryDialog(
		isVisible = uiState.gameState == GameState.VICTORY,
		onDismissRequest = { viewModel.onEvent(Event.DismissVictoryDialog) }
	)

	ResetDialog(
		isVisible = resetDialogVisible,
		onConfirmClick = {
			viewModel.onEvent(Event.ResetGame)
			resetDialogVisible = false
		},
		onDismissClick = { resetDialogVisible = false },
		onClearNotesClick = {
			viewModel.onEvent( Event.ResetNotes )
			resetDialogVisible = false
		}
	)

	HintsDialog(
		dialogState = hintsDialogState,
		onDismissRequest = { hintsDialogState.visible = false },
		onFillNotesClick = {
			viewModel.onEvent(Event.HintFillNotes)
			hintsDialogState.visible = false
		}
	)


	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		SudokuBoard(
			sudoku = sudoku,
			onCellClick = { row, col -> viewModel.onEvent(Event.SelectCell(row, col)) },
		)
		KeyPad(
			onNumberClick = { viewModel.onEvent(Event.InputNumber(it)) },
			onClearClick = { viewModel.onEvent(Event.ClearCell) },
			onUndoClick = { viewModel.onEvent(Event.Undo) },
			onRedoClick = { viewModel.onEvent(Event.Redo) },
			onNumberSwitchClick = { viewModel.onEvent(Event.SwitchInputMode(InputMode.NUMBER)) },
			onNoteSwitchClick = { viewModel.onEvent(Event.SwitchInputMode(InputMode.NOTE)) },
			onColorSwitchClick = { viewModel.onEvent(Event.SwitchInputMode(InputMode.COLOR)) },
			onHintClick = { hintsDialogState.visible = true },
			onShowMistakesClick = { viewModel.onEvent(Event.ShowMistakes) },
			onResetClick = { resetDialogVisible = true },
			inputMode = uiState.inputMode
		)
		if (loading.value) {
			CircularProgressIndicator()
		}
	}
}

@Preview
@Composable
private fun SudokuGameScreenPreview() {
	SudokuGameScreen(
		context = LocalContext.current
	)
}