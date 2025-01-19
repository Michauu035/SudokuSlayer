package com.example.sudokuslayer.presentation.screen.game.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R
import com.example.sudokuslayer.createAnnotatedString
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme
import com.example.sudokuslayer.presentation.ui.theme.catppuccinPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HintBottomSheetScaffold(
	sheetScaffoldState: BottomSheetScaffoldState,
	hintLogs: List<String> = emptyList<String>(),
	explainHintClick: () -> Unit,
	nextHintClick: () -> Unit,
	showNextHint: Boolean = false,
	topBar: @Composable (() -> Unit)? = null,
	content: @Composable (PaddingValues) -> Unit
) {
	BottomSheetScaffold(
		scaffoldState = sheetScaffoldState,
		sheetPeekHeight = 128.dp,
		sheetContainerColor = MaterialTheme.colorScheme.surfaceTint,
		sheetContentColor = MaterialTheme.colorScheme.onSurface,
		containerColor = MaterialTheme.colorScheme.background,
		contentColor = MaterialTheme.colorScheme.onBackground,
		topBar = topBar,
		sheetContent = {
			SheetContent(
				title = "Hint logs",
				logs = hintLogs,
				showNextHint = showNextHint,
				explainHintClick = explainHintClick,
				nextHintClick = nextHintClick,
				modifier = Modifier.heightIn(min = 128.dp, max = 350.dp)
			)
		},
		content = content
	)
}

@Composable
fun SheetContent(
	title: String,
	explainHintClick: () -> Unit,
	nextHintClick: () -> Unit,
	showNextHint: Boolean = false,
	logs: List<String> = emptyList(),
	modifier: Modifier = Modifier
) {
	val listState = rememberLazyListState()
	var currentLogIndex by remember { mutableIntStateOf(0) }
	val logsToShow = logs.take(currentLogIndex + 1).mapIndexed { index, s -> index + 1 to s }

	LaunchedEffect(logsToShow.size) {
		if (logsToShow.isNotEmpty())
			listState.animateScrollToItem(logsToShow.size - 1)
	}

	LaunchedEffect(logs.size) {
		if (logs.isEmpty())
			currentLogIndex = 0
	}

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(16.dp)
			.padding(
				bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
			),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			modifier = Modifier
				.padding(bottom = 8.dp)
		)
		LazyColumn(
			state = listState,
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
		) {
			itemsIndexed(items = logsToShow, key = { id, v -> id }) { index, pair ->
				Text(
					text = buildAnnotatedString {
						append("${pair.first}. ")
						append(
							createAnnotatedString(
								input = pair.second,
								angleBracketStyle = SpanStyle(
									color = MaterialTheme.colorScheme.primary,
									fontWeight = FontWeight.Bold,
									fontSize = MaterialTheme.typography.bodyMedium.fontSize
								),
								asteriskStyle = SpanStyle(
									color = MaterialTheme.catppuccinPalette.Subtext1,
									fontStyle = FontStyle.Italic,
									fontSize = MaterialTheme.typography.bodySmall.fontSize
								),
							)
						)
					},
					style = MaterialTheme.typography.bodyMedium,
					modifier = Modifier
						.padding(vertical = 4.dp)
						.animateItem(),
				)
			}
		}

		if (showNextHint && (currentLogIndex + 1 == logs.size || logs.isEmpty())) {
			BottomSheetElevatedButton(
				text = "Next hint",
				icon = { Icon(painterResource(R.drawable.lightbulb), null) },
				onClick = {
					if (currentLogIndex == 0 && logs.isEmpty()) {
						nextHintClick()
					} else {
						nextHintClick()
						currentLogIndex++
					}
				},
				contentColor = MaterialTheme.colorScheme.secondary,
			)
		} else {
			BottomSheetElevatedButton(
				text = "Explain hint",
				icon = { Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null) },
				onClick = {
					if (currentLogIndex < logs.size - 1) {
						currentLogIndex++
					} else {
						explainHintClick()
						currentLogIndex++
					}
				}
			)
		}
	}
}

@Composable
fun BottomSheetElevatedButton(
	text: String,
	icon: @Composable (() -> Unit)? = null,
	containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
	contentColor: Color = MaterialTheme.colorScheme.primary,
	onClick: () -> Unit
) {
	ElevatedButton(
		onClick = onClick,
		colors = ButtonDefaults.elevatedButtonColors(
			containerColor = containerColor,
			contentColor = contentColor
		)

	) {
		Text(
			text = text,
			color = contentColor
		)

		if (icon != null) {
			Spacer(Modifier.width(4.dp))
			icon()
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HintBottomSheetScaffoldPreview() {
	SudokuSlayerTheme {
		val scaffoldState = rememberBottomSheetScaffoldState(
			bottomSheetState = rememberStandardBottomSheetState(
				initialValue = SheetValue.Expanded
			)
		)
		HintBottomSheetScaffold(
			sheetScaffoldState = scaffoldState,
			hintLogs = listOf("test1", "test2"),
			explainHintClick = { },
			nextHintClick = { },
			topBar = null,
			content = { }
		)
	}
}