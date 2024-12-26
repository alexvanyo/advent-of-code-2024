fun main() {
    data class Input(
        val locks: List<List<Int>>,
        val keys: List<List<Int>>,
    )

    fun parseInput(input: List<String>): Input {
        val schematics = input.chunked(8)

        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()

        schematics.forEach { lines ->
            val isKey = lines[6] == "#####"
            if (!isKey) {
                check(lines[0] == "#####")
            }
            val heights = (0..4).map { charIndex ->
                val amts = lines.subList(0, 7).map { it[charIndex] }
                val height = if (isKey) {
                    6 - amts.indexOf('#')
                } else {
                    amts.lastIndexOf('#')
                }
                height
            }
            if (isKey) {
                keys.add(heights)
            } else {
                locks.add(heights)
            }
        }

        return Input(
            locks = locks,
            keys = keys,
        )
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)

        println(parsedInput)

        return parsedInput.keys.sumOf { key ->
            parsedInput.locks.count { lock ->
                key.zip(lock).all { (a, b) ->
                    a <= 5 - b
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day25_test")
    check(part1(testInput).also { println(it) } == 3)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day25")
    part1(input).println()
    part2(input).println()
}
