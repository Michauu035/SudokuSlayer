package com.example.sudoku.dlxalgorithm

import com.example.sudoku.model.SudokuGrid

class DancingLinksMatrix(val rootNode: Node) {
	companion object {
		fun fromSudoku(sudoku: SudokuGrid): DancingLinksMatrix {
			ExactCoverMatrix.generateExactCoverMatrix()
			val exactCoverMatrix: Array<BooleanArray> = ExactCoverMatrix.matrix.clone()

			val dlxNodes = exactCoverMatrix.toNode()
			sudoku.getArray().filter { it.number != 0 }.forEach { cell ->
				val rowindex = ExactCoverMatrix.getRowIndex(
					row = cell.row,
					col = cell.col,
					num = cell.number
				)
				// Get column numbers that row satisfies
				val columns: Set<String> = exactCoverMatrix[rowindex].mapIndexed { index, b -> if (b) index else -1 }
					.filter { it != -1 }
					.map { "H$it" }
					.toSet()


				var columnNode = dlxNodes.right
				do {
					if (columns.contains(columnNode.name)) {
						columnNode.cover()
					}
					columnNode = columnNode.right
				} while (columnNode != dlxNodes)
			}

			return DancingLinksMatrix(dlxNodes)
		}
	}
}