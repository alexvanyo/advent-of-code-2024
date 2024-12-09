fun main() {
    fun part1(input: List<String>): Long {
        val inputLine = input.first()

        val blockArray = inputLine.foldIndexed(emptyList<Long?>()) { index, acc, char ->
            val amount = char.digitToInt()
            if (index.rem(2) == 0) {
                acc + List(amount) { (index / 2).toLong() }
            } else {
                acc + List(amount) { null }
            }
        }

        val workingBlockArray = blockArray.toMutableList()

        var freeIndex = 0
        var lastIndex = blockArray.size - 1

        while (freeIndex < lastIndex) {
            if (workingBlockArray[freeIndex] != null) {
                freeIndex++
            } else {
                workingBlockArray[freeIndex] = workingBlockArray[lastIndex]
                workingBlockArray[lastIndex] = null
                lastIndex--
            }
        }

        return workingBlockArray.withIndex().sumOf { (index, id) ->
            index * (id ?: 0L)
        }
    }

    fun part2(input: List<String>): Long {
        val inputLine = input.first()

        val idBlocks = mutableListOf<Pair<Int, Int>>()
        val freeSpaceBlocks = mutableListOf<Pair<Int, Int>>()

        val blockArray = inputLine.foldIndexed(emptyList<Long?>()) { index, acc, char ->
            val amount = char.digitToInt()
            if (index.rem(2) == 0) {
                if (amount > 0) {
                    idBlocks.add(acc.size to amount)
                }
                acc + List(amount) { (index / 2).toLong() }
            } else {
                if (amount > 0) {
                    freeSpaceBlocks.add(acc.size to amount)
                }
                acc + List(amount) { null }
            }
        }

        val workingBlockArray = blockArray.toMutableList()

        while (idBlocks.isNotEmpty()) {
            val idBlockSize = idBlocks.last().second
            for (i in freeSpaceBlocks.indices) {
                if (freeSpaceBlocks[i].first < idBlocks.last().first && freeSpaceBlocks[i].second >= idBlocks.last().second) {
                    for (j in 0 until idBlockSize) {
                        workingBlockArray[freeSpaceBlocks[i].first + j] = workingBlockArray[idBlocks.last().first + j]
                        workingBlockArray[idBlocks.last().first + j] = null
                    }
                    freeSpaceBlocks[i] =
                        freeSpaceBlocks[i].first + idBlockSize to freeSpaceBlocks[i].second - idBlockSize
                    break
                }
            }
            idBlocks.removeLast()
        }

        return workingBlockArray.withIndex().sumOf { (index, id) ->
            index * (id ?: 0L)
        }
    }

    // Or read a large test input from the `src/Day09_test.txt` file:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day09.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
