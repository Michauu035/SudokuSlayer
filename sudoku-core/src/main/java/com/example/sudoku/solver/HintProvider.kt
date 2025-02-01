package com.example.sudoku.solver

import com.example.sudoku.model.House
import com.example.sudoku.model.SudokuCellData
import com.example.sudoku.symmetricDifference

sealed interface HintType {
	object NakedSingle : HintType
	data class HiddenSingle(val groupType: GroupType) : HintType
	data class PointingCandidate(val groupType: GroupType) : HintType
	object ClaimingCandidate : HintType
}

enum class GroupType {
	ROW, COLUMN, BLOCK
}

data class Hint(
	val row: Int,
	val col: Int,
	val value: Int,
	val type: HintType? = null,
	val explanationStrategy: HintExplanationStrategy? = null,
	val additionalInfo: String = ""
)

class HintProvider() {
	fun provideHint(data: Array<SudokuCellData>): Hint? {
		val updatedData = fillCandidates(data)

		// Check Naked Single from entire grid.
		findNakedSingle(updatedData)?.let { return it }

		// Generate Houses from updatedData
		val houses = mutableListOf<House>()
		(0..8).forEach { row ->
			houses.add(House.Row(updatedData.filter { it.row == row }, row))
		}
		(0..8).forEach { col ->
			houses.add(House.Column(updatedData.filter { it.col == col }, col))
		}
		(0 until 3).forEach { boxRow ->
			(0 until 3).forEach { boxCol ->
				val blockCells = updatedData.filter { it.row / 3 == boxRow && it.col / 3 == boxCol }
				houses.add(House.Block(blockCells, boxRow, boxCol))
			}
		}

		// Loop over houses and try to find a hidden single in each one
		for (house in houses) {
			findHiddenSingle(house)?.let { return it }
		}
		return null
	}

	fun findNakedSingle(data: Array<SudokuCellData>): Hint? {
		for (cell in data.filter { it.number == 0 }) {
			if (cell.candidates.size == 1) {
				return Hint(
					row = cell.row,
					col = cell.col,
					value = cell.candidates.first(),
					type = HintType.NakedSingle,
					explanationStrategy = NakedSingleExplanation()
				)
			}
		}
		return null
	}

	fun findHiddenSingle(house: House): Hint? {
		val emptyCells = house.cells.filter { it.number == 0 }
		if (emptyCells.isEmpty()) return null
		val diff = symmetricDifference(emptyCells.map { it.candidates })
		if (diff.size == 1) {
			val hintValue = diff.first()
			val hintCell = emptyCells.find { it.candidates.contains(hintValue) }!!
			val hiddenSingleType = when (house) {
				is House.Row -> HintType.HiddenSingle(GroupType.ROW)
				is House.Column -> HintType.HiddenSingle(GroupType.COLUMN)
				is House.Block -> HintType.HiddenSingle(GroupType.BLOCK)
			}
			return Hint(
				row = hintCell.row,
				col = hintCell.col,
				value = hintValue,
				type = hiddenSingleType,
				explanationStrategy = HiddenSingleExplanation()
			)
		}
		return null
	}

	fun findLockedCandidate(house: House, data: Array<SudokuCellData>): Hint? {
		return when (house) {
			is House.Block -> {
				val hints = findPointingCandidates(house, data)
				if (hints.isNotEmpty()) hints.first() else null
			}

			is House.Row, is House.Column -> {
				val hints = findClaimingCandidates(house, data)
				if (hints.isNotEmpty()) hints.first() else null
			}
		}
	}

	// Works on a Block house only
	fun findPointingCandidates(house: House.Block, data: Array<SudokuCellData>): List<Hint> {
		val hints = mutableListOf<Hint>()
		val emptyCells = house.cells.filter { it.number == 0 }
		val candidateDigits = emptyCells.flatMap { it.candidates }.toSet()
		for (digit in candidateDigits) {
			val candidateCells = emptyCells.filter { digit in it.candidates }
			if (candidateCells.size < 2) continue

			val uniqueRows = candidateCells.map { it.row }.toSet()
			val uniqueCols = candidateCells.map { it.col }.toSet()

			// Candidate is pointing in a row
			if (uniqueRows.size == 1) {
				val rowCells = getRow(data, uniqueRows.first())
					.filter { !emptyCells.containsCell(it) && it.number == 0 }
					.filter { digit in it.candidates }

				rowCells.forEach {
					hints.add(
						Hint(
							row = it.row,
							col = it.col,
							value = digit,
							type = HintType.PointingCandidate(GroupType.ROW)
						)
					)
				}
			}

			// Candidate is pointing in a column
			if (uniqueCols.size == 1) {
				val colCells = getColumn(data, uniqueCols.first())
					.filter { !emptyCells.containsCell(it) && it.number == 0 }
					.filter { digit in it.candidates }

				colCells.forEach {
					hints.add(
						Hint(
							row = it.row,
							col = it.col,
							value = digit,
							type = HintType.PointingCandidate(GroupType.COLUMN)
						)
					)
				}
			}
		}
		return hints
	}

	//  Works on a Row or Column house
	fun findClaimingCandidates(house: House, data: Array<SudokuCellData>): List<Hint> {
		val hints = mutableListOf<Hint>()
		for (digit in 1..9) {
			val candidateCells = house.cells.filter { it.number == 0 && digit in it.candidates }
			if (candidateCells.isNotEmpty()) {
				// Check if candidate cells belong to the same block (using block index)
				val uniqueBlocks = candidateCells.map { (it.row / 3) * 3 + (it.col / 3) }.toSet()
				if (uniqueBlocks.size == 1) {
					val blockIndex = uniqueBlocks.first()
					val blockCells = getBox(data, blockIndex / 3, blockIndex % 3)

					blockCells.filter { cell ->
						house.cells.none { it.row == cell.row && it.col == cell.col } &&
								cell.number == 0 && digit in cell.candidates
					}.forEach {
						hints.add(
							Hint(
								row = it.row,
								col = it.col,
								value = digit,
								type = HintType.ClaimingCandidate
							)
						)
					}
				}
			}
		}
		return hints
	}

	fun getPossibleValues(data: Array<SudokuCellData>, row: Int, col: Int): Set<Int> {
		if (data[row * 9 + col].number != 0) {
			return emptySet()
		}
		val rowValues = data.filter { it.row == row }.map { it.number }
		val colValues = data.filter { it.col == col }.map { it.number }
		val subgridValues =
			data.filter { it.row / 3 == row / 3 && it.col / 3 == col / 3 }.map { it.number }
		val allValues = (1..9).toSet()
		val possibleValues = allValues - rowValues - colValues - subgridValues
		return possibleValues
	}

	private fun getRow(data: Array<SudokuCellData>, row: Int): List<SudokuCellData> {
		return data.filter { it.row == row }
	}

	private fun getColumn(data: Array<SudokuCellData>, col: Int): List<SudokuCellData> {
		return data.filter { it.col == col }
	}

	private fun getBox(
		data: Array<SudokuCellData>,
		boxRow: Int,
		boxCol: Int
	): List<SudokuCellData> {
		val startRow = boxRow * 3
		val startCol = boxCol * 3
		return data.filter { it.row in startRow until startRow + 3 && it.col in startCol until startCol + 3 }
	}
}

fun List<SudokuCellData>.containsCell(cell: SudokuCellData): Boolean {
	return this.any { it.row == cell.row && it.col == cell.col }
}

fun HintProvider.fillCandidates(data: Array<SudokuCellData>): Array<SudokuCellData> {
	return data.map { cell ->
		if (cell.number == 0)
			cell.copy(candidates = getPossibleValues(data, cell.row, cell.col))
		else
			cell
	}.toTypedArray()
}