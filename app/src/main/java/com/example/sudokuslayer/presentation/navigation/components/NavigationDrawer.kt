package com.example.sudokuslayer.presentation.navigation.components

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sudokuslayer.R
import com.example.sudokuslayer.presentation.navigation.Destination
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute

@SuppressLint("RestrictedApi")
@Composable
fun NavigationDrawer(
	drawerState: DrawerState,
	navController: NavController,
	content: @Composable () -> Unit
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentScreen = navBackStackEntry?.destination

	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			ModalDrawerSheet {
				Text("Sudoku Slayer", modifier = Modifier.padding(16.dp))
				HorizontalDivider()
				NavigationDrawerItem(
					icon = { Icon(painterResource(R.drawable.tag), "") },
					label = { Text("Current game") },
					selected = currentScreen?.hierarchy?.any { it.hasRoute(Destination.SudokuGame::class) } == true,
					onClick = {
						navController.navigate(Destination.SudokuGame)
					},
					modifier = Modifier.padding(8.dp)
				)
				NavigationDrawerItem(
					icon = {
						Icon(Icons.Default.Add, "")
					},
					label = {
						Text("New game")
					},
					selected = currentScreen?.hierarchy?.any { it.hasRoute(Destination.SudokuCreator::class) } == true,
					onClick = {
						navController.navigate(Destination.SudokuCreator)
					},
					modifier = Modifier.padding(8.dp)
				)
			}
		}
	) {
		content()
	}
}