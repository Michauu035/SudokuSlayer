package com.example.sudokuslayer.presentation.screen.sudokucreator

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sudokuslayer.data.datastore.SudokuDataStoreRepository
import com.example.sudokuslayer.data.datastore.sudokuGridDataStore
import com.example.sudokuslayer.presentation.navigation.Destination
import com.example.sudokuslayer.presentation.screen.sudokucreator.SudokuCreatorViewModel.Event
import com.example.sudokuslayer.presentation.screen.sudokucreator.components.HorizontalSelect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuCreatorScreen(
	context: Context,
	navController: NavController,
	openDrawer: () -> Unit,
	viewModel: SudokuCreatorViewModel = viewModel(factory = SudokuCreatorViewModelFactory(
		SudokuDataStoreRepository(context.sudokuGridDataStore))),
) {
	val uiState = viewModel.uiState.collectAsState().value

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			CenterAlignedTopAppBar(
				title = { },
				navigationIcon = {
					IconButton(onClick = openDrawer) {
						Icon(Icons.Default.Menu, "")
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier.fillMaxSize().padding(innerPadding),
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
					Button(
						enabled = uiState.hasSavedData,
						onClick = { viewModel.onEvent(Event.LoadSudoku) }
					) {
						Text("Continue ${uiState.savedDifficulty} - ${uiState.elapsedTime}")
					}
					Button(onClick = { viewModel.onEvent(Event.NewGame) }) {
						Text("New game")
					}
				}
				ScreenState.LOADING -> {
					CircularProgressIndicator()
				}
				ScreenState.DONE -> {
					navController.navigate(Destination.SudokuGame)
				}
			}
		}

	}
}

@Preview
@Composable
private fun SudokuCreatorScreenPreview() {
	SudokuCreatorScreen(
		context = LocalContext.current,
		navController = rememberNavController(),
		openDrawer = { }
	)
}