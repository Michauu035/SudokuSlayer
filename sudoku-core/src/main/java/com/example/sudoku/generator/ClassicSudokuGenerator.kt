package com.example.sudoku.generator

import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.model.SudokuGrid.Companion.withSeed
import com.example.sudoku.solver.ClassicSudokuSolver

class ClassicSudokuGenerator : SudokuGenerator {
    override suspend fun createSudoku(cellsToRemove: Int, seed: Long): SudokuGrid {
        var sudoku = SudokuGrid().withSeed(seed)
        ClassicSudokuSolver.fillGrid(sudoku)
        sudoku = removeCells(sudoku, cellsToRemove)
        sudoku.lockGeneratedCells()
        return sudoku
    }

    override fun generateFullSudokuGrid(seed: Long): SudokuGrid {
        val sudoku = SudokuGrid().withSeed(seed)
        ClassicSudokuSolver.fillGrid(sudoku)
        return sudoku
    }

    // TODO: remove in batches to improve performance

    override suspend fun removeCells(
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