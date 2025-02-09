package com.example.sudoku.solver

import com.example.sudoku.dlxalgorithm.DLXAlgorithm.solve
import com.example.sudoku.dlxalgorithm.DLXAlgorithm.solveSuspend
import com.example.sudoku.dlxalgorithm.DancingLinksMatrix
import com.example.sudoku.model.SudokuGrid
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.take
import kotlin.random.Random

object ClassicSudokuSolver : SudokuSolver {
    var random: Random = Random(Random.nextLong())
    
    // Bitwise mask for numbers 1-9
    private const val VALID_MASK = 0b1111111110

    private fun numbersToMask(numbers: Iterable<Int>): Int =
        numbers.fold(0) { acc, num -> acc or (1 shl num) }

    override fun checkRow(row: IntArray): Boolean {
        var mask = 0
        row.forEach { num ->
            if (num != 0) {
                if (mask and (1 shl num) != 0) return false
                mask = mask or (1 shl num)
            }
        }
        return true
    }

    override fun checkColumn(col: IntArray): Boolean = checkRow(col)

    override fun checkSubgrid(subgrid: IntArray): Boolean = checkRow(subgrid)

    override fun isValidMove(
        sudoku: SudokuGrid,
        rowNum: Int,
        colNum: Int,
        num: Int
    ): Boolean {
        val rowMask = numbersToMask(sudoku.getRow(rowNum).map { it.number })
        if (rowMask and (1 shl num) != 0) return false
        
        val colMask = numbersToMask(sudoku.getCol(colNum).map { it.number })
        if (colMask and (1 shl num) != 0) return false
        
        val subgridMask = numbersToMask(sudoku.getSubgrid(rowNum, colNum).map { it.number })
        return subgridMask and (1 shl num) == 0
    }

    override fun checkGrid(sudoku: SudokuGrid): Boolean {
        for (i in 0..8) {
            if (!checkRow(sudoku.getRow(i).map { it.number }.toIntArray())) return false
            if (!checkColumn(sudoku.getCol(i).map { it.number }.toIntArray())) return false
        }
        for (row in 0..6 step 3) {
            for (col in 0..6 step 3) {
                if (!checkSubgrid(sudoku.getSubgrid(row, col).map { it.number }.toIntArray())) return false
            }
        }
        return true
    }

    override fun isValidSolution(sudoku: SudokuGrid): Boolean {
        val validGrid = checkGrid(sudoku)
        val noZeros = 0 !in sudoku.getArray().map { it.number }
        return validGrid && noZeros
    }

    private fun isSolvable(sudoku: SudokuGrid): Boolean {
        // Check if current state is valid
        if (!checkGrid(sudoku)) return false
        
        // Check if each empty cell has at least one valid move
        for (row in 0..8) {
            for (col in 0..8) {
                if (sudoku[row, col].number == 0) {
                    val validMoves = getValidMoves(sudoku, row, col)
                    if (validMoves.isEmpty()) return false
                }
            }
        }
        return true
    }

    override fun fillGrid(sudoku: SudokuGrid): Boolean {
        // First check if the grid is solvable
        if (!isSolvable(sudoku)) return false
        
        val bestCell = findBestCell(sudoku) ?: return true
        val (row, col) = bestCell
        
        val validMoves = getValidMoves(sudoku, row, col)
        if (validMoves.isEmpty()) return false
        
        for (num in validMoves.shuffled(sudoku.random)) {
            sudoku[row, col] = num
            if (fillGrid(sudoku)) return true
            sudoku[row, col] = 0
        }
        
        return false
    }

    fun solve(sudoku: SudokuGrid): Boolean {
        val dlxMatrix = DancingLinksMatrix.fromSudoku(sudoku)
        val result = mutableListOf<Int>()
        dlxMatrix.rootNode.printNotEmptyNodes()

        dlxMatrix.rootNode.solve { result.addAll(it) }

        if (result.isEmpty()) return false

        result.toSudokuGrid(sudoku).getArray().forEach { cell ->
            sudoku[cell.row, cell.col] = cell.number
        }
        return true
    }

    private fun getValidMoves(sudoku: SudokuGrid, row: Int, col: Int): List<Int> {
        val rowMask = numbersToMask(sudoku.getRow(row).map { it.number })
        val colMask = numbersToMask(sudoku.getCol(col).map { it.number })
        val subgridMask = numbersToMask(sudoku.getSubgrid(row, col).map { it.number })
        
        val invalidMask = rowMask or colMask or subgridMask
        val validMask = VALID_MASK and invalidMask.inv()
        
        return (1..9).filter { validMask and (1 shl it) != 0 }
    }

    override suspend fun hasUniqueSolution(sudoku: SudokuGrid): Boolean = coroutineScope {
        val dancingLinksMatrix = DancingLinksMatrix.fromSudoku(sudoku)
        val result = mutableListOf<List<Int>>()

        solveSuspend(dancingLinksMatrix.rootNode)
            .consumeAsFlow()
            .take(2)
            .collect { result.add(it) }

        result.size == 1
    }

    fun findBestCell(sudoku: SudokuGrid): Pair<Int, Int>? {
        var bestRow = -1
        var bestCol = -1
        var minPossibilities = 10

        for (row in 0..8) {
            for (col in 0..8) {
                if (sudoku[row, col].number == 0) {
                    val possibilities = getValidMoves(sudoku, row, col).size
                    if (possibilities < minPossibilities) {
                        minPossibilities = possibilities
                        bestRow = row
                        bestCol = col
                        if (possibilities == 1) return bestRow to bestCol // Early termination
                    }
                }
            }
        }
        return if (bestRow != -1 && bestCol != -1) bestRow to bestCol else null
    }
}