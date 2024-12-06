package com.example.sudokuslayer.presentation.screen.game.components.KeyPadParts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun KeyPadItem(
	text: String,
	onClick: () -> Unit,
	icon: ImageVector? = null,
	iconContentDescription: String? = null,
	bgColor: Color = MaterialTheme.colorScheme.primary,
	textColor: Color = MaterialTheme.colorScheme.onPrimary,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.size(60.dp)
			.clip(CircleShape)
			.background(bgColor)
			.clickable(
				onClick = onClick
			),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			if (icon != null) {
				Icon(
					imageVector = icon,
					contentDescription = if (iconContentDescription == null) "" else iconContentDescription,
					tint = textColor
				)
			} else {
				Text(
					text = text,
					color = textColor
				)
			}
		}
	}
}

@Preview(name = "KeyPadItem_number", group = "KeyPadItemPreview")
@Composable
private fun KeyboardItemNumberPreview() {
	KeyPadItem(
		text = "5",
		onClick = { },
	)
}

@Preview(name = "KeyPadItem_icon", group = "KeyPadItemPreview")
@Composable
private fun KeyboardItemIconPreview() {
	KeyPadItem(
		text = "5",
		icon = Icons.Default.Clear,
		bgColor = MaterialTheme.colorScheme.background,
		textColor = MaterialTheme.colorScheme.onBackground,
		onClick = { },
	)
}