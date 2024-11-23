package com.example.sudokuslayer.domain.data

import kotlin.math.floor

class SudokuGrid() {
    private var data: Array<IntArray> = Array(9) { IntArray(9) { 0 } }

    constructor(grid: Array<IntArray>) : this() {
        data = grid
    }

    operator fun get(row: Int, col: Int): Int {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        return data[row][col]
    }

    operator fun set(row: Int, col: Int, value: Int) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        data[row][col] = value
    }

    operator fun iterator(): Iterator<IntArray> {
        return data.iterator()
    }

    /**
     * Get row of sudoku grid
     * @param row row number starting from 0
     * @return returns IntArray containing numbers in given row
     */
    fun getRow(row: Int): IntArray {
        require(row in 0 .. 8) { "Index out of bounds" }
        return data[row]
    }

    /**
     * Get column of sudoku grid
     * @param col column number starting from 0
     * @return returns IntArray containing numbers in given column
     */
    fun getCol(col: Int): IntArray {
        require(col in 0 .. 8) { "Index out of bounds" }
        val col = data.map { it[col] }.toIntArray()
        return col
    }

    /**
     * Get subgrid (3x3 area) of sudoku grid
     * @param colNum column number
     * @param rowNum row number
     * @return returns 2d array containing numbers in specified subgrid number
     */
    fun getSubgrid(rowNum: Int, colNum: Int): Array<IntArray> {
        require(rowNum in 0 .. 8 && colNum in 0 .. 8) { "Index out of bounds" }
        val colStart: Int = floor(colNum / 3.0).toInt() * 3
        val rowStart: Int = floor(rowNum / 3.0).toInt() * 3
        val subgrid = data.slice(rowStart..rowStart+2).map { it.slice(colStart..colStart+2).toIntArray() }.toTypedArray()
        return subgrid
    }

    override fun toString(): String{
        return data.joinToString("\n") {row -> row.joinToString(", ")}
    }

}