package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

interface SudokuGenerator {
    fun generateSudokuGrid(seed: Long = Random.nextLong()): SudokuGrid
    fun removeCells(grid: SudokuGrid, cellsToRemove: Int, seed: Long = Random.nextLong()): SudokuGrid
}