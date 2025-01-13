package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Timer(
	elapsedTime: Long
) {
	val minutes = elapsedTime / 60
	val seconds = elapsedTime % 60
	Text(
		text = String.format("%02d:%02d", minutes, seconds),
		style = MaterialTheme.typography.bodyLarge,
		color = MaterialTheme.colorScheme.primary
	)
}