package com.example.sudokuslayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.sudokuslayer.domain.model.ClassicSudokuGenerator
import com.example.sudokuslayer.presentation.screen.game.SudokuGameScreen
import com.example.sudokuslayer.presentation.screen.game.SudokuGameViewModel
import com.example.sudokuslayer.presentation.screen.game.components.SudokuBoard
import com.example.sudokuslayer.presentation.ui.theme.SudokuSlayerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: SudokuGameViewModel by viewModels()
        setContent {
            SudokuSlayerTheme(
                darkTheme = true,
                dynamicColor = false
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        SudokuGameScreen(viewModel = viewModel, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
