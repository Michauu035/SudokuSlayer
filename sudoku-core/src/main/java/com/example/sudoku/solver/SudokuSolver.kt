package com.example.sudoku.solver

import com.example.sudoku.model.SudokuGrid

interface SudokuSolver {
    fun checkRow(row: IntArray): Boolean
    fun checkColumn(col: IntArray): Boolean
    fun checkSubgrid(subgrid: IntArray): Boolean
    fun isValidMove(sudoku: SudokuGrid, rowNum: Int, colNum: Int, num: Int): Boolean
    fun checkGrid(sudoku: SudokuGrid): Boolean
    fun isValidSolution(sudokuGrid: SudokuGrid): Boolean
    fun hasUniqueSolution(sudokuGrid: SudokuGrid): Boolean
    fun fillGrid(sudokuGrid: SudokuGrid) : Boolean
}