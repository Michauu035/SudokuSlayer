package com.example.sudokuslayer.domain.model

interface SudokuSolver {
    fun checkRow(row: IntArray): Boolean
    fun checkColumn(col: IntArray): Boolean
    fun checkSubgrid(subgrid: Array<IntArray>): Boolean
}