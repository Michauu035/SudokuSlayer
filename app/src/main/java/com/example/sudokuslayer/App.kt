package com.example.sudokuslayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.sudokuslayer.presentation.navigation.Destination
import com.example.sudokuslayer.presentation.navigation.SudokuNavHost
import com.example.sudokuslayer.presentation.navigation.components.NavigationDrawer
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
	val navController = rememberNavController()
	val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
	val destinations = listOf(
		Destination.SudokuGame,
		Destination.SudokuCreator
	)
	val scope = rememberCoroutineScope()

	SudokuSlayerTheme() {
		NavigationDrawer(
			destinations = destinations,
			drawerState = drawerState,
			navController = navController,
			scope = scope
		) {
			Scaffold(
				modifier = Modifier.fillMaxSize(),
				topBar = {
					CenterAlignedTopAppBar(
						title = { },
						navigationIcon = {
							IconButton(onClick = { scope.launch { drawerState.open() } }) {
								Icon(Icons.Default.Menu, "")
							}
						}
					)
				}
			) { innerPadding ->
				Column(modifier = Modifier.padding(innerPadding)) {
					SudokuNavHost(navController)
				}
			}
		}
	}
}