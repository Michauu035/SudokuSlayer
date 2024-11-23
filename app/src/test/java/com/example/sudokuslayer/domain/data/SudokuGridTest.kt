package com.example.sudokuslayer.domain.data

import com.example.sudokuslayer.SudokuGrid
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class SudokuGridTest {
    private val grid: SudokuGrid = SudokuGrid(
        arrayOf(
            intArrayOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
            intArrayOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
            intArrayOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
            intArrayOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
            intArrayOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
            intArrayOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
            intArrayOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
            intArrayOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
            intArrayOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
        )
    )

    @Test
    fun `should return correct row`(){
        val expected = intArrayOf(2, 8, 7, 4, 1, 9, 6, 3, 5)
        val result = grid.getRow(7)
        assertArrayEquals(expected, result)
    }

    @Test
    fun `should return correct col`(){
        val expected = intArrayOf(5, 6, 1, 8, 4, 7, 9, 2, 3)
        val result = grid.getCol(0)
        assertArrayEquals(expected, result)
    }

    @Test
    fun `should return correct subgrid`(){
        val expected = arrayOf(
            intArrayOf(5, 3, 7),
            intArrayOf(4, 1, 9),
            intArrayOf(2, 8, 6),
        )
        val result = grid.getSubgrid(5, 7)
        assertArrayEquals(expected, result)
    }
}