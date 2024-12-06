package com.example.sudokuslayer.presentation.screen.game.components.keypadparts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R
import com.example.sudokuslayer.presentation.screen.game.model.InputMode

@Composable
fun VerticalActionPadRight(
	onNumberSwitchClick: () -> Unit,
	onNoteSwitchClick: () -> Unit,
	onColorSwitchClick: () -> Unit,
	inputMode: InputMode
) {
	val bgColor = MaterialTheme.colorScheme.surfaceContainer
	val iconColor = MaterialTheme.colorScheme.onSurface
	val selectedBgColor = MaterialTheme.colorScheme.tertiaryContainer
	val selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer

	Column(modifier = Modifier.padding(8.dp)) {
		KeyPadItem(
			text = "",
			icon = {
				Icon(
					painter = painterResource(R.drawable.tag),
					contentDescription = "Switch to number pad"
				)
			},
			bgColor = if (inputMode == InputMode.NUMBER) selectedBgColor else bgColor,
			textColor = if (inputMode == InputMode.NUMBER) selectedIconColor else selectedIconColor,
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
			bgColor = if (inputMode == InputMode.NOTE) selectedBgColor else bgColor,
			textColor = if (inputMode == InputMode.NOTE) selectedIconColor else selectedIconColor,
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
			bgColor = if (inputMode == InputMode.COLOR) selectedBgColor else bgColor,
			textColor = if (inputMode == InputMode.COLOR) selectedIconColor else selectedIconColor,
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
		onColorSwitchClick = { },
		inputMode = InputMode.NUMBER
	)
}