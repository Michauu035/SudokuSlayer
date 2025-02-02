package com.example.sudoku.solver

import com.example.sudoku.model.SudokuGrid

interface HintExplanationStrategy {
	fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String>
}

class NakedSingleExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(gird: SudokuGrid, hint: Hint): List<String> {
		val (row, column, value) = hint
		return listOf(
			"The cell at [${row + 1}, ${column + 1}] has only one possible candidate remaining after considering the numbers already present in its row, column, and block.",
			"Since the only possible candidate for the cell is <$value>, this cell must contain <$value>.",
			"*Naked Single*"
		)
	}
}

class HiddenSingleExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String> {
		val hintType = hint.type as HintType.HiddenSingle
		val scope = when (hintType.groupType) {
			GroupType.ROW -> "row ${hint.row + 1}"
			GroupType.COLUMN -> "col ${hint.col + 1}"
			GroupType.BLOCK -> "3x3 block containing the cell"
		}

		val blockedCells = when (hintType.groupType) {
			GroupType.ROW -> "columns { " + grid.getRow(hint.row).filter { it.number == 0 }
				.map { it.col + 1 }.joinToString() + " }"
			GroupType.COLUMN -> "rows { " + grid.getCol(hint.col).filter { it.number == 0 }
				.map { it.row + 1 }.joinToString() + " }"
			GroupType.BLOCK -> "other cells"
		}

		val blockedReason = when (hintType.groupType) {
			GroupType.ROW -> "they are blocked by ${hint.value} in the same column or block"
			GroupType.COLUMN -> "they are blocked by ${hint.value} in the same row or block"
			GroupType.BLOCK -> "those cells are blocked by numbers in the same row or column"
		}

		return listOf(
			"In '$scope', <${hint.value}> cannot be placed in $blockedCells because $blockedReason.",
			"Therefore, the cell at [${hint.row + 1}, ${hint.col + 1}] must contain <${hint.value}>.",
			"*Hidden Single*"
		)
	}
}

class ClaimingCandidateExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String> {
		val hintType = hint.type as HintType.ClaimingCandidate
		val scope = when (hintType.groupType) {
			GroupType.ROW -> "row ${hint.row + 1}"
			GroupType.COLUMN -> "col ${hint.col + 1}"
			else -> throw IllegalArgumentException("Claiming Candidate hint type must be either row or column")
		}

		val blockedCells = when (hintType.groupType) {
			GroupType.ROW -> "columns { " + grid.getRow(hint.row).filter { it.number == 0 }
				.map { it.col+ 1 }.joinToString() + " }"
			GroupType.COLUMN -> "rows { " + grid.getCol(hint.col).filter { it.number == 0 }
				.map { it.row + 1 }.joinToString() + " }"
			else -> throw IllegalArgumentException("Claiming Candidate hint type must be either row or column")
		}

		val blockedReason = when (hintType.groupType) {
			GroupType.ROW -> "they are blocked by ${hint.value} in the same block"
			GroupType.COLUMN -> "they are blocked by ${hint.value} in the same block"
			else -> throw IllegalArgumentException("Claiming Candidate hint type must be either row or column")
		}

		return listOf(
			"In $scope, <${hint.value}> cannot be placed in $blockedCells because $blockedReason.",
			"Therefore, the cell at [${hint.row + 1}, ${hint.col + 1}] must contain <${hint.value}>.",
			"*Claiming Candidate*"
		)
	}
}

class PointingCandidateExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String> {
		val hintType = hint.type as HintType.PointingCandidate
		val scope = when (hintType.groupType) {
			GroupType.ROW -> "row ${hint.row + 1}"
			GroupType.COLUMN -> "col ${hint.col + 1}"
			else -> throw IllegalArgumentException("Pointing Candidate hint type must be either row or column")
		}

		val blockedCells = when (hintType.groupType) {
			GroupType.ROW -> "columns { " + grid.getRow(hint.row).filter { it.number == 0 }
				.map { it.col + 1 }.joinToString() + " }"
			GroupType.COLUMN -> "rows { " + grid.getCol(hint.col).filter { it.number == 0 }
				.map { it.row + 1 }.joinToString() + " }"
			else -> throw IllegalArgumentException("Pointing Candidate hint type must be either row or column")
		}

		val blockedReason = when (hintType.groupType) {
			GroupType.ROW -> "they are blocked by ${hint.value} in the same block"
			GroupType.COLUMN -> "they are blocked by ${hint.value} in the same block"
			else -> throw IllegalArgumentException("Pointing Candidate hint type must be either row or column")
		}

		return listOf(
			"In $scope, <${hint.value}> cannot be placed in $blockedCells because $blockedReason.",
			"Therefore, the cell at [${hint.row + 1}, ${hint.col + 1}] must contain <${hint.value}>.",
			"*Pointing Candidate*"
		)
	}
}