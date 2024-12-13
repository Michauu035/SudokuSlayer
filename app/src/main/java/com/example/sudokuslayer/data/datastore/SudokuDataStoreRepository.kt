package com.example.sudokuslayer.data.datastore

import androidx.datastore.core.DataStore
import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.data.SudokuCellData
import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import sudoku.SudokuCell.Attributes
import sudoku.SudokuCell as SudokuCellProto
import sudoku.SudokuGrid as SudokuGridProto


class SudokuDataStoreRepository(
	private val dataStore: DataStore<SudokuGridProto>
) {
	val sudokuGridProto: Flow<SudokuGrid> = dataStore.data
			.map { mapToDomainSudokuGrid(it) }
			.distinctUntilChanged()

	private fun mapToDomainSudokuGrid(grid: SudokuGridProto): SudokuGrid {
		val cellData = grid.dataList.map { cell -> mapToDomainCellData(cell) }.toTypedArray()

		return if (grid.seed != 0L) {
			SudokuGrid(seed = grid.seed).apply { set(cellData) }
		} else {
			SudokuGrid(cellData)
		}
	}


	// Private function for mapping CellDataProto to SudokuCellData
	private fun mapToDomainCellData(protoCell: SudokuCellProto): SudokuCellData {
		return SudokuCellData(
			row = protoCell.row,
			col = protoCell.col,
			number = protoCell.number,
			cornerNotes = protoCell.cornerNotesList.toMutableSet(),
			attributes = if (protoCell.attribute.number == 1) {
				mutableSetOf(CellAttributes.GENERATED)
			} else {
				mutableSetOf()
			}
		)
	}

	private fun mapToProtoCellData(domainCell: SudokuCellData): SudokuCellProto {
		return SudokuCellProto.newBuilder()
			.setRow(domainCell.row)
			.setCol(domainCell.col)
			.setNumber(domainCell.number)
			.addAllCornerNotes(domainCell.cornerNotes)
			.setAttribute(
				Attributes.forNumber( if (domainCell.attributes.contains(CellAttributes.GENERATED)) 1 else 0)
			)
			.build()
	}

	private fun updateProtoWithDomainSudoku(domainSudoku: SudokuGrid): SudokuGridProto {
		val protoCells = domainSudoku.clone().getArray().map { mapToProtoCellData(it) }

		return SudokuGridProto.newBuilder()
			.setSeed(domainSudoku.seed ?: 0)
			.addAllData(protoCells)
			.build()
	}

	suspend fun updateData(domainSudoku: SudokuGrid) {
		val updatedData = updateProtoWithDomainSudoku(domainSudoku)

		dataStore.updateData { currentData -> updatedData }
	}

	suspend fun updateCell(row: Int, col: Int, newCellData: SudokuCellData) {
		dataStore.updateData { protoGrid ->
			val updatedDataList = protoGrid.dataList.map { cell ->
				if (cell.row == row && cell.col == col) {
					cell.toBuilder()
						.setNumber(newCellData.number)
						.clearCornerNotes()
						.addAllCornerNotes(newCellData.cornerNotes)
						.setAttribute(
							Attributes.forNumber( if (newCellData.attributes.contains(CellAttributes.GENERATED)) 1 else 0)
						)
						.build()
				} else {
					cell
				}
			}

			protoGrid.toBuilder()
				.clearData()
				.addAllData(updatedDataList)
				.build()
		}
	}
}
