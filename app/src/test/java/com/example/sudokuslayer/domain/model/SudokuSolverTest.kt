package com.example.sudokuslayer.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SudokuSolverRowTest(
    private val row: IntArray,
    private val expected: Boolean
) {
    private val solver = ClassicSudokuSolver()

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

    private val solver = ClassicSudokuSolver()

    @Test
    fun `should validate subgrid correctly`(){
        assertEquals(expected, solver.checkSubgrid(subgrid))
    }
}