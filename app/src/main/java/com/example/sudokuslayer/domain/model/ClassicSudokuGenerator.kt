package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import kotlin.random.Random

class ClassicSudokuGenerator : SudokuGenerator {
    override fun generateSudokuGrid(seed: Long): SudokuGrid {
        val grid = SudokuGrid()
        fillGrid(grid, seed)
        return grid
    }

    fun fillGrid(sudoku: SudokuGrid, seed: Long): Boolean {
        val solver = ClassicSudokuSolver()
        for (row in 0..8) {
            for (col in 0..8) {
                // Find empty cell
                if (sudoku[row, col] == 0) {
                    val numbers = (1..9).shuffled(Random(seed))

                    // Find number that is safe to place in cell
                    for (num in numbers) {
                        if (
                            solver.isValidMove(
                                sudoku = sudoku,
                                rowNum = row,
                                colNum = col,
                                num = num
                            )
                        ) {
                            sudoku[row, col] = num
                            if (fillGrid(sudoku, seed)) {
                                return true
                            }
                            sudoku[row, col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }
}