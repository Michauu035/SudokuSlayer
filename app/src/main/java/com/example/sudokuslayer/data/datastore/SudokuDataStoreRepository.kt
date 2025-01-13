package com.example.sudokuslayer.data.datastore

import androidx.datastore.core.DataStore
import com.example.sudoku.model.CellAttributes
import com.example.sudoku.model.SudokuCellData
import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.model.SudokuGrid.Companion.withSeed
import com.example.sudokuslayer.presentation.screen.sudokucreator.SudokuDifficulty
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

	val difficultyProto: Flow<SudokuDifficulty> = dataStore.data.map { data ->
		when (data.difficulty) {
			SudokuGridProto.Difficulty.DIFFICULTY_EASY -> SudokuDifficulty.EASY
			SudokuGridProto.Difficulty.DIFFICULTY_MEDIUM -> SudokuDifficulty.MEDIUM
			SudokuGridProto.Difficulty.DIFFICULTY_HARD -> SudokuDifficulty.HARD
			SudokuGridProto.Difficulty.DIFFICULTY_EXPERT -> SudokuDifficulty.EXPERT
			else -> SudokuDifficulty.EASY
		}
	}.distinctUntilChanged()

	val elapsedTimeProto: Flow<Long> = dataStore.data.map { data ->
		data.elapsedTime
	}.distinctUntilChanged()

	private fun mapToDomainSudokuGrid(grid: SudokuGridProto): SudokuGrid {
		val cellData = grid.dataList.map { cell -> mapToDomainCellData(cell) }.toTypedArray()

		return if (grid.seed != 0L) {
			SudokuGrid.fromCellData(cellData).withSeed(grid.seed)
		} else {
			SudokuGrid.fromCellData(cellData)
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

	suspend fun updateDifficulty(difficulty: SudokuDifficulty) {
		dataStore.updateData { proto ->
			proto.toBuilder()
				.setDifficulty(
					when (difficulty) {
						SudokuDifficulty.EASY -> SudokuGridProto.Difficulty.DIFFICULTY_EASY
						SudokuDifficulty.MEDIUM -> SudokuGridProto.Difficulty.DIFFICULTY_MEDIUM
						SudokuDifficulty.HARD -> SudokuGridProto.Difficulty.DIFFICULTY_HARD
						SudokuDifficulty.EXPERT -> SudokuGridProto.Difficulty.DIFFICULTY_EXPERT
					}
				).build()
		}
	}

	suspend fun updateElapsedTime(elapsedTime: Long) {
		dataStore.updateData { proto ->
			proto.toBuilder()
				.setElapsedTime(elapsedTime)
				.build()
		}
	}
}
