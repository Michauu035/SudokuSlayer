package com.example.sudokuslayer.domain.model

interface SudokuGenerator {
    fun createEmptyGrid(): Array<IntArray>
    fun fillGrid(grid: Array<IntArray>): Array<IntArray>
    fun validateGrid(grid: Array<IntArray>): Boolean
}