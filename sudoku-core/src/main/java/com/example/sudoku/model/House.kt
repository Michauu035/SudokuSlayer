package com.example.sudoku.model

sealed interface House {
    val cells: List<SudokuCellData>
    
    class Row(override val cells: List<SudokuCellData>, val rowId: Int) : House
    class Column(override val cells: List<SudokuCellData>, val colId: Int) : House
    class Block(override val cells: List<SudokuCellData>, val rowId: Int, val colId: Int) : House
}