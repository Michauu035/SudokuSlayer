package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

interface SudokuSolver {
    fun checkRow(row: IntArray): Boolean
    fun checkColumn(col: IntArray): Boolean
    fun checkSubgrid(subgrid: Array<IntArray>): Boolean
    fun isValidMove(sudoku: SudokuGrid, rowNum: Int, colNum: Int, num: Int): Boolean
    fun checkGrid(sudoku: SudokuGrid): Boolean
    fun isValidSolution(sudokuGrid: SudokuGrid): Boolean
    fun hasUniqueSolution(sudokuGrid: SudokuGrid): Boolean
    fun fillGrid(sudokuGrid: SudokuGrid) : Boolean
}