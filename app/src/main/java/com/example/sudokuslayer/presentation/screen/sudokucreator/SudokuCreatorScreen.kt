package com.example.sudokuslayer.presentation.screen.sudokucreator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudokuslayer.presentation.screen.sudokucreator.SudokuCreatorViewModel.Event
import com.example.sudokuslayer.presentation.screen.sudokucreator.components.HorizontalSelect

@Composable
fun SudokuCreatorScreen(
	viewModel: SudokuCreatorViewModel = viewModel(),
	modifier: Modifier = Modifier
) {
	val uiState = viewModel.uiState.collectAsState().value

	Column(
		modifier = modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Box(contentAlignment = Alignment.Center,
			modifier = Modifier
			.size(200.dp)
			.background(color = MaterialTheme.colorScheme.error))
		{
			Text(
				"PREVIEW",
				style = MaterialTheme.typography.displayMedium
			)
		}
		Spacer(Modifier.height(50.dp))
		HorizontalSelect(
			text = uiState.difficulty.name.lowercase().replaceFirstChar { it.uppercase() },
			onLeftClick = { viewModel.onEvent(Event.ChangeDifficulty(-1)) },
			onRightClick = { viewModel.onEvent(Event.ChangeDifficulty(1))},
			modifier = Modifier.fillMaxWidth(0.8f)
		)
		when(uiState.screenState) {
			ScreenState.INITIAL -> {
				Button(onClick = { viewModel.onEvent(Event.LoadSudoku) }) {
					Text("Continue")
				}
				Button(onClick = { viewModel.onEvent(Event.NewGame) }) {
					Text("New game")
				}
			}
			ScreenState.LOADING -> {
				CircularProgressIndicator()
			}
			ScreenState.DONE -> {
				Text("DONE")
			}
		}
	}
}

@Preview
@Composable
private fun SudokuCreatorScreenPreview() {
	SudokuCreatorScreen()
}