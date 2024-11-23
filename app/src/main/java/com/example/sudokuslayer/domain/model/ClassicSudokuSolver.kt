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
        sudoku: SudokuGrid,
        rowNum: Int,
        colNum: Int,
        num: Int
    ): Boolean {
        val row = sudoku.getRow(rowNum)
        val col = sudoku.getCol(colNum)
        val subgrid = sudoku.getSubgrid(rowNum, colNum).flatMap { it.asList() }

        if (num in row)
            return false
        if (num in col)
            return false
        if (num in subgrid)
            return false

        return true
    }

    override fun isValidSolution(sudoku: SudokuGrid): Boolean {
        for(i in 0..8){
            if (!checkRow(sudoku.getRow(i))) return false
        }
        for (i in 0..8){
            if (!checkColumn(sudoku.getCol(i))) return false
        }
        for (row in 0..6 step 3){
            for (col in 0..6 step 3){
                if (!checkSubgrid(sudoku.getSubgrid(row, col))) return false
            }
        }
        return true
    }
}