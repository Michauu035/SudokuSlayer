package com.example.sudokuslayer.domain.model

class ClassicSudokuGenerator : SudokuGenerator {
    override fun createEmptyGrid(): Array<IntArray> {
        val grid = Array(9) { IntArray(9) { 0 } }
        return grid;
    }

    override fun fillGrid(grid: Array<IntArray>): Array<IntArray> {
        TODO("Not yet implemented")
    }

    override fun validateGrid(grid: Array<IntArray>): Boolean {
        TODO("Not yet implemented")
    }
}