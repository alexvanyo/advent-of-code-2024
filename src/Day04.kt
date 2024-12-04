fun main() {
    fun part1(input: List<String>): Int {
        val inputCharArray = input.map {
            it.toCharArray()
        }
        var count = 0

        inputCharArray.indices.forEach { row ->
            inputCharArray[row].indices.forEach { col ->
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col + 1) == 'M' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col + 2) == 'A' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col + 3)== 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col - 1) == 'M' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col - 2) == 'A' &&
                    inputCharArray.getOrNull(row)?.getOrNull(col - 3)== 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row + 1)?.getOrNull(col) == 'M' &&
                    inputCharArray.getOrNull(row + 2)?.getOrNull(col) == 'A' &&
                    inputCharArray.getOrNull(row + 3)?.getOrNull(col) == 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row - 1)?.getOrNull(col) == 'M' &&
                    inputCharArray.getOrNull(row - 2)?.getOrNull(col) == 'A' &&
                    inputCharArray.getOrNull(row - 3)?.getOrNull(col) == 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row + 1)?.getOrNull(col + 1) == 'M' &&
                    inputCharArray.getOrNull(row + 2)?.getOrNull(col + 2) == 'A' &&
                    inputCharArray.getOrNull(row + 3)?.getOrNull(col + 3) == 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row + 1)?.getOrNull(col - 1) == 'M' &&
                    inputCharArray.getOrNull(row + 2)?.getOrNull(col - 2) == 'A' &&
                    inputCharArray.getOrNull(row + 3)?.getOrNull(col - 3) == 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row - 1)?.getOrNull(col + 1) == 'M' &&
                    inputCharArray.getOrNull(row - 2)?.getOrNull(col + 2) == 'A' &&
                    inputCharArray.getOrNull(row - 3)?.getOrNull(col + 3) == 'S') {
                    count++
                }
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'X' &&
                    inputCharArray.getOrNull(row - 1)?.getOrNull(col - 1) == 'M' &&
                    inputCharArray.getOrNull(row - 2)?.getOrNull(col - 2) == 'A' &&
                    inputCharArray.getOrNull(row - 3)?.getOrNull(col - 3) == 'S') {
                    count++
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val inputCharArray = input.map {
            it.toCharArray()
        }
        var count = 0

        inputCharArray.indices.forEach { row ->
            inputCharArray[row].indices.forEach { col ->
                if (inputCharArray.getOrNull(row)?.getOrNull(col) == 'A') {
                    val nw = inputCharArray.getOrNull(row - 1)?.getOrNull(col - 1)
                    val ne = inputCharArray.getOrNull(row - 1)?.getOrNull(col + 1)
                    val sw = inputCharArray.getOrNull(row + 1)?.getOrNull(col - 1)
                    val se = inputCharArray.getOrNull(row + 1)?.getOrNull(col + 1)

                    if (((nw == 'S' && se == 'M') || (nw == 'M' && se == 'S')) &&
                        ((sw == 'S' && ne == 'M') || (sw == 'M' && ne == 'S'))) {
                        count++
                    }
                }
            }
        }
        return count
    }

    // Or read a large test input from the `src/Day04_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput).also { it.println() } == 18)

    // Read the input from the `src/Day04.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
