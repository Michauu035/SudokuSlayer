package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.CellAttributes
import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ClassicSudokuGenerator : SudokuGenerator {
    override fun createSudoku(cellsToRemove: Int, seed: Long): SudokuGrid {
        var sudoku = SudokuGrid(seed)
        ClassicSudokuSolver.fillGrid(sudoku)
        sudoku = removeCells(sudoku, cellsToRemove)
        for (cell in sudoku) {
            if (cell.number != 0)
                cell.attributes += CellAttributes.GENERATED
        }
        return sudoku
    }

    override fun generateFullSudokuGrid(seed: Long): SudokuGrid {
        val sudoku = SudokuGrid(seed)
        ClassicSudokuSolver.fillGrid(sudoku)
        return sudoku
    }

    // TODO: remove in batches to improve performance

    override fun removeCells(
        sudoku: SudokuGrid,
        cellsToRemove: Int,
    ): SudokuGrid {
        val removedGrid: SudokuGrid = sudoku.clone()
        val totalCells = 81
        var removedCount = 0
        val cellIndices = (0 until totalCells).shuffled(sudoku.random)

            for (index in cellIndices) {
                if (removedCount >= cellsToRemove) break

                val row = index / 9
                val col = index % 9
                val orginalValue = removedGrid[row, col].number

                removedGrid[row, col] = 0

                if (!ClassicSudokuSolver.hasUniqueSolution(removedGrid)) {
                    removedGrid[row, col] = orginalValue
                } else {
                    removedCount++
                }
            }

        return removedGrid
    }
}