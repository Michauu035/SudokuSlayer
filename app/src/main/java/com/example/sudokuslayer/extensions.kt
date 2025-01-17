package com.example.sudokuslayer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.min
import kotlin.math.roundToInt

fun Color.lighten(factor: Float): Color {
	val argb = this.toArgb()
	val red = (argb.red * factor).roundToInt()
	val green = (argb.green * factor).roundToInt()
	val blue = (argb.blue * factor).roundToInt()

	return Color(
		android.graphics.Color.argb(
			argb.alpha,
			min(red, 255),
			min(green, 255),
			min(blue, 255)
		)
	)

}



