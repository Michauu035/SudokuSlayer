package com.example.sudokuslayer.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SudokuGeneratorTest {
    private val generator = ClassicSudokuGenerator()
    @Test
    fun `should generate valid sudoku grid`(){
        val expected = true
        val grid = generator.generateSudokuGrid(82391931281823L)
        println(grid.toString())
        val result = ClassicSudokuSolver.isValidSolution(grid)
        assertEquals(expected, result)
    }

    @Test
    fun `should generate identical full grids given the same seed`(){
        var grid1 = generator.createSudoku(53, 12345L)
        var grid2 = generator.createSudoku(53, 12345L)
        println(grid1.toString())
        println("\n")
        println(grid2.toString())
        assertEquals(grid1.toString(), grid2.toString())
    }
}