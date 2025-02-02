package com.example.sudoku.model

import kotlin.random.Random

class SudokuGrid(
	private var data: Array<SudokuCellData> = createEmptyGrid()
) {
	var seed: Long? = null
		private set
	var random: Random = Random(0)
		private set

	private val cellManager = CellManager(data)

	operator fun get(row: Int, col: Int): SudokuCellData {
		requireValidIndex(row, col)
		return data[getIndex(row, col)]
	}

	operator fun set(row: Int, col: Int, value: Int) {
		requireValidIndex(row, col)
		val index = getIndex(row, col)
		data[index] = data[index].copy(
			number = value
		)
	}

	fun set(array: Array<SudokuCellData>) {
		data = array
	}

	fun replaceCell(row: Int, col: Int, cellData: SudokuCellData) {
		val index = getIndex(row, col)
		data[index] = data[index].copy(
			number = cellData.number,
			cornerNotes = cellData.cornerNotes,
			candidates = cellData.candidates,
		)
	}

	fun getRow(row: Int): Array<SudokuCellData> {
		require(row in 0..8) { "Index out of bounds" }
		return data.filter { it.row == row }.toTypedArray()
	}

	fun getCol(col: Int): Array<SudokuCellData> {
		require(col in 0..8) { "Index out of bounds" }
		return data.filter { it.col == col }.toTypedArray()
	}

	fun getSubgrid(rowNum: Int, colNum: Int): Array<SudokuCellData> {
		requireValidIndex(rowNum, colNum)
		val startRow = (rowNum / 3) * 3
		val startCol = (colNum / 3) * 3
		return data.filter { it.row in startRow until startRow + 3 && it.col in startCol until startCol + 3 }
			.toTypedArray()
	}

	fun getEmptyCellsCount(): Int = data.count { it.number == 0 }

	fun getArray(): Array<SudokuCellData> = data

	fun clone(): SudokuGrid = SudokuGrid(data.clone())

	override fun toString(): String = data.groupBy { it.row }
		.values.joinToString("\n") { row -> row.joinToString(" ") { it.number.toString() } }

	companion object {
		private fun createEmptyGrid(): Array<SudokuCellData> = Array(81) { index ->
			SudokuCellData(
				row = index / 9,
				col = index % 9,
				number = 0
			)
		}

		private fun intArrayToData(intArray: Array<IntArray>): Array<SudokuCellData> =
			intArray.flatMapIndexed { rowIndex, row ->
				row.mapIndexed { colIndex, value -> SudokuCellData(rowIndex, colIndex, value) }
			}.toTypedArray()

		fun fromIntArray(gridData: Array<IntArray>): SudokuGrid =
			SudokuGrid(intArrayToData(gridData))

		fun SudokuGrid.withSeed(seed: Long): SudokuGrid = this.apply {
			this.seed = seed
			this.random = Random(seed)
		}

		fun fromCellData(cells: Array<SudokuCellData>): SudokuGrid = SudokuGrid(cells.clone())

		fun fromStringArray(gridData: Array<String>): SudokuGrid =
			fromIntArray(gridData.map { row -> row.map { it.toString().toInt() }.toIntArray() }
				.toTypedArray())
	}

	// Delegating Cell Manager functionality
	fun addAttribute(row: Int, col: Int, attribute: CellAttributes) {
		requireValidIndex(row, col)
		cellManager.addAttribute(row, col, attribute)
	}

	fun removeAttribute(row: Int, col: Int, attribute: CellAttributes) {
		requireValidIndex(row, col)
		cellManager.removeAttribute(row, col, attribute)
	}

	fun addCornerNote(row: Int, col: Int, noteNumber: Int) {
		requireValidIndex(row, col)
		cellManager.addCornerNote(row, col, noteNumber)
	}

	fun removeCornerNote(row: Int, col: Int, noteNumber: Int) {
		requireValidIndex(row, col)
		cellManager.removeCornerNote(row, col, noteNumber)
	}

	fun clearCornerNotes(row: Int, col: Int) {
		requireValidIndex(row, col)
		cellManager.clearCornerNotes(row, col)
	}

	fun highlightMatchingCells(number: Int) {
		cellManager.highlightMatchingCells(number)
	}

	fun highlightRowAndColumn(row: Int, col: Int) {
		requireValidIndex(row, col)
		cellManager.highlightRowAndColumn(row, col)
	}

	fun clearNumberHighlight() {
		cellManager.clearNumberHighlight()
	}

	fun clearRowColumnHighlight() {
		cellManager.clearRowColumnHighlight()
	}

	fun lockGeneratedCells() = cellManager.lockGeneratedCells()

	fun fillNotes() = cellManager.fillNotes()

	fun resetGame() = cellManager.resetGame()

	fun clearNotes() = cellManager.clearNotes()

	fun markRuleBreakingCells() = cellManager.markRuleBreakingCells()

	fun clearRuleBreakingCells() = cellManager.clearRuleBreakingCells()

	// Utility functions
	private fun getIndex(row: Int, col: Int): Int = row * 9 + col

	private fun requireValidIndex(row: Int, col: Int) {
		require(row in 0..8 && col in 0..8) { "Index out of bounds: row=$row, col=$col" }
	}
}