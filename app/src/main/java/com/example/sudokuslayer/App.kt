package com.example.sudokuslayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sudokuslayer.presentation.navigation.SudokuNavHost
import com.example.sudokuslayer.presentation.navigation.components.NavigationDrawer
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme

@Composable
fun App() {
	val navController = rememberNavController()
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

	SudokuSlayerTheme(
		darkTheme = true,
		dynamicColor = false
	) {
		NavigationDrawer(
			drawerState = drawerState,
			navController = navController,
		) {
			Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
				Column(modifier = Modifier.padding(innerPadding)) {
					SudokuNavHost(navController)
				}
			}
		}
	}
}