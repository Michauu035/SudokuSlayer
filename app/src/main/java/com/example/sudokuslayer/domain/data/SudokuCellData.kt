package com.example.sudokuslayer.domain.data

data class SudokuCellData(
	val row: Int,
	val col: Int,
	val number: Int = 0,
	val cornerNotes: MutableSet<Int> = mutableSetOf(),
	val centerNotes: MutableSet<Int> = mutableSetOf(),
	val attributes: MutableSet<CellAttributes> = mutableSetOf()
)

enum class CellAttributes {
	SELECTED,
	GENERATED
}