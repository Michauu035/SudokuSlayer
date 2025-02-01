package com.example.sudoku.model

data class SudokuCellData(
	val row: Int,
	val col: Int,
	val number: Int = 0,
	val cornerNotes: Set<Int> = setOf(),
	val candidates: Set<Int> = setOf(),
	val attributes: Set<CellAttributes> = setOf()
)

enum class CellAttributes {
	UNSPECIFIED,
	SELECTED,
	GENERATED,
	NUMBER_MATCH_HIGHLIGHTED,
	ROW_COLUMN_HIGHLIGHTED,
	RULE_BREAKING,
	HINT_FOCUS,
	HINT_REVEALED,
}