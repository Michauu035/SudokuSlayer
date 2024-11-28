package com.example.sudokuslayer.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SudokuGeneratorTest {
    private val generator = ClassicSudokuGenerator()
    @Test
    fun `should generate valid sudoku grid`(){
        val expected = true
        val grid = generator.generateSudokuGrid()
        val result = ClassicSudokuSolver.isValidSolution(grid)
        assertEquals(expected, result)
    }

    @Test
    fun `should generate identical full grids given the same seed`(){
        var grid1 = generator.generateSudokuGrid(0)
        grid1 = generator.removeCells(grid1, 53, 0)
        var grid2 = generator.generateSudokuGrid(0)
        grid2 = generator.removeCells(grid2, 53, 0)
        assertEquals(grid1.toString(), grid2.toString())
    }
}