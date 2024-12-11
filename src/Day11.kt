fun main() {
    fun part1(input: List<String>): Int {
        var stones = input.first().split(" ").map(String::toLong)

        repeat(25) {
            stones = stones.flatMap { stone ->
                val stoneString = stone.toString()
                when {
                    stone == 0L -> listOf(1L)
                    stoneString.length.rem(2) == 0 -> listOf(
                        stoneString.substring(0, stoneString.length / 2).toLong(),
                        stoneString.substring(stoneString.length / 2).toLong(),
                    )
                    else -> listOf(stone * 2024L)
                }
            }
        }

        return stones.size
    }

    fun part2(input: List<String>): Long {
        val stones = input.first().split(" ").map(String::toLong)

        val memoizedMap = mutableMapOf<Pair<Long, Long>, Long>()

        fun countOf(stone: Long, blinkCount: Long): Long =
            memoizedMap.getOrPut(stone to blinkCount) {
                val stoneString = stone.toString()
                when {
                    blinkCount == 0L -> 1
                    stone == 0L -> countOf(1L, blinkCount - 1)
                    stoneString.length.rem(2) == 0 ->
                        countOf(stoneString.substring(0, stoneString.length / 2).toLong(), blinkCount - 1) +
                            countOf(stoneString.substring(stoneString.length / 2).toLong(), blinkCount - 1)
                    else -> countOf(stone * 2024, blinkCount - 1)
                }
            }

        return stones.sumOf {
            countOf(it, 75)
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(listOf("125 17")) == 55312)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
