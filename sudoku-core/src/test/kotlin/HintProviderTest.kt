import com.example.sudoku.model.House
import com.example.sudoku.model.SudokuGrid
import com.example.sudoku.solver.Hint
import com.example.sudoku.solver.HintProvider
import com.example.sudoku.solver.HintType
import com.example.sudoku.solver.fillCandidates
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
		val hintProvider = HintProvider()
		val possibleValues = hintProvider.getPossibleValues(grid.getArray(), 0, 1)
		assert(possibleValues.containsAll(setOf(3, 6, 8, 9)))
	}

	@Test
	fun `should find naked single`() {
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		val nakedSingle = hintProvider.findNakedSingle(updatedGrid)
		val (row, col, value, type) = nakedSingle!!
		assert(
			row == 3 && col == 2 && value == 8 && type == HintType.NakedSingle
		)
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
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		
		// Generate Houses from updatedGrid (using Row houses in this test)
		val houses = mutableListOf<House>()
		(0..8).forEach { i ->
			houses.add(House.Row(updatedGrid.filter { it.row == i }, i))
			houses.add(House.Column(updatedGrid.filter { it.col == i }, i))
			houses.add(House.Block(updatedGrid.filter { it.row / 3 == i / 3 && it.col / 3 == i % 3 }, i))
		}
		
		// Loop over houses and find a hidden single in one house
		var hiddenSingle: Hint? = null
		for (house in houses) {
			hiddenSingle = hintProvider.findHiddenSingle(house)
			if (hiddenSingle != null) break
		}
		
//		val (row, col, value, type) = hiddenSingle!!
		assert(hiddenSingle != null)
	}

	@Test
	fun `should find locked candidate`() {
		val grid = SudokuGrid.fromStringArray(
			arrayOf(
				"521349678",
				"008167352",
				"673000004",
				"180400030",
				"204700800",
				"006000045",
				"009083001",
				"360014580",
				"810070403"
			)
		)
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		// Generate houses from updatedGrid
		val houses = mutableListOf<House>()
		(0..8).forEach { row ->
			houses.add(House.Row(updatedGrid.filter { it.row == row }, row))
		}
		(0..8).forEach { col ->
			houses.add(House.Column(updatedGrid.filter { it.col == col }, col))
		}
		(0 until 3).forEach { blockRow ->
			(0 until 3).forEach { blockCol ->
				val blockCells = updatedGrid.filter { it.row / 3 == blockRow && it.col / 3 == blockCol }
				val blockId = blockRow * 3 + blockCol
				houses.add(House.Block(blockCells, blockId))
			}
		}
		var lockedCandidates: List<Hint> = emptyList()
		for (house in houses) {
			hintProvider.findLockedCandidate(house, updatedGrid)?.let { lockedCandidates = it }
			if (lockedCandidates.isNotEmpty()) break
		}
		assert(lockedCandidates.isNotEmpty())
	}

	@Test
	fun `should find pointing candidates`() {
		val grid = SudokuGrid.fromStringArray(
			arrayOf(
				"521349678",
				"008167352",
				"673000004",
				"180400030",
				"204700800",
				"006000045",
				"009083001",
				"360014580",
				"810070403"
			)
		)
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		val blockHouses = mutableListOf<House.Block>()
		(0 until 3).forEach { blockRow ->
			(0 until 3).forEach { blockCol ->
				val blockCells = updatedGrid.filter { it.row / 3 == blockRow && it.col / 3 == blockCol }
				val blockId = blockRow * 3 + blockCol
				blockHouses.add(House.Block(blockCells, blockId))
			}
		}
		val pointingCandidates = mutableListOf<List<Hint>>()
		for (house in blockHouses) {
			val hint = hintProvider.findPointingCandidates(house, updatedGrid)
			if (hint.isNotEmpty())
				pointingCandidates += hint
		}
		println(pointingCandidates.joinToString("\n"))
		println(pointingCandidates.first().first().run {
			explanationStrategy?.generateHintExplanationSteps(grid, this)?.joinToString("\n")
		})
		assert(pointingCandidates.isNotEmpty())
	}

	@Test
	fun `should find claiming candidates`() {
		val grid = SudokuGrid.fromStringArray(
			arrayOf(
				"521349678",
				"008167352",
				"673000004",
				"180400030",
				"204700800",
				"006000045",
				"009083001",
				"360014580",
				"810070403"
			)
		)
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		val houses = mutableListOf<House>()
		(0..8).forEach { row ->
			houses.add(House.Row(updatedGrid.filter { it.row == row }, row))
		}
		(0..8).forEach { col ->
			houses.add(House.Column(updatedGrid.filter { it.col == col }, col))
		}
		val claimingCandidates = mutableListOf<Hint>()
		houses.forEach { house ->
			val hint = hintProvider.findClaimingCandidates(house, updatedGrid)
			claimingCandidates += hint
		}
		assert(claimingCandidates.isNotEmpty())
	}

	@Test
	fun `should return null for no locked candidates`() {
		val grid = SudokuGrid.fromStringArray(
			arrayOf(
				"123456789",
				"456789123",
				"789123456",
				"234567891",
				"567891234",
				"891234567",
				"345678912",
				"678912345",
				"912345678"
			)
		)
		val hintProvider = HintProvider()
		val updatedGrid = hintProvider.fillCandidates(grid.getArray())
		val houses = mutableListOf<House>()
		(0..8).forEach { row ->
			houses.add(House.Row(updatedGrid.filter { it.row == row }, row))
		}
		(0..8).forEach { col ->
			houses.add(House.Column(updatedGrid.filter { it.col == col }, col))
		}
		(0 until 3).forEach { blockRow ->
			(0 until 3).forEach { blockCol ->
				val blockCells = updatedGrid.filter { it.row / 3 == blockRow && it.col / 3 == blockCol }
				val blockId = blockRow * 3 + blockCol
				houses.add(House.Block(blockCells, blockId))
			}
		}
		for (house in houses) {
			val lockedCandidate = hintProvider.findLockedCandidate(house, updatedGrid)
			assertNull(lockedCandidate)
		}
	}

	@Test
	fun `should return null for empty grid`() {
		val grid = SudokuGrid()
		val hintProvider = HintProvider()
		val time = measureTime {
			val hint = hintProvider.provideHint(data = grid.getArray())
			assertNull(hint)
		}
		println("Time: $time")
	}
}