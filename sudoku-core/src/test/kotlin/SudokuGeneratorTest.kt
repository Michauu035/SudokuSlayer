
import com.example.sudoku.generator.ClassicSudokuGenerator
import com.example.sudoku.solver.ClassicSudokuSolver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("Classic Sudoku Generator")
class SudokuGeneratorTest {
    private lateinit var generator: ClassicSudokuGenerator
    
    @BeforeEach
    fun setup() {
        generator = ClassicSudokuGenerator()
    }

    @Test
    @DisplayName("Should generate valid full Sudoku grid")
    fun generateValidFullGrid() {
        val grid = generator.generateFullSudokuGrid(12345L)
        
        assertEquals(
            true,
            ClassicSudokuSolver.isValidSolution(grid),
            "Generated grid should be a valid Sudoku solution"
        )
    }

    @ParameterizedTest(name = "Should remove {0} cells with seed {1}")
    @CsvSource(
        "55, 8153123",
        "40, 8153123",
        "30, 8153123"
    )
    fun removeCells(cellsToRemove: Int, seed: Long) {
        val grid = generator.createSudoku(cellsToRemove, seed)
        
        assertEquals(
            cellsToRemove,
            grid.getArray().count { it.number == 0 },
            "Should have exactly $cellsToRemove empty cells"
        )
    }

    @Test
    @DisplayName("Should generate identical grids with same seed")
    fun generateIdenticalGrids() {
        val seed = 12345L
        val cellsToRemove = 53
        
        val grid1 = generator.createSudoku(cellsToRemove, seed)
        val grid2 = generator.createSudoku(cellsToRemove, seed)
        
        assertEquals(
            grid1.toString(),
            grid2.toString(),
            "Grids generated with same seed should be identical"
        )
    }
}