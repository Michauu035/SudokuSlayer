package com.example.sudoku.solver

import com.example.sudoku.dlxalgorithm.DancingLinksMatrix
import com.example.sudoku.dlxalgorithm.SudokuExactCoverMatrix
import com.example.sudoku.dlxalgorithm.toDancingLinksMatrix
import com.example.sudoku.model.SudokuCellData
import com.example.sudoku.model.SudokuGrid
import kotlin.math.sqrt

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

fun Collection<Int>.toSudokuGrid(inputGrid: SudokuGrid = SudokuGrid()): SudokuGrid {
    if (isEmpty()) {
        return inputGrid
    }
    val resultGrid = inputGrid.clone()
    for (pos in this) {
        val row = pos / 81
        val col = (pos % 81) / 9
        val num = pos % 9 + 1
        if (inputGrid[row, col].number == 0) {
            resultGrid[row, col] = num
        }
    }

    return resultGrid
}

fun DancingLinksMatrix.Companion.fromSudoku(sudoku: SudokuGrid): DancingLinksMatrix {
    // Convert existing numbers in the Sudoku grid to constraints
    val filledCells = getFilledCells( sudoku.getArray() )
    val exactCoverMatrix = SudokuExactCoverMatrix.createClassic().apply {
        coverAll(filledCells)
    }
    val dancingLinksMatrix = exactCoverMatrix.toDancingLinksMatrix()
    return dancingLinksMatrix
}

fun getFilledCells(sudokuGrid: Array<SudokuCellData>): List<Triple<Int, Int, Int>> {
    val filledCells = mutableListOf<Triple<Int, Int, Int>>()
    for (cell in sudokuGrid) {
            val (row, col, num) = cell
            if (num != 0) {  // If the cell is already filled
                filledCells.add(Triple(row, col, num))
            }
    }
    return filledCells
}



fun createConstraints(row: Int, col: Int, num: Int, gridSize: Int): List<Int> {
    val subgridSize = sqrt(gridSize.toDouble()).toInt()
    fun getBoxIndex(row: Int, col: Int): Int = (row / subgridSize) * subgridSize + (col / subgridSize)
    fun getCellConstraintIndex(row: Int, col: Int, num: Int): Int = row * gridSize + col
    fun getRowConstraintIndex(row: Int, num: Int): Int = gridSize * gridSize + row * gridSize + num
    fun getColConstraintIndex(col: Int, num: Int): Int = 2 * gridSize * gridSize + col * gridSize + num
    fun getBoxConstraintIndex(box: Int, num: Int): Int = 3 * gridSize * gridSize + box * gridSize + num

    val cellConstraint = getCellConstraintIndex(row, col, num)
    val rowConstraint = getRowConstraintIndex(row, num)
    val colConstraint = getColConstraintIndex(col, num)
    val boxConstraint = getBoxConstraintIndex(getBoxIndex(row, col), num)
    return listOf(cellConstraint, rowConstraint, colConstraint, boxConstraint)
}