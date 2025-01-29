package com.example.sudoku.solver

import com.example.sudoku.model.SudokuCellData

enum class HintType {
	NAKED_SINGLE,
	HIDDEN_SINGLE
}

data class Hint(
	val row: Int,
	val col: Int,
	val value: Int,
	val type: HintType,
	val explanationStrategy: HintExplanationStrategy,
	val additionalInfo: String = ""
)

class HintProvider(
	private val data: Array<SudokuCellData>
) {
	fun provideHint(): Hint? {
		val nakedSingle = findNakedSingle()
		if (nakedSingle != null) {
			return nakedSingle
		}
		val hiddenSingle = findHiddenSingle()
		if (hiddenSingle != null) {
			return hiddenSingle
		}
		return null
	}

	fun findNakedSingle(): Hint? {
		for (cell in data.filter { it.number == 0 }) {
			val possibleValues = getPossibleValues(cell.row, cell.col)
			if (possibleValues.size == 1) {
				return Hint(
					row = cell.row,
					col = cell.col,
					value = possibleValues.first(),
					type = HintType.NAKED_SINGLE,
					explanationStrategy = NakedSingleExplanation()
				)
			}
		}
		return null
	}

	fun findHiddenSingle(): Hint? {
		for (row in 0..8) {
			val cellsInRow = data.filter { it.row == row }
			var emptyCells = cellsInRow.filter { it.number == 0 }
			if (emptyCells.isEmpty()) break

			emptyCells = emptyCells.map { it.copy(cornerNotes = getPossibleValues(it.row, it.col)) }

			val setSubstract = emptyCells.map { it.cornerNotes }.reduce { acc, set -> acc - set }
			if (setSubstract.size == 1) {
				val hintValue = setSubstract.first()
				val hintCell = emptyCells.find { it.cornerNotes.contains(hintValue) }!!
				return Hint(
					row = hintCell.row,
					col = hintCell.col,
					value = hintValue,
					type = HintType.HIDDEN_SINGLE,
					additionalInfo = "row",
					explanationStrategy = HiddenSingleExplanation()
				)
			}
		}
		for (col in 0..8) {
			val cellsInCol = data.filter { it.col == col }
			var emptyCells = cellsInCol.filter { it.number == 0 }
			if (emptyCells.isEmpty()) break

			emptyCells = emptyCells.map { it.copy(cornerNotes = getPossibleValues(it.row, it.col)) }

			val setSubstract = emptyCells.map { it.cornerNotes }.reduce { acc, set -> acc - set }
			if (setSubstract.size == 1) {
				val hintValue = setSubstract.first()
				val hintCell = emptyCells.find { it.cornerNotes.contains(hintValue) }!!
				return Hint(
					row = hintCell.row,
					col = hintCell.col,
					value = hintValue,
					type = HintType.HIDDEN_SINGLE,
					additionalInfo = "col",
					explanationStrategy = HiddenSingleExplanation()
				)
			}
		}

		for (subgrid in 0..8) {
			val cellsInSubgrid =
				data.filter { it.row / 3 == subgrid / 3 && it.col / 3 == subgrid / 3 }
			var emptyCells = cellsInSubgrid.filter { it.number == 0 }
			if (emptyCells.isEmpty()) break

			emptyCells = emptyCells.map { it.copy(cornerNotes = getPossibleValues(it.row, it.col)) }

			val setSubstract = emptyCells.map { it.cornerNotes }.reduce { acc, set -> acc - set }
			if (setSubstract.size == 1) {
				val hintValue = setSubstract.first()
				val hintCell = emptyCells.find { it.cornerNotes.contains(hintValue) }!!
				return Hint(
					row = hintCell.row,
					col = hintCell.col,
					value = hintValue,
					type = HintType.HIDDEN_SINGLE,
					additionalInfo = "block",
					explanationStrategy = HiddenSingleExplanation()
				)
			}
		}
		return null
	}

	fun getPossibleValues(row: Int, col: Int): Set<Int> {
		val rowValues = data.filter { it.row == row }.map { it.number }
		val colValues = data.filter { it.col == col }.map { it.number }
		val subgridValues =
			data.filter { it.row / 3 == row / 3 && it.col / 3 == col / 3 }.map { it.number }
		val allValues = (1..9).toSet()
		val possibleValues = allValues - rowValues - colValues - subgridValues
		return possibleValues
	}
}