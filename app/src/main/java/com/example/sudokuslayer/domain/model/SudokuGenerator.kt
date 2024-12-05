package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

interface SudokuGenerator {
    fun createSudoku(cellsToRemove: Int, seed: Long = Random.nextLong()): SudokuGrid
    fun generateFullSudokuGrid(seed: Long = Random.nextLong()): SudokuGrid
    fun removeCells(grid: SudokuGrid, cellsToRemove: Int): SudokuGrid
}