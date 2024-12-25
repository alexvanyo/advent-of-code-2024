fun main() {
    fun choose(set: Set<String>, amount: Int): Set<Set<String>> =
        if (amount == 0) {
            setOf(emptySet())
        } else {
            set.flatMap { value ->
                choose(set - value, amount - 1).map {
                    it + value
                }
            }.toSet()
        }

    fun part1(input: List<String>): Int {
        val connections = input.map {
            val (a, b) = it.split("-")
            a to b
        }

        val edges = (connections + connections.map { it.second to it.first })
            .groupBy { it.first }
            .mapValues { it.value.map { it.second }.toSet() }

        val k3 = mutableSetOf<Set<String>>()

        edges.keys.forEach { a ->
            val bs = edges[a].orEmpty()
            bs.forEach { b ->
                val cs = edges[b].orEmpty()
                cs.forEach { c ->
                    if (a in edges[c].orEmpty()) {
                        k3.add(setOf(a, b, c))
                    }
                }
            }
        }

        return k3.filter { it.any { it.startsWith("t") } }.size
    }

    fun part2(input: List<String>): String {
        val connections = input.map {
            val (a, b) = it.split("-")
            a to b
        }

        val edges = (connections + connections.map { it.second to it.first })
            .groupBy { it.first }
            .mapValues { it.value.map { it.second }.toSet() }

        var largestCliques = setOf(emptySet<String>())
        val nodes = edges.keys

        do {
            var newLargestCliques = mutableSetOf<Set<String>>()
            largestCliques.forEach { largestClique ->
                nodes.forEach { node ->
                    if (node !in largestClique) {
                        val testSubset = largestClique + node
                        val isClique = testSubset.all { a ->
                            testSubset.all { b ->
                                if (a == b) {
                                    true
                                } else {
                                    b in edges[a].orEmpty()
                                }
                            }
                        }
                        if (isClique) {
                            newLargestCliques.add(testSubset)
                        }
                    }
                }
            }

            if (newLargestCliques.size != 0) {
                largestCliques = newLargestCliques
                println("new largest cliques: $largestCliques")
            }
        } while (newLargestCliques.size != 0)

        return largestCliques.first().sorted().joinToString(",")
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
