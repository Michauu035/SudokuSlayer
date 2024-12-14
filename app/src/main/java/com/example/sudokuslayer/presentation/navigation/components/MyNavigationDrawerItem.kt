package com.example.sudokuslayer.presentation.navigation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sudokuslayer.presentation.navigation.Destination
import com.example.sudokuslayer.presentation.navigation.DestinationIcon

@Composable
fun MyNavigationDrawerItem(isSelected: Boolean, destination: Destination, onClick: () -> Unit) {
	NavigationDrawerItem(
		icon = {
			DestinationIcon(destination.icon)
		},
		label = {
			Text(destination.routeName)
		},
		selected = isSelected,
		onClick = onClick,
		modifier = Modifier.padding(8.dp)
	)
}

@Composable
fun DestinationIcon(icon: DestinationIcon) {
	when(icon){
		is DestinationIcon.ResourceIcon -> Icon(painterResource(icon.resourceId), "")
		is DestinationIcon.VectorIcon -> Icon(icon.imageVector, "")
	}
}