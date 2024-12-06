package com.example.sudokuslayer.presentation.screen.game.components.KeyPadParts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionPad(
	onClearClick: () -> Unit,
	onUndoClick: () -> Unit,
	onRedoClick: () -> Unit,
) {
	val bgColor = MaterialTheme.colorScheme.background
	val iconColor = MaterialTheme.colorScheme.onBackground
	Row(
		modifier = Modifier.padding(horizontal = 8.dp)
	) {
		KeyPadItem(
			text = "",
			icon = Icons.AutoMirrored.Default.ArrowBack,
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onUndoClick
		)
		Spacer(modifier = Modifier.width(8.dp))
		KeyPadItem(
			text = "",
			icon = Icons.Default.Clear,
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onClearClick
		)
		Spacer(modifier = Modifier.width(8.dp))
		KeyPadItem(
			text = "",
			icon = Icons.AutoMirrored.Default.ArrowForward,
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onRedoClick
		)
	}
}

@Preview
@Composable
private fun ActionPadPreview() {
	ActionPad(
		onClearClick = {  },
		onUndoClick = {  },
		onRedoClick = {  }
	)
}