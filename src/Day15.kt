fun main() {
    sealed interface 

    fun part1(input: List<String>): Int {
        val emptyLine = input.indexOfFirst { it == "" }
        val grid = input.subList(0, emptyLine).map {

        }

        val moveSequence = input.subList(emptyLine + 1, input.size).map {
            it.toCharArray().map {
                when (it) {
                    '>' -> Direction.Right
                    '<' -> Direction.Left
                    '^' -> Direction.Up
                    'v' -> Direction.Down
                    else -> error("Unexpected direction: $it")
                }
            }
        }.flatten()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
