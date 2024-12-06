package com.example.sudokuslayer.presentation.screen.game.components.keypadparts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R

@Composable
fun VerticalActionPadRight(
	onNumberSwitchClick: () -> Unit,
	onNoteSwitchClick: () -> Unit,
	onColorSwitchClick: () -> Unit,
) {
	val bgColor = MaterialTheme.colorScheme.surfaceContainer
	val iconColor = MaterialTheme.colorScheme.onSurface

	Column(modifier = Modifier.padding(8.dp)) {
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.tag),
					contentDescription = "Switch to number pad"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onNumberSwitchClick
		)
		Spacer(modifier = Modifier.height(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.stylus_note),
					contentDescription = "Switch to note pad"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onNoteSwitchClick
		)
		Spacer(modifier = Modifier.height(8.dp))
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.palette),
					contentDescription = "Switch to color pad"
				)
			},
			bgColor = bgColor,
			textColor = iconColor,
			onClick = onColorSwitchClick
		)
	}
}

@Preview
@Composable
private fun VerticalActionPadRightPreview() {
	VerticalActionPadRight(
		onNumberSwitchClick = { },
		onNoteSwitchClick = { },
		onColorSwitchClick = { }
	)
}