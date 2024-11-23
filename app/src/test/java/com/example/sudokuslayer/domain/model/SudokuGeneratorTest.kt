package com.example.sudokuslayer.domain.model

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
}