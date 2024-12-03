import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.map(String::parseIntPair)
        val firstList = pairs.map(Pair<Int, Int>::first)
        val secondList = pairs.map(Pair<Int, Int>::second)

        val firstSorted = firstList.sorted()
        val secondSorted = secondList.sorted()

        return firstSorted.zip(secondSorted).sumOf { abs(it.first - it.second) }
    }

    fun part2(input: List<String>): Int {
        val pairs = input.map(String::parseIntPair)
        val firstList = pairs.map(Pair<Int, Int>::first)
        val secondList = pairs.map(Pair<Int, Int>::second)

        val secondCountMap = secondList.groupingBy { it }.eachCount()

        return firstList.sumOf { it * secondCountMap.getOrDefault(it, 0) }
    }

    // Test if implementation meets criteria from the description, like:
    check(
        part1(
        ("3   4\n" +
            "4   3\n" +
            "2   5\n" +
            "1   3\n" +
            "3   9\n" +
            "3   3").lines().toList()
    ).also { it.println() } == 11)

    // Or read a large test input from the `src/Day01_test.txt` file:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
