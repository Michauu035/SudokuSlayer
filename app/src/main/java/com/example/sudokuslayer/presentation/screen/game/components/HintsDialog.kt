package com.example.sudokuslayer.presentation.screen.game.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme

@Composable
fun HintsDialog(
	dialogState: DialogState,
	onDismissRequest: () -> Unit,
	onFillNotesClick: () -> Unit,
	onHintClick: () -> Unit,
	onShowLogsClick: () -> Unit
) {
	val buttons = listOf(
		HintDialogButton("Hint cell", onHintClick, { Icon(Icons.Default.Face, null) }),
		HintDialogButton("Show hint logs", onShowLogsClick, { Icon(Icons.AutoMirrored.Default.List, null) }),
		HintDialogButton("Fill notes", onFillNotesClick, { Icon(painterResource(R.drawable.stylus_note), null) }),
	)


	Dialog(
		state = dialogState,
		onDismiss = onDismissRequest,
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
		) {
			Card(
				modifier = Modifier.fillMaxSize(),
				colors = CardDefaults.cardColors(
					containerColor = MaterialTheme.colorScheme.surface
				),
				elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
			) {
				Column(
					modifier = Modifier.padding(20.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Icon(painterResource(R.drawable.lightbulb), "")
					Spacer(Modifier.height(20.dp))
					LazyColumn {
						items(buttons) { button ->
							Button(
								onClick = button.onClick,
								colors = ButtonDefaults.buttonColors(
									containerColor = MaterialTheme.colorScheme.primaryContainer,
									contentColor = MaterialTheme.colorScheme.onPrimaryContainer
								)
							) {
								Row(
									Modifier.fillMaxWidth().padding(4.dp),
									horizontalArrangement = Arrangement.Center,
									verticalAlignment = Alignment.CenterVertically
								) {
									button.icon()
									Spacer(Modifier.width(10.dp))
									Text(
										text = button.text,
										color = MaterialTheme.colorScheme.onPrimaryContainer
									)
								}
							}
							Spacer(Modifier.height(10.dp))
						}
					}
				}
			}
		}
	}
}

data class HintDialogButton(
	val text: String,
	val onClick: () -> Unit,
	val icon: @Composable () -> Unit = { }
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, name = "Dark mode")
@Composable
private fun HintsDialogPreview() {
	val dialogState = rememberDialogState(true)
	SudokuSlayerTheme {
		HintsDialog(
			dialogState = dialogState,
			onDismissRequest = { },
			onHintClick = { },
			onFillNotesClick = { },
			onShowLogsClick = {}
		)
	}
}
