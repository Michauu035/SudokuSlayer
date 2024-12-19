package com.example.sudoku.dlxalgorithm

object ExactCoverMatrix {
	private val size = 9
	private val subgridSize = 3
	private val totalOptions = size * size * size
	private val totalConstraints = size * size * 4

	val matrix = Array(totalOptions) { BooleanArray(totalConstraints) }

	fun getRowIndex(row: Int, col: Int, num: Int): Int {
		val rowOffset = row * size * size
		val colOffset = col * size
		return rowOffset + colOffset + num - 1
	}

	fun rowIndexToCellData(rowIndex: Int, size: Int = 9): Triple<Int, Int, Int> {
		// Decode the rowIndex
		val cell = rowIndex / size // Cell index corresponds to the first 81 columns
		val num = (rowIndex % size) + 1 // Number is based on modulus and 1-based indexing

		// Derive row and col from the cell index
		val row = cell / size
		val col = cell % size

		return Triple(row, col, num)
	}

	fun generateExactCoverMatrix() {
		var rowIndex = 0
		for (r in 0 until size) {
			for (c in 0 until size) {
				for (n in 0 until size) {
					fillMatrixRow(matrix[rowIndex], r, c, n)
					rowIndex++
				}
			}
		}
	}

	private fun fillMatrixRow(matrixRow: BooleanArray, row: Int, col: Int, num: Int) {
		val rowColConstraint = row * size + col
		val rowNumConstraint = size * size + row * size + num
		val colNumConstraint = 2 * size * size + col * size + num
		val boxNumConstraint = 3 * size * size + getBoxIndex(row, col) * size + num

		matrixRow[rowColConstraint] = true
		matrixRow[rowNumConstraint] = true
		matrixRow[colNumConstraint] = true
		matrixRow[boxNumConstraint] = true
	}

	private fun getBoxIndex(row: Int, col: Int): Int {
		return (row / subgridSize) * subgridSize + (col / subgridSize)
	}
}