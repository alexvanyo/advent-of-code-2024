fun main() {
    fun part1(input: List<String>): Int =
        Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(input.joinToString()).sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }

    fun part2(input: List<String>): Int {
        var isEnabled = true
        return Regex("""mul\((\d{1,3}),(\d{1,3})\)|(don't\(\))|(do\(\))""").findAll(input.joinToString("")).sumOf {
            if (it.groupValues[3].isNotEmpty()) {
                isEnabled = false
                0
            } else if (it.groupValues[4].isNotEmpty()) {
                isEnabled = true
                0
            } else if (isEnabled) {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            } else {
                0
            }
        }
    }

    // Or read a large test input from the `src/Day03_test.txt` file:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)

    // Read the input from the `src/Day03.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
