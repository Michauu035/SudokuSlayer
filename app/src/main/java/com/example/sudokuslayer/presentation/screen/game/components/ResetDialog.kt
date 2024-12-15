package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ResetDialog(
	isVisible: Boolean,
	onConfirmClick: () -> Unit,
	onDismissClick: () -> Unit,
	onClearNotesClick: () -> Unit,
) {
	if (isVisible) {
		AlertDialog(
			title = {
				Text("Clear Sudoku")
			},
			text = { Text("Are you sure you want to clear the entire Sudoku board? This action cannot be undone.") },
			icon = { Icon(Icons.Default.Warning, "") },
			onDismissRequest = onDismissClick,
			confirmButton = {
				TextButton(onConfirmClick) {
					Text("Yes")
				}
			},
			dismissButton = {
					TextButton(onClearNotesClick) {
						Text("Clear notes")
					}
					TextButton(onDismissClick) {
						Text("No")
					}
			}
		)
	}
}