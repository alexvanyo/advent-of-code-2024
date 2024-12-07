import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long = input.sumOf { line ->
        val total = line.substringBefore(':').toLong()
        val values = line.substringAfter(':').trim().split(' ').map(String::toLong)
        if ((0 until (1 shl values.size)).any { operationIndex ->
            values.reduceIndexed { index, acc, i ->
                if ((operationIndex and (1 shl index - 1)) == 0) {
                    acc + i
                } else {
                    acc * i
                }
            } == total
        }) {
            total
        } else {
            0L
        }
    }

    fun part2(input: List<String>): Long = input.sumOf { line ->
        val total = line.substringBefore(':').toLong()
        val values = line.substringAfter(':').trim().split(' ').map(String::toLong)
        if (
            (0 until 3.0.pow(values.size - 1).toInt()).any { operationIndex ->
                values.reduceIndexed { index, acc, i ->
                    val operator = (operationIndex / (3.0.pow(index - 1).toInt())).mod(3)
                    when (operator) {
                        0 -> acc + i
                        1 -> acc * i
                        2 -> (acc.toString() + i.toString()).toLong()
                        else -> error("unknown operator")
                    }
                } == total
            }
        ) {
            total
        } else {
            0L
        }
    }

    // Or read a large test input from the `src/Day07_test.txt` file:
    val testInput = readInput("Day07_test")
    check(part1(testInput).also(::println) == 3749L)

    // Read the input from the `src/Day07.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
