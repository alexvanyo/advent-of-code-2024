import androidx.compose.ui.unit.IntOffset
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val nodes = input.map { it.toCharArray() }

        val antennas = mutableListOf<Pair<Char, IntOffset>>()

        nodes.indices.forEach { row ->
            nodes[row].indices.forEach { col ->
                val pos = IntOffset(col, row)
                if (nodes[row][col] != '.') {
                    antennas.add(nodes[row][col] to pos)
                }
            }
        }

        val antennaGroupings = antennas.groupBy { it.first }.mapValues { it.value.map { it.second } }

        val antinodes = mutableSetOf<IntOffset>()

        antennaGroupings.entries.forEach { (_, antennaLocations) ->
            antennaLocations.forEach { a ->
                antennaLocations.forEach { b ->
                    if (a != b) {
                        val diff = b - a
                        val antinode1 = b + diff
                        val antinode2 = a - diff

                        antinodes.add(antinode1)
                        antinodes.add(antinode2)
                    }
                }
            }
        }

        antinodes.removeIf {
            it.y !in nodes.indices || it.x !in nodes[0].indices
        }

        return antinodes.size
    }

    fun part2(input: List<String>): Int {
        val nodes = input.map { it.toCharArray() }

        val antennas = mutableListOf<Pair<Char, IntOffset>>()

        nodes.indices.forEach { row ->
            nodes[row].indices.forEach { col ->
                val pos = IntOffset(col, row)
                if (nodes[row][col] != '.') {
                    antennas.add(nodes[row][col] to pos)
                }
            }
        }

        fun IntOffset.isValid() =
            y in nodes.indices && x in nodes[0].indices

        val antennaGroupings = antennas.groupBy { it.first }.mapValues { it.value.map { it.second } }

        val antinodes = mutableSetOf<IntOffset>()

        antennaGroupings.entries.forEach { (_, antennaLocations) ->
            antennaLocations.forEach { a ->
                antennaLocations.forEach { b ->
                    if (a != b) {
                        val diff = b - a
                        val gcd = gcd(abs(diff.x), abs(diff.y))
                        val adjustedDiff = diff / gcd.toFloat()

                        var downCurr = a
                        while (downCurr.isValid()) {
                            antinodes.add(downCurr)
                            downCurr -= adjustedDiff
                        }

                        var upCurr = b
                        while (upCurr.isValid()) {
                            antinodes.add(upCurr)
                            upCurr += adjustedDiff
                        }
                    }
                }
            }
        }

        return antinodes.size
    }

    // Or read a large test input from the `src/Day08_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput).also(::println) == 14)

    // Read the input from the `src/Day08.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
