package com.example.sudokuslayer.domain.model

import com.example.sudokuslayer.domain.data.SudokuGrid
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SudokuSolverRowTest(
    private val row: IntArray,
    private val expected: Boolean
) {
    private val solver = ClassicSudokuSolver

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun provideRowsForValidation(): Collection<Array<Any>> {
            return listOf(
                arrayOf(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9), true),
                arrayOf(intArrayOf(0, 2, 3, 0, 5, 6, 7, 8, 9), true),
                arrayOf(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 8), false),
                arrayOf(intArrayOf(0, 0, 3, 0, 0, 3, 7, 0, 0), false),
            )
        }
    }

    @Test
    fun `should validate row correctly`(){
        assertEquals(expected, solver.checkRow(row))
    }
}

@RunWith(Parameterized::class)
class SudokuSolverSubgridTest(
    private val subgrid: Array<IntArray>,
    private val expected: Boolean
){
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(
                    // Full correct subgrid
                    arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 6),
                        intArrayOf(7, 8, 9)
                    ),
                    true
                ),
                arrayOf(
                    // Full incorrect subgrid
                    arrayOf(
                        intArrayOf(1, 2, 3),
                        intArrayOf(4, 5, 6),
                        intArrayOf(7, 8, 8),
                    ),
                    false
                ),
                arrayOf(
                    // Not full correct subgrid
                    arrayOf(
                        intArrayOf(1, 0, 0),
                        intArrayOf(0, 5, 0),
                        intArrayOf(0, 0, 9)
                    ),
                    true
                ),
                arrayOf(
                    // Not full incorrect subgrid
                    arrayOf(
                        intArrayOf(1, 0, 0),
                        intArrayOf(0, 0 ,1),
                        intArrayOf(0, 0, 9),
                    ),
                    false
                )
            )
        }
    }

    private val solver = ClassicSudokuSolver

    @Test
    fun `should validate subgrid correctly`(){
        assertEquals(expected, solver.checkSubgrid(subgrid))
    }
}

class SudokuSolverValidMoveTest(){
    private val sudoku = SudokuGrid(
        arrayOf(
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
    )
    private val solver = ClassicSudokuSolver

    @Test
    fun `should return false for incorrect move in row`(){
        val expected = false
        val result = solver.isValidMove(sudoku, 0, 2, 3)
        assertEquals(expected, result)
    }

    @Test
    fun `should return false for incorrect move on column`(){
        val expected = false
        val result = solver.isValidMove(sudoku, 2, 4, 7)
        assertEquals(expected, result)
    }

    @Test
    fun `should return false for incorrect move on subgrid`(){
        val expected = false
        val result = solver.isValidMove(sudoku, 1, 1, 6)
        assertEquals(expected, result)
    }

    @Test
    fun `should return true for correct move`(){
        val expected = true
        val result = solver.isValidMove(sudoku, 0, 2, 1)
        assertEquals(expected, result)
    }
}

class SudokuSolverFillTest(){

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
        ClassicSudokuSolver.fillGrid(sudoku)
        val expected = true
        val result = ClassicSudokuSolver.isValidSolution(sudoku)
        assertEquals(expected, result)
    }
}