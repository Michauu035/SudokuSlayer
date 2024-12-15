package com.example.sudokuslayer.domain.model

data class SudokuCellData(
	val row: Int,
	val col: Int,
	val number: Int = 0,
	val cornerNotes: Set<Int> = setOf(),
	val centerNotes: Set<Int> = setOf(),
	val attributes: Set<CellAttributes> = setOf()
)

enum class CellAttributes {
	SELECTED,
	GENERATED,
	HIGHLIGHTED
}