package com.example.sudoku.solver

import com.example.sudoku.model.SudokuGrid

interface HintExplanationStrategy {
	fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String>
}

object HintExplanationRegistry {
	private val strategies = mutableMapOf<HintType, HintExplanationStrategy>(
		HintType.HIDDEN_SINGLE to HiddenSingleExplanation(),
		HintType.NAKED_SINGLE to NakedSingleExplanation()
	)

	fun registerStrategy(type: HintType, strategy: HintExplanationStrategy) {
		strategies[type] = strategy
	}

	fun getStrategy(type: HintType): HintExplanationStrategy {
		return strategies[type] ?: throw IllegalArgumentException("No strategy registered for $type")
	}
}

class NakedSingleExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(gird: SudokuGrid, hint: Hint): List<String> {
		val (row, column, value) = hint
		return listOf(
			"The cell at [${row + 1}, ${column + 1}] has only one possible candidate remaining after considering the numbers already present in its row, column, and block.",
			"Since the only possible candidate for the cell is <$value>, this cell must contain <$value>. \n*Naked Single*"
		)
	}
}

class HiddenSingleExplanation : HintExplanationStrategy {
	override fun generateHintExplanationSteps(grid: SudokuGrid, hint: Hint): List<String> {
		val scope = when (hint.additionalInfo) {
			"row" -> "row ${hint.row + 1}"
			"col" -> "col ${hint.col + 1}"
			else -> "3x3 block containing the cell"
		}

		val blockedCells = when (hint.additionalInfo) {
			"row" -> "columns { " + grid.getRow(hint.row).withIndex().filter { it.value == 0 }
				.map { it.index + 1 }.joinToString() + " }"
			"col" -> "rows { " + grid.getCol(hint.col).withIndex().filter { it.value == 0 }
				.map { it.index + 1 }.joinToString() + " }"
			else -> "other cells"
		}

		val blockedReason = when (hint.additionalInfo) {
			"row" -> "they are blocked by ${hint.value} in the same column or block"
			"col" -> "they are blocked by ${hint.value} in the same row or block"
			else -> "those cells are blocked by numbers in the same row or column"
		}

		return listOf(
			"In '$scope', <${hint.value}> cannot be placed in $blockedCells because $blockedReason.",
			"Therefore, the cell at [${hint.row + 1}, ${hint.col + 1}] must contain <${hint.value}>. \n*Hidden Single*"
		)
	}
}