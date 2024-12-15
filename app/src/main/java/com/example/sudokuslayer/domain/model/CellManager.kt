package com.example.sudokuslayer.domain.model

class CellManager(
	private val data: Array<SudokuCellData>
) {
	fun addAttribute(row: Int, col: Int, attribute: CellAttributes) {
		updateCell(row, col) { it.copy(attributes = it.attributes + attribute) }
	}

	fun removeAttribute(row: Int, col: Int, attribute: CellAttributes) {
		updateCell(row, col) { it.copy(attributes = it.attributes - attribute) }
	}

	fun addCornerNote(row: Int, col: Int, noteNumber: Int) {
		updateCell(row, col) { it.copy(cornerNotes = it.cornerNotes + noteNumber) }
	}

	fun removeCornerNote(row: Int, col: Int, noteNumber: Int) {
		updateCell(row, col) { it.copy(cornerNotes = it.cornerNotes - noteNumber) }
	}

	fun clearCornerNotes(row: Int, col: Int) {
		updateCell(row, col) { it.copy(cornerNotes = emptySet()) }
	}

	fun resetGame() {
		for (cell in data.filter { !it.attributes.contains(CellAttributes.GENERATED) }) {
			updateCell(cell.row, cell.col) {
				it.copy(
					number = 0,
					cornerNotes = emptySet()
				)
			}
		}
	}

	fun clearNotes() {
		for (cell in data.filter { !it.attributes.contains(CellAttributes.GENERATED) }) {
			updateCell(cell.row, cell.col) {
				it.copy(
					cornerNotes = emptySet()
				)
			}
		}
	}

	fun lockGeneratedCells() {
		for (cell in data.filter { it.number != 0 }) {
			updateCell(cell.row, cell.col) {
				it.copy(
					attributes = it.attributes + CellAttributes.GENERATED
				)
			}
		}
	}

	fun highlightMatchingCells(number: Int) {
		if (number == 0)
			return
		for (cell in data.filter { it.number == number  }) {
			updateCell(cell.row, cell.col) { cell.copy(attributes = cell.attributes + CellAttributes.HIGHLIGHTED) }
		}
	}

	fun clearHighlightedCells() {
		for (cell in data.filter { it.attributes.contains(CellAttributes.HIGHLIGHTED)}) {
			updateCell(cell.row, cell.col) { cell.copy(attributes = cell.attributes - CellAttributes.HIGHLIGHTED) }
		}
	}

	fun fillNotes() {
		for (cell in data.filter { !it.attributes.contains(CellAttributes.GENERATED) }) {
			val possibleNumbers = (1..9).filter { ClassicSudokuSolver.isValidMove(SudokuGrid.fromCellData(data), cell.row, cell.col, it) }.toSet()
			updateCell(cell.row, cell.col) {
				it.copy(
					cornerNotes = possibleNumbers
				)
			}
		}
	}
	private fun updateCell(row: Int, col: Int, updater: (SudokuCellData) -> SudokuCellData) {
		val index = row * 9 + col
		data[index] = updater(data[index])
	}
}