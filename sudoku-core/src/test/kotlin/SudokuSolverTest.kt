import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.solver.ClassicSudokuSolver
import com.example.sudoku.solver.createConstraints
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@DisplayName("Sudoku Solver")
class SudokuSolverTest {

	companion object {
		@JvmStatic
		fun rowTestData(): Stream<Arguments> = Stream.of(
			Arguments.of(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9), true),
			Arguments.of(intArrayOf(0, 2, 3, 0, 5, 6, 7, 8, 9), true),
			Arguments.of(intArrayOf(0, 2, 3, 4, 5, 6, 7, 8, 8), false),
			Arguments.of(intArrayOf(0, 0, 3, 0, 0, 3, 7, 0, 0), false)
		)

		@JvmStatic
		fun subgridTestData(): Stream<Arguments> = Stream.of(
			Arguments.of(
				arrayOf(
					intArrayOf(1, 2, 3),
					intArrayOf(4, 5, 6),
					intArrayOf(7, 8, 9)
				), true
			),
			Arguments.of(
				arrayOf(
					intArrayOf(1, 2, 3),
					intArrayOf(4, 5, 6),
					intArrayOf(7, 8, 8)
				), false
			),
			Arguments.of(
				arrayOf(
					intArrayOf(1, 0, 0),
					intArrayOf(0, 5, 0),
					intArrayOf(0, 0, 9)
				), true
			),
			Arguments.of(
				arrayOf(
					intArrayOf(1, 0, 0),
					intArrayOf(0, 0, 1),
					intArrayOf(0, 0, 9)
				), false
			)
		)
	}

	@Nested
	@DisplayName("Row Validation")
	inner class RowValidation {
		@ParameterizedTest(name = "Row {0} should be {1}")
		@MethodSource("SudokuSolverTest#rowTestData")
		fun validateRow(row: IntArray, expected: Boolean) {
			assertEquals(expected, ClassicSudokuSolver.checkRow(row))
		}

	}

	@Nested
	@DisplayName("Subgrid Validation")
	inner class SubgridValidation {
		@ParameterizedTest(name = "Subgrid case {index}")
		@MethodSource("SudokuSolverTest#subgridTestData")
		fun validateSubgrid(subgrid: Array<IntArray>, expected: Boolean) {
			assertEquals(
				expected,
				ClassicSudokuSolver.checkSubgrid(subgrid.flatMap { it.toList() }.toIntArray()),
				"Subgrid validation failed"
			)
		}
	}

	@Nested
	@DisplayName("Move Validation")
	inner class MoveValidation {
		private val sudoku = SudokuGrid.fromIntArray(
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

		@Test
		@DisplayName("Should reject invalid row move")
		fun invalidRowMove() {
			assertEquals(
				false,
				ClassicSudokuSolver.isValidMove(sudoku, 0, 2, 3),
				"Move should be invalid due to row conflict"
			)
		}

		@Test
		@DisplayName("Should reject invalid column move")
		fun invalidColumnMove() {
			assertEquals(
				false,
				ClassicSudokuSolver.isValidMove(sudoku, 2, 4, 7),
				"Move should be invalid due to column conflict"
			)
		}

		@Test
		@DisplayName("Should reject invalid subgrid move")
		fun invalidSubgridMove() {
			assertEquals(
				false,
				ClassicSudokuSolver.isValidMove(sudoku, 1, 1, 6),
				"Move should be invalid due to subgrid conflict"
			)
		}

		@Test
		@DisplayName("Should accept valid move")
		fun validMove() {
			assertEquals(
				true,
				ClassicSudokuSolver.isValidMove(sudoku, 0, 2, 1),
				"Move should be valid"
			)
		}
	}

	@Nested
	@DisplayName("Grid Filling")
	inner class GridFilling {
		@Test
		@DisplayName("Should correctly fill partially complete grid")
		fun fillPartialGrid() {
			val grid = SudokuGrid.fromIntArray(
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

			ClassicSudokuSolver.fillGrid(grid)

			assertEquals(
				true,
				ClassicSudokuSolver.isValidSolution(grid),
				"Filled grid should be a valid solution"
			)
		}
	}

	@Nested
	@DisplayName("Unique Solution Validation")
	inner class UniqueSolutionValidation {
		@Test
		@DisplayName("Should detect non-unique solution")
		fun detectNonUniqueSolution() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 1, 2),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
				)
			)
			assertEquals(false, ClassicSudokuSolver.hasUniqueSolution(grid))
		}
	}

	@Nested
	@DisplayName("Edge Cases")
	inner class EdgeCases {
		@Test
		@DisplayName("Should handle empty grid")
		fun handleEmptyGrid() {
			val grid = SudokuGrid()
			assertEquals(true, ClassicSudokuSolver.fillGrid(grid))
			assertEquals(true, ClassicSudokuSolver.isValidSolution(grid))
		}

		@Test
		@DisplayName("Should handle nearly complete grid")
		fun handleNearlyCompleteGrid() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
					intArrayOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
					intArrayOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
					intArrayOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
					intArrayOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
					intArrayOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
					intArrayOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
					intArrayOf(2, 8, 7, 4, 1, 9, 6, 3, 0), // Only one cell empty
					intArrayOf(3, 4, 5, 2, 8, 6, 1, 7, 9)
				)
			)
			println(grid)
			val isSolved = ClassicSudokuSolver.solve(grid)
			println("====")
			println(grid)
			assertEquals(true, isSolved)
			assertEquals(true, ClassicSudokuSolver.isValidSolution(grid))
		}

		@Test
		@DisplayName("Should detect unsolvable grid")
		fun detectUnsolvableGrid() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(5, 5, 0, 0, 0, 0, 0, 0, 0), // Two 5s in first row makes it unsolvable
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
				)
			)

			assertEquals(false, ClassicSudokuSolver.fillGrid(grid))
		}
	}

	@Nested
	@DisplayName("Best Cell Selection")
	inner class BestCellSelection {
		@Test
		@DisplayName("Should select cell with minimum possibilities")
		fun selectMinimumPossibilitiesCell() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 0), // Only one possibility for (0,8)
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
				)
			)
			val bestCell = ClassicSudokuSolver.findBestCell(grid)
			assertEquals(Pair(0, 8), bestCell)
		}

		@Test
		@DisplayName("Should return null for complete grid")
		fun returnNullForCompleteGrid() {
			val grid = SudokuGrid.fromIntArray(
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
			val bestCell = ClassicSudokuSolver.findBestCell(grid)
			assertEquals(null, bestCell)
		}
	}

	@Nested
	@DisplayName("Unsolvable Grids")
	inner class UnsolvableGrids {
		@Test
		@DisplayName("Should detect grid with no valid moves for a cell")
		fun detectNoValidMovesGrid() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 0),
					intArrayOf(4, 5, 6, 7, 8, 9, 1, 2, 3),
					intArrayOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 9)
				)
			)

			assertEquals(false, ClassicSudokuSolver.fillGrid(grid))
		}

		@Test
		@DisplayName("Should detect grid with invalid initial state")
		fun detectInvalidInitialState() {
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(1, 1, 0, 0, 0, 0, 0, 0, 0), // Duplicate 1s in first row
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
				)
			)
			assertEquals(false, ClassicSudokuSolver.fillGrid(grid))
		}
	}

	@Nested
	@DisplayName("Performance Tests")
	inner class PerformanceTests {
		@Test
		@DisplayName("Should solve anti-brute-force puzzle")
		fun solveAntiBruteForceGrid() {
			// This is the puzzle designed to be hard for brute force algorithms
			// from https://en.wikipedia.org/wiki/Sudoku_solving_algorithms
			val grid = SudokuGrid.fromIntArray(
				arrayOf(
					intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 0, 3, 0, 8, 5),
					intArrayOf(0, 0, 1, 0, 2, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 5, 0, 7, 0, 0, 0),
					intArrayOf(0, 0, 4, 0, 0, 0, 1, 0, 0),
					intArrayOf(0, 9, 0, 0, 0, 0, 0, 0, 0),
					intArrayOf(5, 0, 0, 0, 0, 0, 0, 7, 3),
					intArrayOf(0, 0, 2, 0, 1, 0, 0, 0, 0),
					intArrayOf(0, 0, 0, 0, 4, 0, 0, 0, 9)
				)
			)

			val solved = ClassicSudokuSolver.solve(grid)
			assertEquals(true, solved, "Puzzle should be solvable")
			assertEquals(true, ClassicSudokuSolver.isValidSolution(grid), "Solution should be valid")
		}
	}
}

class CreateConstraintsTest {

	@Test
	fun `test createConstraints for top-left cell in 9x9 grid`() {
		val result = createConstraints(0, 0, 0, 9)
		// Calculation:
		// rowColConstraint = 0 * 9 + 0 = 0
		// rowNumConstraint = 9 * 9 + 0 * 9 + 0 = 81
		// colNumConstraint = 2 * 9 * 9 + 0 * 9 + 0 = 162
		// getBoxIndex(0, 0) = (0 / 3) * 3 + (0 / 3) = 0, boxNumConstraint = 3 * 9 * 9 + 0 * 9 + 0 = 243
		val expected = listOf(0, 81, 162, 243)
		assertEquals(expected, result)
	}

	@Test
	fun `test createConstraints for bottom-right cell in 9x9 grid`() {
		val result = createConstraints(8, 8, 8, 9)
		// Calculation:
		// rowColConstraint = 8 * 9 + 8 = 80
		// rowNumConstraint = 9 * 9 + 8 * 9 + 8 = 81 + 72 + 8 = 161
		// colNumConstraint = 2 * 9 * 9 + 8 * 9 + 8 = 162 + 72 + 8 = 242
		// getBoxIndex(8, 8) = (8 / 3) * 3 + (8 / 3) = 2 * 3 + 2 = 8, 
		// boxNumConstraint = 3 * 9 * 9 + 8 * 9 + 8 = 243 + 72 + 8 = 323
		val expected = listOf(80, 161, 242, 323)
		assertEquals(expected, result)
	}

	@Test
	fun `test createConstraints for a cell in 4x4 grid`() {
		val result = createConstraints(1, 2, 3, 4)
		// For gridSize 4: subgridSize = sqrt(4) = 2
		// rowColConstraint = 1 * 4 + 2 = 6
		// rowNumConstraint = 4 * 4 + 1 * 4 + 3 = 16 + 4 + 3 = 23
		// colNumConstraint = 2 * 4 * 4 + 2 * 4 + 3 = 32 + 8 + 3 = 43
		// getBoxIndex(1, 2) = (1 / 2) * 2 + (2 / 2) = 0 * 2 + 1 = 1,
		// boxNumConstraint = 3 * 4 * 4 + 1 * 4 + 3 = 48 + 4 + 3 = 55
		val expected = listOf(6, 23, 43, 55)
		assertEquals(expected, result)
	}
}
