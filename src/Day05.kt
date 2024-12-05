fun main() {
    fun part1(parsedRules: List<Pair<Int, Int>>, parsedUpdates: List<List<Int>>): Int =
        parsedUpdates.sumOf { parsedUpdate ->
            val updateValuesMap = parsedUpdate.withIndex().associate { (index, value) -> value to index }
            if (
                parsedRules.all { (a, b) ->
                    val startIndex = updateValuesMap.getOrElse(a) { null }
                    val endIndex = updateValuesMap.getOrElse(b) { null }
                    startIndex == null || endIndex == null || startIndex < endIndex
                }
            ) {
                parsedUpdate[parsedUpdate.size / 2]
            } else {
                0
            }
        }

    fun createOrder(
        pages: List<Int>,
        parsedRules: List<Pair<Int, Int>>
    ): List<Int> {
        val pageSet = pages.toSet()
        val mapToNext =
            parsedRules
                .groupBy(Pair<Int, Int>::first)
                .mapValues { it.value.map(Pair<Int, Int>::second).filter(pageSet::contains) }
                .filterValues(Collection<Int>::isNotEmpty)
                .filterKeys(pageSet::contains)
        val coveredPages = mutableSetOf<Int>()
        val order = mutableListOf<Int>()

        fun depthFirst(page: Int) {
            if (page in coveredPages) return
            mapToNext[page]?.forEach(::depthFirst)
            order.add(page)
            coveredPages.add(page)
        }
        pageSet.forEach(::depthFirst)

        return order.also {
            it.println()
        }
    }

    fun part2(parsedRules: List<Pair<Int, Int>>, parsedUpdates: List<List<Int>>): Int =
        parsedUpdates.sumOf { parsedUpdate ->
            val updateValuesMap = parsedUpdate.withIndex().associate { (index, value) -> value to index }
            if (
                parsedRules.all { (a, b) ->
                    val startIndex = updateValuesMap.getOrElse(a) { null }
                    val endIndex = updateValuesMap.getOrElse(b) { null }
                    startIndex == null || endIndex == null || startIndex < endIndex
                }
            ) {
                0
            } else {
                createOrder(parsedUpdate, parsedRules)[parsedUpdate.size / 2]
            }
        }

    fun readInputDay5(name: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val input = readInput(name)
        val blankLineIndex = input.indexOf("")
        val rules = input.subList(0, blankLineIndex)
        val updates = input.subList(blankLineIndex + 1, input.size)
        val parsedRules = rules.map {
            val (a, b) = it.split("|")
            a.toInt() to b.toInt()
        }
        val parsedUpdates = updates.map { update ->
            update.split(",").map(String::toInt)
        }
        return parsedRules to parsedUpdates
    }

    // Or read a large test input from the `src/Day05_test.txt` file:
    val (testInputParsedRules, testInputParsedUpdates) = readInputDay5("Day05_test")
    check(part2(testInputParsedRules, testInputParsedUpdates) == 123)

    // Read the input from the `src/Day05.txt` file.
    val (inputParsedRules, inputParsedUpdates) = readInputDay5("Day05")
    part1(inputParsedRules, inputParsedUpdates).println()
    part2(inputParsedRules, inputParsedUpdates).println()
}
