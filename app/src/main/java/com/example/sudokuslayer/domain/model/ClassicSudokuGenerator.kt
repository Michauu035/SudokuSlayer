package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

class ClassicSudokuGenerator : SudokuGenerator {
    override fun createSudoku(cellsToRemove: Int, seed: Long): SudokuGrid {
        var grid = SudokuGrid()
        grid.seed = seed
        ClassicSudokuSolver.fillGrid(grid)
        grid = removeCells(grid, cellsToRemove)
        return grid
    }

    override fun generateSudokuGrid(seed: Long): SudokuGrid {
        val grid = SudokuGrid()
        grid.seed = seed
        ClassicSudokuSolver.fillGrid(grid)
        return grid
    }

    override fun removeCells(
        grid: SudokuGrid,
        cellsToRemove: Int,
    ): SudokuGrid {
        val removedGrid: SudokuGrid = grid.clone()
        var removedCount = 0
        val random = Random(grid.seed)

        while (removedCount < cellsToRemove) {
            val row = random.nextInt(9)
            val col = random.nextInt(9)

            if (removedGrid[row, col].number != 0) {
                val backup = removedGrid[row, col].number

                removedGrid[row, col] = 0
                if (!ClassicSudokuSolver.hasUniqueSolution(removedGrid)) {
                    removedGrid[row, col] = backup
                } else {
                    removedCount++
                }
            }
        }

        return removedGrid
    }
}