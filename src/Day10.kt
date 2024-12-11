import androidx.compose.ui.unit.IntOffset

fun main() {
    val cardinalOffsets = listOf(
        IntOffset(0, 1),
        IntOffset(-1, 0),
        IntOffset(1, 0),
        IntOffset(0, -1),
    )

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map(Char::digitToInt) }

        var score = 0

        grid.indices.forEach { row ->
            grid[row].indices.forEach { col ->
                if (grid[row][col] == 0) {
                    val searchedCells = mutableSetOf<IntOffset>()
                    val cellsToSearch = mutableListOf(0 to IntOffset(col, row))

                    while (cellsToSearch.isNotEmpty()) {
                        val (cellValue, cell) = cellsToSearch.removeFirst()
                        if (searchedCells.contains(cell)) continue
                        searchedCells.add(cell)
                        if (cellValue == 9) {
                            score++
                        }
                        cardinalOffsets.forEach { offset ->
                            val newPos = cell + offset
                            val newPosValue = grid.getOrNull(newPos.y)?.getOrNull(newPos.x)
                            if (newPosValue == cellValue + 1) {
                                cellsToSearch.add(newPosValue to newPos)
                            }
                        }
                    }
                }
            }
        }

        return score
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map(Char::digitToInt) }

        var score = 0

        grid.indices.forEach { row ->
            grid[row].indices.forEach { col ->
                if (grid[row][col] == 0) {
                    val searchedCells = mutableSetOf<IntOffset>()
                    val cellsToSearch = mutableMapOf(IntOffset(col, row) to (0 to 1))

                    while (cellsToSearch.isNotEmpty()) {
                        val cell = cellsToSearch.keys.first()
                        val (cellValue, cellCount) = cellsToSearch.remove(cell)!!
                        if (searchedCells.contains(cell)) continue
                        searchedCells.add(cell)
                        if (cellValue == 9) {
                            score += cellCount
                        }
                        cardinalOffsets.forEach { offset ->
                            val newPos = cell + offset
                            val newPosValue = grid.getOrNull(newPos.y)?.getOrNull(newPos.x)
                            if (newPosValue == cellValue + 1) {
                                cellsToSearch[newPos] = newPosValue to (cellsToSearch[newPos]?.second ?: 0) + cellCount
                            }
                        }
                    }
                }
            }
        }

        return score
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}


