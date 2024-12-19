fun main() {
    data class Towel(
        val stripes: List<Char>,
    )

    data class Design(
        val stripes: List<Char>,
    )

    data class Input(
        val towels: List<Towel>,
        val designs: List<Design>,
    )

    fun parseInput(input: List<String>): Input {
        val towels = input
            .first()
            .split(", ")
            .map { it.toCharArray().toList() }
            .map(::Towel)
        val designs = input
            .drop(2)
            .map { it.toCharArray().toList() }
            .map(::Design)
        return Input(towels, designs)
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        val memoizedMap: MutableMap<List<Char>, List<Towel>?> = mutableMapOf(emptyList<Char>() to emptyList<Towel>())

        fun leastTowels(stripes: List<Char>): List<Towel>? =
            if (stripes in memoizedMap) {
                memoizedMap[stripes]
            } else {
                parsedInput.towels.mapNotNull { towel ->
                    if (stripes.size >= towel.stripes.size && stripes.take(towel.stripes.size) == towel.stripes) {
                        leastTowels(stripes.drop(towel.stripes.size))?.let { listOf(towel) + it }
                    } else {
                        null
                    }
                }
                    .minByOrNull { it.size }
                    .also {
                        memoizedMap[stripes] = it
                    }
            }

        return parsedInput.designs.count { design ->
            leastTowels(design.stripes) != null
        }
    }

    fun part2(input: List<String>): Long {
        val parsedInput = parseInput(input)
        val memoizedMap: MutableMap<List<Char>, Long> = mutableMapOf(emptyList<Char>() to 1L)

        fun leastTowels(stripes: List<Char>): Long =
            if (stripes in memoizedMap) {
                memoizedMap[stripes]!!
            } else {
                parsedInput.towels.sumOf { towel ->
                    if (stripes.size >= towel.stripes.size && stripes.take(towel.stripes.size) == towel.stripes) {
                        leastTowels(stripes.drop(towel.stripes.size))
                    } else {
                        0L
                    }
                }
                    .also {
                        memoizedMap[stripes] = it
                    }
            }

        return parsedInput.designs.sumOf { design ->
            leastTowels(design.stripes)
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
