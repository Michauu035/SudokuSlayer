package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid

class ClassicSudokuSolver : SudokuSolver {
    override fun checkRow(row: IntArray): Boolean {
        // Remove empty boxes and
        val filtered = row.filter { it != 0 }
        // Compare Set size to Array size
        return filtered.toSet().size == filtered.size
    }

    override fun checkColumn(col: IntArray): Boolean {
        return checkRow(col)
    }

    override fun checkSubgrid(subgrid: Array<IntArray>): Boolean {
        // Merge subgrid into one array
        val arr = subgrid.flatMap { it.asList() }.filter{it != 0}.toIntArray()
        return checkRow(arr)
    }

    override fun isValidMove(
        grid: SudokuGrid,
        rowNum: Int,
        colNum: Int,
        num: Int
    ): Boolean {
        TODO()
    }
}