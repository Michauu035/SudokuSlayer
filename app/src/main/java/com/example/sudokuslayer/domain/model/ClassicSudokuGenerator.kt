package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

class ClassicSudokuGenerator : SudokuGenerator {
    override fun generateSudokuGrid(seed: Long): SudokuGrid {
        val grid = SudokuGrid()
        ClassicSudokuSolver.fillGrid(grid, seed)
        return grid
    }

    override fun removeCells(
        grid: SudokuGrid,
        cellsToRemove: Int,
        seed: Long
    ): SudokuGrid {
        val removedGrid: Array<IntArray> = grid.getGridAsArray().map { it.clone() }.toTypedArray()
        var removedCount = 0
        val random = Random(seed)

        while (removedCount < cellsToRemove) {
            val row = random.nextInt(9)
            val col = random.nextInt(9)

            if (removedGrid[row][col] != 0) {
                val backup = removedGrid[row][col]

                removedGrid[row][col] = 0
                if (!ClassicSudokuSolver.hasUniqueSolution(SudokuGrid(removedGrid))) {
                    removedGrid[row][col] = backup
                } else {
                    removedCount++
                }
            }
        }

        return SudokuGrid(removedGrid)
    }
}