package com.example.sudokuslayer.domain.data

import kotlin.math.floor
import kotlin.random.Random

class SudokuGrid() {
    private var data: Array<SudokuCellData> = Array(81) { SudokuCellData(it / 9, it % 9, 0) }
    var seed: Long? = null
        set(value) {
            if(value != null)
                random = Random(value)
            value
        }
    var random: Random = Random(0)
        private set

    constructor(grid: Array<IntArray>) : this() {
        grid.forEachIndexed { i, row ->
            row.forEachIndexed { j, num ->
				data[i*9 + j] = SudokuCellData(
					row = i,
					col = j,
					number = num
				)
            }
        }
    }

    constructor(seed: Long) : this() {
        this.seed = seed
        random = Random(seed)
    }

    constructor(grid: Array<SudokuCellData>) : this() {
        data = grid
    }

    operator fun get(row: Int, col: Int): SudokuCellData {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        return data[getIndex(row, col)]
    }

    operator fun set(row: Int, col: Int, value: Int) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            number = value
        )
    }

    fun set(array: Array<SudokuCellData>) {
        data = array
    }

    operator fun iterator(): Iterator<SudokuCellData> = data.iterator()

    /**
     * Get row of sudoku grid
     * @param row row number starting from 0
     * @return returns IntArray containing numbers in given row
     */
    fun getRow(row: Int): IntArray {
        require(row in 0 .. 8) { "Index out of bounds" }
        return data.filter { it.row == row }.map { it.number }.toIntArray()
    }

    /**
     * Get column of sudoku grid
     * @param col column number starting from 0
     * @return returns IntArray containing numbers in given column
     */
    fun getCol(col: Int): IntArray {
        require(col in 0 .. 8) { "Index out of bounds" }
        val col = data.filter { it.col == col }.map { it.number }.toIntArray()
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
        val filteredCol = data.filter { it.col >= colStart && it.col <= colStart + 2 }
        val subgrid: Array<IntArray> = arrayOf(
            filteredCol.filter { it.row == rowStart }.map { it.number }.toIntArray(),
            filteredCol.filter { it.row == rowStart + 1 }.map { it.number }.toIntArray(),
            filteredCol.filter { it.row == rowStart + 2 }.map { it.number }.toIntArray()
        )
        return subgrid
    }

    override fun toString(): String{
        val rows = data.groupBy { it.row }.values
        val grid = rows.joinToString("\n") { it.joinToString(", ") { it.number.toString() } }
        return grid
    }

    fun getEmptyCellsCount(): Int = data.count { it.number == 0 }

    fun getArray(): Array<SudokuCellData> = data

    fun clone(): SudokuGrid = SudokuGrid(data.clone())

    private fun getIndex(row: Int, col: Int): Int = row * 9 + col

    fun addAttribute(row: Int, col: Int, attribute: CellAttributes) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            attributes = data[index].attributes + attribute
        )
    }

    fun removeAttribute(row: Int, col: Int, attribute: CellAttributes) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            attributes = data[index].attributes - attribute
        )
    }

    fun addCornerNote(row: Int, col: Int, noteNumber: Int) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            cornerNotes = data[index].cornerNotes + noteNumber
        )
    }

    fun removeCornerNote(row: Int, col: Int, noteNumber: Int) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            cornerNotes = data[index].cornerNotes - noteNumber
        )
    }

    fun clearCornerNotes(row: Int, col: Int) {
        require(row in 0 .. 8 && col in 0 .. 8) { "Index out of bounds" }
        val index = getIndex(row, col)
        data[index] = data[index].copy(
            cornerNotes = emptySet()
        )
    }
}