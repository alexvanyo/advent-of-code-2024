fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("DayXX_test")
    check(part1(testInput) == 1)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("DayXX")
    part1(input).println()
    part2(input).println()
}
