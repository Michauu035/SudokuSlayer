package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid

class ClassicSudokuGenerator : SudokuGenerator {
    override fun createEmptyGrid(): SudokuGrid {
        val grid = Array(9) { IntArray(9) { 0 } }
        return SudokuGrid(grid);
    }

    override fun fillGrid(sudoku: SudokuGrid): SudokuGrid {
        TODO() 
    }

    override fun validateGrid(sudoku: SudokuGrid): Boolean {
        TODO("Not yet implemented")
    }
}