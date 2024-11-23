package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid

interface SudokuSolver {
    fun checkRow(row: IntArray): Boolean
    fun checkColumn(col: IntArray): Boolean
    fun checkSubgrid(subgrid: Array<IntArray>): Boolean
    fun isValidMove(grid: SudokuGrid, rowNum: Int, colNum: Int, num: Int): Boolean
}