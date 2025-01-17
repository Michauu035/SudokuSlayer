package com.example.sudokuslayer.presentation.navigation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sudokuslayer.presentation.navigation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@Composable
fun NavigationDrawer(
	destinations: List<Destination>,
	drawerState: DrawerState,
	navController: NavController,
	scope: CoroutineScope,
	content: @Composable () -> Unit,
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentScreen = navBackStackEntry?.destination

	ModalNavigationDrawer(
		drawerState = drawerState,
		drawerContent = {
			ModalDrawerSheet(
				drawerContainerColor = MaterialTheme.colorScheme.surfaceVariant
			) {
				Text(
					text = "Sudoku Slayer",
					modifier = Modifier.padding(16.dp),
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				HorizontalDivider()
				destinations.forEach { destination ->
					MyNavigationDrawerItem(
						isSelected = currentScreen?.hierarchy?.any { it.hasRoute(destination::class) } == true,
						destination = destination,
						onClick = {
							navController.navigate(destination)
							scope.launch {
								drawerState.close()
							}
						}
					)
				}
			}
		}
	) {
		content()
	}
}