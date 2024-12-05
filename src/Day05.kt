fun main() {
    fun part1(rules: List<String>, updates: List<String>): Int {
        val parsedRules = rules.map {
            val (a, b) = it.split("|")
            a.toInt() to b.toInt()
        }
        return updates.sumOf { update ->
            val updateValues = update.split(",").map(String::toInt)
            val updateValuesMap = updateValues.withIndex().associate { (index, value) -> value to index }
            if (
                parsedRules.all { (a, b) ->
                    val startIndex = updateValuesMap.getOrElse(a) { null }
                    val endIndex = updateValuesMap.getOrElse(b) { null }
                    startIndex == null || endIndex == null || startIndex < endIndex
                }
            ) {
                updateValues[updateValues.size / 2]
            } else {
                0
            }
        }
    }

    fun createOrder(
        pages: List<Int>,
        parsedRules: List<Pair<Int, Int>>
    ): List<Int> {
        val pageSet = pages.toSet()
        val mapToNext =
            parsedRules
                .groupBy { it.first }
                .mapValues { it.value.map { it.second }.filter { it in pageSet } }
                .filterValues { it.isNotEmpty() }
                .filterKeys { it in pageSet }
        val seedPages = pages.toMutableSet()
        mapToNext.values.forEach {
            it.forEach {
                seedPages.remove(it)
            }
        }
        val coveredPages = mutableSetOf<Int>()
        val order = mutableListOf<Int>()

        fun depthFirst(page: Int) {
            if (page in coveredPages) return
            mapToNext[page]?.forEach {
                depthFirst(it)
            }
            order.add(page)
            coveredPages.add(page)
        }

        seedPages.forEach {
            depthFirst(it)
        }

        return order
    }

    fun part2(rules: List<String>, updates: List<String>): Int {
        val parsedRules = rules.map {
            val (a, b) = it.split("|")
            a.toInt() to b.toInt()
        }
        return updates.sumOf { update ->
            val updateValues = update.split(",").map(String::toInt)
            val updateValuesMap = updateValues.withIndex().associate { (index, value) -> value to index }
            if (
                parsedRules.all { (a, b) ->
                    val startIndex = updateValuesMap.getOrElse(a) { null }
                    val endIndex = updateValuesMap.getOrElse(b) { null }
                    startIndex == null || endIndex == null || startIndex < endIndex
                }
            ) {
                0
            } else {
                createOrder(updateValues, parsedRules)[updateValues.size / 2]
            }
        }
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val testInput = readInput("Day05_test")
    val testInputBlankLineIndex = testInput.indexOf("")
    val testInputRules = testInput.subList(0, testInputBlankLineIndex)
    val testInputUpdates = testInput.subList(testInputBlankLineIndex + 1, testInput.size)

    check(part1(testInputRules, testInputUpdates) == 143)

    // Read the input from the `src/Day05.txt` file.
    val input = readInput("Day05")

    val inputBlankLineIndex = input.indexOf("")
    val inputRules = input.subList(0, inputBlankLineIndex)
    val inputUpdates = input.subList(inputBlankLineIndex + 1, input.size)
    part1(inputRules, inputUpdates).println()
    part2(inputRules, inputUpdates).println()
}
