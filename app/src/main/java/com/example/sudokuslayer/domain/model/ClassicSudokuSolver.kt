package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid

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
		val arr = subgrid.flatMap { it.asList() }.filter { it != 0 }.toIntArray()
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
		for (i in 0..8) {
			if (!checkRow(sudoku.getRow(i))) return false
		}
		for (i in 0..8) {
			if (!checkColumn(sudoku.getCol(i))) return false
		}
		for (row in 0..6 step 3) {
			for (col in 0..6 step 3) {
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
	 * @param sudoku the [SudokuGrid] to check.
	 * @return `true` if the Sudoku grid is a valid solution, `false` otherwise.
	 *
	 * **Example Usage:**
	 * ```kotlin
	 * val sudokuGrid = SudokuGrid()
	 * val isValid = isValidSolution(sudokuGrid) // Checks if the grid is a valid solution
	 * println(isValid) // Output: true or false
	 * ```
	 */
	override fun isValidSolution(sudoku: SudokuGrid): Boolean {
		val validGrid = checkGrid(sudoku)
		val noZeros = 0 !in sudoku.getArray().map { it.number }
		return validGrid && noZeros
	}

	/**
	 * Recursively fills the provided Sudoku grid with numbers following Sudoku rules.
	 *
	 * This function attempts to fill the grid by finding the best empty cell (a cell with the fewest valid
	 * options) and then recursively tries to assign numbers to that cell in a random order. If a valid number
	 * cannot be assigned to the current cell, the function backtracks to the previous step.
	 *
	 * @param sudoku The `SudokuGrid` object representing the current state of the grid.
	 * @return `true` if the grid is successfully filled, `false` if no solution is found.
	 *
	 * ### Algorithm:
	 * 1. Finds the "best cell" to fill next using the [findBestCell] function.
	 * 2. Randomly shuffles the numbers 1-9 to attempt filling the cell.
	 * 3. Validates each number using `isValidMove`:
	 *    - If valid, assigns the number to the cell and recursively calls `fillGrid`.
	 *    - If the recursive call fails, it resets the cell to 0 (backtracking).
	 * 4. Stops when either the grid is completely filled or no valid solutions exist.
	 *
	 * ### Performance:
	 * - The function uses backtracking with heuristics (choosing the cell with the least options) to improve efficiency.
	 * - The grid's state is updated in-place.
	 *
	 * @see findBestCell
	 * @see isValidMove
	 */
	override fun fillGrid(sudoku: SudokuGrid): Boolean {
		val random = sudoku.random
		val bestCell = findBestCell(sudoku) ?: return true // no empty cells left
		val (row, col) = bestCell
		val numbers = (1..9).shuffled(random)

		for (num in numbers) {
			if (isValidMove(sudoku, rowNum = row, colNum = col, num = num)
			) {
				sudoku[row, col] = num
				if (fillGrid(sudoku)) return true
				sudoku[row, col] = 0
			}
		}

		return false
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
	 * @param sudoku the [SudokuGrid] to check.
	 * @return `true` if the puzzle has exactly one solution, `false` otherwise.
	 *
	 * **Example Usage:**
	 * ```kotlin
	 * val sudokuGrid = SudokuGrid()
	 * val hasUniqueSolution = hasUniqueSolution(sudokuGrid) // Checks if the grid has a unique solution
	 * println(hasUniqueSolution) // Output: true or false
	 * ```
	 */
	override fun hasUniqueSolution(sudoku: SudokuGrid): Boolean {
		var solutionCount = 0

		fun solve(grid: SudokuGrid): Boolean {
			for (row in 0..8) {
				for (col in 0..8) {
					if (grid[row, col].number == 0) {
						for (num in 1..9) {
							if (isValidMove(grid, row, col, num)) {
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

		solve(sudoku.clone())
		return solutionCount == 1
	}

	/**
	 * Finds the best empty cell in the Sudoku grid to fill next.
	 *
	 * This function identifies the cell with the least number of valid options (i.e., the fewest valid numbers
	 * that can be placed in that cell). This heuristic helps reduce the branching factor and improves the
	 * efficiency of the backtracking algorithm used in [fillGrid].
	 *
	 * @param sudoku The `SudokuGrid` object representing the current state of the grid.
	 * @return A `Pair` containing the row and column indices of the best cell to fill next, or `null` if no empty
	 * cells are available.
	 *
	 * ### Algorithm:
	 * 1. Iterates through all cells in the grid.
	 * 2. For each empty cell (value 0), counts the number of valid numbers (1-9) using `isValidMove`.
	 * 3. Tracks the cell with the minimum number of valid options.
	 * 4. Returns the indices of the best cell or `null` if all cells are filled.
	 *
	 * ### Performance:
	 * - The function performs a full scan of the grid (O(n^2), where n = 9 for a standard Sudoku grid).
	 * - Efficiently guides the backtracking process in [fillGrid] by prioritizing constrained cells.
	 *
	 * @see fillGrid
	 * @see isValidMove
	 */
	fun findBestCell(sudoku: SudokuGrid): Pair<Int, Int>? {
		var bestRow = -1
		var bestCol = -1
		var minChoices = Int.MAX_VALUE

		for (row in 0..8) {
			for (col in 0..8) {
				if (sudoku[row, col].number == 0) {
					val validNumbers = (1..9).count { isValidMove(sudoku, row, col, it) }
					if (validNumbers < minChoices) {
						minChoices = validNumbers
						bestRow = row
						bestCol = col
					}
				}
			}
		}
		return if (bestRow != -1 && bestCol != -1) bestRow to bestCol else null
	}
}