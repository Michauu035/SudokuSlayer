package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogState
import com.composables.core.Scrim
import com.composables.core.rememberDialogState
import com.example.sudokuslayer.R

@Composable
fun HintsDialog(
	dialogState: DialogState,
	onDismissRequest: () -> Unit,
	onFillNotesClick: () -> Unit
) {
	Dialog(
		state = dialogState,
		onDismiss = onDismissRequest
	) {
		Scrim()
		DialogPanel(
			modifier =
			Modifier
				.systemBarsPadding()
				.widthIn(min = 280.dp, max = 560.dp)
				.height(400.dp)
				.padding(20.dp)
				.clip(RoundedCornerShape(12.dp))
				.background(AlertDialogDefaults.containerColor)
		) {
			Column(
				modifier = Modifier.padding(20.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Icon(painterResource(R.drawable.lightbulb), "")
				Spacer(Modifier.height(20.dp))
				Button(
					onClick = { }
				) {
					Row(
						Modifier.fillMaxWidth().padding(4.dp),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(Icons.Default.Face, "")
						Spacer(Modifier.width(10.dp))
						Text("Reveal cell")
					}
				}
				Spacer(Modifier.height(10.dp))
				Button(
					onClick = onFillNotesClick
				) {
					Row(
						Modifier.fillMaxWidth().padding(4.dp),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(painterResource(R.drawable.stylus_note), "")
						Spacer(Modifier.width(10.dp))
						Text("Fill notes")
					}
				}
			}
		}
	}
}

@Preview
@Composable
private fun HintsDialogPreview() {
	val dialogState = rememberDialogState(true)
	HintsDialog(
		dialogState = dialogState,
		onDismissRequest = { },
		onFillNotesClick = { },
	)
}