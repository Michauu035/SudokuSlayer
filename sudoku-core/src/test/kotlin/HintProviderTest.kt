import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.solver.Hint
import com.example.sudoku.solver.HintProvider
import com.example.sudoku.solver.HintType
import junit.framework.TestCase.assertNull
import org.junit.Test
import kotlin.time.measureTime

class HintProviderTest {
	private val grid = SudokuGrid.fromStringArray(
		arrayOf(
			"200150074",
			"001000020",
			"407600013",
			"040200095",
			"070480060",
			"010000430",
			"706000289",
			"004907350",
			"003000040"
		)
	)

	@Test
	fun `should find correct possible values`() {
		val hintProvider = HintProvider(grid.getArray())
		val possibleValues = hintProvider.getPossibleValues(0, 1)
		assert(possibleValues.containsAll(setOf(3, 6, 8, 9)))
	}

	@Test
	fun `should find naked single`() {
		val hintProvider = HintProvider(grid.getArray())
		val nakedSingle = hintProvider.findNakedSingle()
		assert(nakedSingle == Hint(3, 2, 8, HintType.NAKED_SINGLE))
	}

	@Test
	fun `should find hidden single`() {
		val grid = SudokuGrid.fromStringArray(
			arrayOf(
				"900702000",
				"000305091",
				"002016040",
				"200000460",
				"007200000",
				"830009000",
				"400000835",
				"001000000",
				"008007100"
			)
		)
		val hintProvider = HintProvider(grid.getArray())
		val hiddenSingle = hintProvider.findHiddenSingle()

		assert(hiddenSingle == Hint(0, 1, 1, HintType.HIDDEN_SINGLE, "row"))
	}

	@Test
	fun `should return null for empty grid`() {
		val grid = SudokuGrid()
		val hintProvider = HintProvider(grid.getArray())
		val time = measureTime {
			val hint = hintProvider.provideHint()
			assertNull(hint)
		}
		println("Time: $time")
	}
}