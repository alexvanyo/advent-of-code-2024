fun main() {
    fun calculateNext(value: Long): Long {
        val a = value * 64L
        val b = value xor a
        val c = b.mod(16777216L)
        val d = c / 32L
        val e = c xor d
        val f = e.mod(16777216L)
        val g = f * 2048L
        val h = f xor g
        val i = h.mod(16777216L)
        return i
    }

    fun bananasBought(
        seed: Long,
        diffSequence: List<Int>,
        length: Int = 2000
    ): Long {
        var curr = seed
        val currDiff = mutableListOf<Int>()
        repeat(length) {
            val next = calculateNext(curr)
            currDiff.addLast(next.mod(10) - curr.mod(10))
            while (currDiff.size > 4) {
                currDiff.removeFirst()
            }
            if (currDiff == diffSequence) {
                return next.mod(10).toLong()
            }
            curr = next
        }
        return 0L
    }

    fun part1(input: List<String>): Long =
        input.sumOf { line ->
            val seed = line.toLong()
            generateSequence(seed, ::calculateNext).take(2001).last()
        }

    fun part2(input: List<String>): Long {
        val amountMaps = input.map { line ->
            val map = mutableMapOf<List<Int>, Long>()

            val seed = line.toLong()
            var curr = seed
            val currDiff = mutableListOf<Int>()
            repeat(2000) {
                val next = calculateNext(curr)
                currDiff.addLast(next.mod(10) - curr.mod(10))
                while (currDiff.size > 4) {
                    currDiff.removeFirst()
                }
                val key = currDiff.toList()
                if (key !in map) {
                    map[key] = next.mod(10).toLong()
                }
                curr = next
            }
            map
        }

        return (-9..9).maxOf { a ->
            println("a: $a")
            (-9..9).maxOf { b ->
                println("b: $b")
                (-9..9).maxOf { c ->
                    (-9..9).maxOf { d ->
                        amountMaps.sumOf {
                            it.getOrDefault(listOf(a, b, c, d), 0L)
                        }
                    }
                }
            }
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day22_test")

    check(part1(testInput) == 37327623L)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
