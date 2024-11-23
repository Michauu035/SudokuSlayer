package com.example.sudokuslayer.domain.data

import java.lang.IndexOutOfBoundsException
import kotlin.math.floor

class SudokuGrid(
    var grid: Array<IntArray>
) {

    /**
     * Get row of sudoku grid
     * @param row row number starting from 0
     * @return returns IntArray containing numbers in given row
     */
    fun getRow(row: Int): IntArray {
        return grid[row]
    }

    /**
     * Get column of sudoku grid
     * @param col column number starting from 0
     * @return returns IntArray containing numbers in given column
     */
    fun getCol(col: Int): IntArray {
        val col = grid.map { it[col] }.toIntArray()
        return col
    }

    /**
     * Get subgrid (3x3 area) of sudoku grid
     * @param colNum column number
     * @param rowNum row number
     * @return returns 2d array containing numbers in specified subgrid number
     */
    fun getSubgrid(colNum: Int, rowNum: Int): Array<IntArray> {
        val colStart: Int = floor(colNum / 3.0).toInt() * 3
        val rowStart: Int = floor(rowNum / 3.0).toInt() * 3
        val subgrid = grid.slice(rowStart..rowStart+2).map { it.slice(colStart..colStart+2).toIntArray() }.toTypedArray()
        return subgrid
    }

}