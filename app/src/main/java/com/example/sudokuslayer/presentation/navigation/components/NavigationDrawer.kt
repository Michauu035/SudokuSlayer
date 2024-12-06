package com.example.sudokuslayer.presentation.navigation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.R

@Composable
fun NavigationDrawer(
	drawerState: DrawerState,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
) {
	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			ModalDrawerSheet {
				Text("Sudoku Slayer", modifier = Modifier.padding(16.dp))
				HorizontalDivider()
				NavigationDrawerItem(
					icon = { Icon(painterResource(R.drawable.tag), "") },
					label = { Text("Current game") },
					selected = true,
					onClick = { },
					modifier = Modifier.padding(8.dp)
				)
				NavigationDrawerItem(
					icon = {
						Icon(Icons.Default.Add, "")
					},
					label = {
						Text("New game")
					},
					selected = false,
					onClick = { },
					modifier = Modifier.padding(8.dp)
				)
			}
		}
	) {
		content()
	}
}