package com.example.sudokuslayer.domain.model

import kotlin.random.Random

interface SudokuGenerator {
    fun createSudoku(cellsToRemove: Int, seed: Long = Random.nextLong()): SudokuGrid
    fun generateFullSudokuGrid(seed: Long = Random.nextLong()): SudokuGrid
    fun removeCells(grid: SudokuGrid, cellsToRemove: Int): SudokuGrid
}