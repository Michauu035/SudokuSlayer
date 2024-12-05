package com.example.sudokuslayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator
import com.example.sudokuslayer.presentation.ui.game.SudokuBoard
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val generator = ClassicSudokuGenerator()
        val numbers = generator.createSudoku(56, Random.nextLong()).getArray().map { it.number }.toTypedArray()
        setContent {
            SudokuSlayerTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Button(
                            onClick = {
                            }
                        ) {
                            Text("Regenerate grid")
                        }
                        SudokuBoard(numbers = numbers, modifier = Modifier.fillMaxHeight())
                    }
                }
            }
        }
    }
}
