package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

object ClassicSudokuSolver : SudokuSolver {
    /**
     * Checks if a given row contains unique non-zero numbers.
     *
     * This function ensures that all non-zero numbers in the specified row are unique,
     * maintaining the Sudoku rule that each number (1–9) appears only once in each row.
     *
     * @param row the row to check, represented as an [SudokuGrid] row (an IntArray).
     * @return `true` if all non-zero numbers in the row are unique, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val row = intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0)
     * val isValid = checkRow(row) // Checks if the row is valid
     * println(isValid) // Output: true
     * ```
     */
    override fun checkRow(row: IntArray): Boolean {
        val filtered = row.filter { it != 0 }
        return filtered.toSet().size == filtered.size
    }

    /**
     * Checks if a given column contains unique non-zero numbers.
     *
     * This function behaves the same as `checkRow`, but it operates on a column instead.
     * It ensures that all non-zero numbers in the column are unique.
     *
     * @param col the column to check, represented as an [SudokuGrid] column (an IntArray).
     * @return `true` if all non-zero numbers in the column are unique, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val col = intArrayOf(5, 6, 0, 8, 4, 0, 7, 0, 0)
     * val isValid = checkColumn(col) // Checks if the column is valid
     * println(isValid) // Output: true
     * ```
     */
    override fun checkColumn(col: IntArray): Boolean {
        return checkRow(col)
    }

    /**
     * Checks if a given 3x3 subgrid contains unique non-zero numbers.
     *
     * This function flattens a 2D array representing a 3x3 subgrid into a 1D array
     * and checks if all non-zero numbers in the subgrid are unique. It uses [checkRow]
     * to perform the uniqueness check on the flattened array.
     *
     * @param subgrid the 3x3 subgrid to check, represented as an `Array<IntArray>`.
     * @return `true` if all non-zero numbers in the subgrid are unique, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val subgrid = arrayOf(
     *     intArrayOf(5, 3, 0),
     *     intArrayOf(6, 0, 0),
     *     intArrayOf(0, 9, 8)
     * )
     * val isValid = checkSubgrid(subgrid) // Checks if the subgrid is valid
     * println(isValid) // Output: true
     * ```
     */
    override fun checkSubgrid(subgrid: Array<IntArray>): Boolean {
        val arr = subgrid.flatMap { it.asList() }.filter{it != 0}.toIntArray()
        return checkRow(arr)
    }

    /**
     * Checks if placing a given number at a specific position in the Sudoku grid is valid.
     *
     * This function performs the following checks:
     * 1. The number must not already exist in the same row.
     * 2. The number must not already exist in the same column.
     * 3. The number must not already exist in the same 3x3 subgrid.
     *
     * @param sudoku the Sudoku grid to check, represented as a [SudokuGrid] (assumed to have helper methods for rows, columns, and subgrids).
     * @param rowNum the row index of the cell to check (0–8).
     * @param colNum the column index of the cell to check (0–8).
     * @param num the number to check (1–9).
     * @return `true` if the move is valid, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val sudoku = SudokuGrid()
     * val isValid = isValidMove(sudoku, 0, 0, 4) // Checks if placing 4 at (0, 0) is valid
     * println(isValid) // Output: true or false
     * ```
     */
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

    /**
     * Checks if the entire Sudoku grid is valid.
     *
     * This function verifies that all rows, columns, and 3x3 subgrids in the Sudoku grid
     * follow the Sudoku rules, i.e., each number (1–9) appears only once per row,
     * column, and subgrid. It calls [checkRow], [checkColumn], and [checkSubgrid]
     * for each row, column, and subgrid in the grid to ensure validity.
     *
     * @param sudoku the [SudokuGrid] to check.
     * @return `true` if all rows, columns, and subgrids are valid, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val sudokuGrid = SudokuGrid()
     * val isValid = checkGrid(sudokuGrid) // Checks if the entire grid is valid
     * println(isValid) // Output: true or false
     * ```
     */
    override fun checkGrid(sudoku: SudokuGrid): Boolean {
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

    /**
     * Checks if a given Sudoku grid is a valid solution.
     *
     * This function checks whether the Sudoku grid satisfies the rules of Sudoku:
     * 1. All rows, columns, and 3x3 subgrids must contain unique non-zero numbers.
     * It calls [checkGrid] to validate the structure of the grid and ensures there are no zeros in the grid.
     *
     * @param sudokuGrid the [SudokuGrid] to check.
     * @return `true` if the Sudoku grid is a valid solution, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val sudokuGrid = SudokuGrid()
     * val isValid = isValidSolution(sudokuGrid) // Checks if the grid is a valid solution
     * println(isValid) // Output: true or false
     * ```
     */
    override fun isValidSolution(sudokuGrid: SudokuGrid): Boolean {
        val validGrid = checkGrid(sudokuGrid)
        val noZeros = 0 !in sudokuGrid.getArray().map { it.number }
        return validGrid && noZeros
    }

    /**
     * Fills a Sudoku grid with numbers, attempting to generate a valid configuration.
     *
     * This function generates a fully solved Sudoku grid using a backtracking algorithm.
     * The generated grid adheres to all Sudoku rules:
     *  - Each number from 1 to 9 appears exactly once in each row.
     *  - Each number from 1 to 9 appears exactly once in each column.
     *  - Each number from 1 to 9 appears exactly once in each 3x3 subgrid.
     *
     *  **Algorithm Overview:**
     *  The function uses backtracking to recursively attempt to fill the grid:
     *  1. It finds the first empty cell (value 0) in the grid.
     *  2. It tries placing numbers (1–9) in that cell.
     *     - The numbers are shuffled for randomness to ensure varied solutions.
     *     - Before placing a number, it checks if the placement is valid (no conflicts in the row, column, or subgrid).
     *  3. If a number is valid, it is placed in the cell, and the function proceeds to the next empty cell.
     *  4. If no valid number can be placed in a cell (i.e., a dead end is reached), the function backtracks:
     *     - It resets the current cell to 0 and returns to the previous cell to try the next number.
     *  5. The process continues until the grid is completely filled or all possibilities are exhausted.
     *
     * **Performance:**
     * - Backtracking can be computationally expensive in the worst case, but it is efficient for generating valid Sudoku grids.
     * - The algorithm uses randomization for variety, which makes grid generation unpredictable and diverse.
     *
     * @param sudoku the [SudokuGrid] to fill.
     * @return `true` if the grid was successfully filled, `false` if it was not possible.
     *
     * **Example Usage:**
     * ```kotlin
     * val sudokuGrid = SudokuGrid()
     * val success = fillGrid(sudokuGrid) // Tries to fill the grid
     * println(success) // Output: true or false
     * ```
     */
    override fun fillGrid(sudoku: SudokuGrid): Boolean {
        val random = Random(sudoku.seed)
        for (row in 0..8) {
            for (col in 0..8) {
                // Find empty cell
                if (sudoku[row, col].number == 0) {
                    val numbers = (1..9).toList()
                    val shuffled = numbers.shuffled(random)
                    println(shuffled)

                    // Find number that is safe to place in cell
                    for (num in shuffled) {
                        if (
                            isValidMove(
                                sudoku = sudoku,
                                rowNum = row,
                                colNum = col,
                                num = num
                            )
                        ) {
                            sudoku[row, col] = num
                            if (fillGrid(sudoku)) {
                                return true
                            }
                            sudoku[row, col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    /**
     * Checks if a given Sudoku puzzle has a unique solution.
     *
     * This function uses a backtracking approach to try and solve the Sudoku puzzle.
     * It recursively attempts to fill the grid with numbers. If the puzzle has more
     * than one solution, it returns `false`. If exactly one solution is found, it
     * returns `true`. The function stops once it detects more than one solution.
     *
     * The `solve` function is used to fill the grid and count solutions. Each time
     * a solution is found, the solution count is incremented. If the solution count
     * exceeds 1, it immediately returns `false`, indicating the puzzle has multiple solutions.
     *
     * @param sudokuGrid the [SudokuGrid] to check.
     * @return `true` if the puzzle has exactly one solution, `false` otherwise.
     *
     * **Example Usage:**
     * ```kotlin
     * val sudokuGrid = SudokuGrid()
     * val hasUniqueSolution = hasUniqueSolution(sudokuGrid) // Checks if the grid has a unique solution
     * println(hasUniqueSolution) // Output: true or false
     * ```
     */
    override fun hasUniqueSolution(sudokuGrid: SudokuGrid): Boolean {
        var solutionCount = 0

        fun solve(grid: SudokuGrid): Boolean{
            for (row in 0..8){
                for (col in 0..8){
                    if (grid[row, col].number == 0) {
                        for (num in 1..9) {
                            if (isValidMove(grid, row, col, num)){
                                grid[row, col] = num
                                if (solve(grid)) {
                                    solutionCount++
                                    if (solutionCount > 1) return false
                                }
                                grid[row, col] = 0
                            }
                        }
                        return false
                    }
                }
            }
            return true
        }
        solve(sudokuGrid)
        return solutionCount == 1
    }
}