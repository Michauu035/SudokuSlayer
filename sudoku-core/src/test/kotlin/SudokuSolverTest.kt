import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.solver.ClassicSudokuSolver
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
}