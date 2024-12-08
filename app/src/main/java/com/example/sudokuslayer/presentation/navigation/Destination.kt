package com.example.sudokuslayer.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
	@Serializable
	object SudokuGame : Destination

	@Serializable
	object SudokuCreator : Destination
}
