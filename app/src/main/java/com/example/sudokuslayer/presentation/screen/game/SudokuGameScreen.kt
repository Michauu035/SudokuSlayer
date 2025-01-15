package com.example.sudokuslayer.presentation.screen.game

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composables.core.rememberDialogState
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.data.datastore.sudokuGridDataStore
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel.Event
import com.example.sudokuslayer.presentation.screen.game.components.HintsDialog
import com.example.sudokuslayer.presentation.screen.game.components.KeyPad
import com.example.sudokuslayer.presentation.screen.game.components.ResetDialog
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard
import com.example.sudokuslayer.presentation.screen.game.components.Timer
import com.example.sudokuslayer.presentation.screen.game.components.VictoryDialog
import com.example.sudokuslayer.presentation.screen.game.model.GameState
import com.example.sudokuslayer.presentation.screen.game.model.InputMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuGameScreen(
	context: Context,
	openDrawer: () -> Unit,
	viewModel: SudokuGameViewModel = viewModel(
		factory = SudokuGameViewModelFactory(
			SudokuDataStoreRepository(
				context.sudokuGridDataStore
			)
		)
	),
	timerViewModel: TimerViewModel = viewModel(
		factory = TimerViewModelFactory(
			SudokuDataStoreRepository(
				context.sudokuGridDataStore
			)
		)
	)
) {
	val lifecycleOwner = LocalLifecycleOwner.current
	DisposableEffect(Unit) {
		lifecycleOwner.lifecycle.addObserver(timerViewModel)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(timerViewModel)
		}
	}

	val uiState = viewModel.uiState.collectAsState().value
	val elapsedTime = timerViewModel.elapsedTime.collectAsState().value
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
			timerViewModel.resetTimer()
			resetDialogVisible = false
		},
		onDismissClick = { resetDialogVisible = false },
		onClearNotesClick = {
			viewModel.onEvent(Event.ResetNotes)
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

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			CenterAlignedTopAppBar(
				title = { Timer(elapsedTime) },
				navigationIcon = {
					IconButton(onClick = openDrawer) {
						Icon(Icons.Default.Menu, "")
					}
				}
			)
		}
	) { innerPadding ->
		if (loading.value) {
			CircularProgressIndicator()
		} else {
			Column(
				modifier = Modifier.fillMaxSize().padding(innerPadding),
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

			}
		}
	}
}

@Preview
@Composable
private fun SudokuGameScreenPreview() {
	SudokuGameScreen(
		context = LocalContext.current,
		openDrawer = { }
	)
}