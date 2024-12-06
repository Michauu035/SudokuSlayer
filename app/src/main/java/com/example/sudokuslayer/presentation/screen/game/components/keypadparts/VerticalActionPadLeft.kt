package com.example.sudokuslayer.presentation.screen.game.components.keypadparts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R

@Composable
fun VerticalActionPadLeft(
	onHintClick: () -> Unit,
	onShowMistakesClick: () -> Unit,
	onResetClick: () -> Unit
) {
	val bgColor = MaterialTheme.colorScheme.surfaceContainer
	val iconColor = MaterialTheme.colorScheme.onSurface
	Column(modifier = Modifier.padding(8.dp)) {
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.lightbulb),
					contentDescription = "Show Hint"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onHintClick
		)
		Spacer(modifier = Modifier.height(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.question_mark),
					contentDescription = "Show mistakes"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onShowMistakesClick
		)
		Spacer(modifier = Modifier.height(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					imageVector = Icons.Default.Refresh,
					contentDescription = "Restart this game"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onResetClick
		)
	}
}

@Preview
@Composable
private fun VerticalActionPadLeftPreview() {
	VerticalActionPadLeft(
		onHintClick = { },
		onShowMistakesClick = { },
		onResetClick = { }
	)
}