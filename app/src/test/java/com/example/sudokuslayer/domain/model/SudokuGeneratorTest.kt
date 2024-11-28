package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import org.junit.Assert.assertEquals
import org.junit.Test

class SudokuGeneratorTest {
    private val generator = ClassicSudokuGenerator()
    @Test
    fun `should generate valid sudoku grid`(){
        val expected = true
        val grid = generator.generateSudokuGrid()
        val result = ClassicSudokuSolver().isValidSolution(grid)
        assertEquals(expected, result)
    }

    @Test
    fun `should generate identical grids given the same seed`(){
        val grid1 = generator.generateSudokuGrid(0)
        val grid2 = generator.generateSudokuGrid(0)
        assertEquals(grid1.toString(), grid2.toString())
    }

    @Test
    fun `should fill partially full grid`(){
        val grid = arrayOf(
            intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            intArrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            intArrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            intArrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
        val sudoku = SudokuGrid(grid)
        generator.fillGrid(sudoku, 0)
        val expected = true
        val result = ClassicSudokuSolver().isValidSolution(sudoku)
        assertEquals(expected, result)
    }
}