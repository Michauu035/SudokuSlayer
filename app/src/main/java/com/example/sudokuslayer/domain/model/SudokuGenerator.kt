package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid

interface SudokuGenerator {
    fun createEmptyGrid(): SudokuGrid
    fun fillGrid(grid: SudokuGrid): SudokuGrid
    fun validateGrid(grid: SudokuGrid): Boolean
}