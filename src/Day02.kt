import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int =
        input.map(String::parseIntList).map(::isSafe).count { it }


    fun part2(input: List<String>): Int =
        input.map(String::parseIntList).map { isSafe(it, 1) }.count { it }


    // Or read a large test input from the `src/Day02_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput).also { it.println() } == 2)

    // Read the input from the `src/Day02.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun isSafe(levels: List<Int>, allowedProblems: Int = 0): Boolean {
    if (allowedProblems < 0) return false
    var prev = levels.first()
    var curr = levels[1]
    val isIncreasing = prev > curr
    if (abs(prev - curr) !in 1..3) {
        return isSafe(levels.removeIndices(0), allowedProblems - 1) ||
            isSafe(levels.removeIndices(1), allowedProblems - 1)
    }

    for (i in 2 until levels.size) {
        prev = curr
        curr = levels[i]
        if (prev > curr != isIncreasing) {
            return isSafe(levels.removeIndices(i), allowedProblems - 1) ||
                isSafe(levels.removeIndices(i - 1), allowedProblems - 1) ||
                // handle case where initial 2 caused wrong direction
                (i == 2 && isSafe(levels.removeIndices(0), allowedProblems - 1))
        }
        if (abs(prev - curr) !in 1..3) {
            return isSafe(levels.removeIndices(i), allowedProblems - 1) ||
                isSafe(levels.removeIndices(i - 1), allowedProblems - 1)
        }
    }
    return true
}
