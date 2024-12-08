package com.example.sudokuslayer.presentation.screen.game.components.keypadparts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R

@Composable
fun ActionPad(
	onClearClick: () -> Unit,
	onUndoClick: () -> Unit,
	onRedoClick: () -> Unit,
) {
	val bgColor = MaterialTheme.colorScheme.surfaceContainer
	val iconColor = MaterialTheme.colorScheme.onSurface
	Row(
		modifier = Modifier.padding(horizontal = 8.dp)
	) {
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.undo),
					contentDescription = "Undo"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onUndoClick
		)
		Spacer(modifier = Modifier.width(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					imageVector = Icons.Default.Clear,
					contentDescription = "Clear"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onClearClick
		)
		Spacer(modifier = Modifier.width(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.redo),
					contentDescription = "Redo"
				)
			},
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