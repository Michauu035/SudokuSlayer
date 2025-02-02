package com.example.sudoku.solver

import com.example.sudoku.dlxalgorithm.DancingLinksMatrix
import com.example.sudoku.dlxalgorithm.findSolution
import com.example.sudoku.model.SudokuGrid
import kotlin.random.Random

object ClassicSudokuSolver : SudokuSolver {
	var random: Random = Random(Random.nextLong())

	override fun checkRow(row: IntArray): Boolean {
		val filtered = row.filter { it != 0 }
		return filtered.toSet().size == filtered.size
	}

	override fun checkColumn(col: IntArray): Boolean {
		return checkRow(col)
	}

	override fun checkSubgrid(subgrid: IntArray): Boolean {
		val arr = subgrid.filter { it != 0 }.toIntArray()
		return checkRow(arr)
	}

	override fun isValidMove(
		sudoku: SudokuGrid,
		rowNum: Int,
		colNum: Int,
		num: Int
	): Boolean {
		val row = sudoku.getRow(rowNum).map { it.number }
		val col = sudoku.getCol(colNum).map { it.number }
		val subgrid = sudoku.getSubgrid(rowNum, colNum)
			.map{ it.number }

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
			if (!checkRow(sudoku.getRow(i).map { it.number }.toIntArray())) return false
		}
		for (i in 0..8) {
			if (!checkColumn(sudoku.getCol(i).map { it.number }.toIntArray())) return false
		}
		for (row in 0..6 step 3) {
			for (col in 0..6 step 3) {
				if (!checkSubgrid(sudoku.getSubgrid(row, col).map { it.number }.toIntArray())) return false
			}
		}
		return true
	}

	override fun isValidSolution(sudoku: SudokuGrid): Boolean {
		val validGrid = checkGrid(sudoku)
		val noZeros = 0 !in sudoku.getArray().map { it.number }
		return validGrid && noZeros
	}

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

	override fun hasUniqueSolution(sudoku: SudokuGrid): Boolean {
		val dancingLinksMatrix = DancingLinksMatrix.fromSudoku(sudoku)
		val result = findSolution(dancingLinksMatrix.rootNode, 2)

		return result.solutions.size == 1
	}

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