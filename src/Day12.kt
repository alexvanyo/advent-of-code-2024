import androidx.compose.ui.unit.IntOffset

fun main() {
    val cardinalOffsets = listOf(
        IntOffset(0, 1),
        IntOffset(-1, 0),
        IntOffset(1, 0),
        IntOffset(0, -1),
    )

    fun part1(input: List<String>): Long {
        val plots = input.map { it.toCharArray().toList() }

        val coordsToCheck = plots.indices.flatMap { row ->
            plots[row].indices.map { col ->
                IntOffset(col, row)
            }
        }

        val regions = mutableListOf<List<IntOffset>>()

        val regionatedCoords = mutableSetOf<IntOffset>()

        coordsToCheck.forEach { coord ->
            if (coord !in regionatedCoords) {
                val char = plots[coord.y][coord.x]
                val region = mutableSetOf<IntOffset>()
                val coordOfRegionToSearch = mutableListOf(coord)

                while (coordOfRegionToSearch.isNotEmpty()) {
                    val nextCoordToSearch = coordOfRegionToSearch.removeFirst()
                    if (nextCoordToSearch in region) continue
                    region.add(nextCoordToSearch)
                    cardinalOffsets.forEach { offset ->
                        val pos = nextCoordToSearch + offset
                        if (plots.getOrNull(pos.y)?.getOrNull(pos.x) == char) {
                            coordOfRegionToSearch.add(pos)
                        }
                    }
                }
                regions.add(region.toList())
                regionatedCoords.addAll(region)
            }
        }

        return regions.sumOf { region ->
            val area = region.size
            val perimeter = region.sumOf { coord ->
                cardinalOffsets.sumOf { offset ->
                    val pos = coord + offset
                    if (plots.getOrNull(pos.y)?.getOrNull(pos.x) != plots[coord.y][coord.x]) {
                        1L
                    } else {
                        0L
                    }
                }
            }
            area * perimeter
        }
    }

    fun part2(input: List<String>): Long {
        val plots = input.map { it.toCharArray().toList() }

        val coordsToCheck = plots.indices.flatMap { row ->
            plots[row].indices.map { col ->
                IntOffset(col, row)
            }
        }

        val regions = mutableListOf<Set<IntOffset>>()

        val regionatedCoords = mutableSetOf<IntOffset>()

        coordsToCheck.forEach { coord ->
            if (coord !in regionatedCoords) {
                val char = plots[coord.y][coord.x]
                val region = mutableSetOf<IntOffset>()
                val coordOfRegionToSearch = mutableListOf(coord)

                while (coordOfRegionToSearch.isNotEmpty()) {
                    val nextCoordToSearch = coordOfRegionToSearch.removeFirst()
                    if (nextCoordToSearch in region) continue
                    region.add(nextCoordToSearch)
                    cardinalOffsets.forEach { offset ->
                        val pos = nextCoordToSearch + offset
                        if (plots.getOrNull(pos.y)?.getOrNull(pos.x) == char) {
                            coordOfRegionToSearch.add(pos)
                        }
                    }
                }
                regions.add(region.toSet())
                regionatedCoords.addAll(region)
            }
        }

        return regions.sumOf { region ->
            val area = region.size
            val fences = region.flatMap { coord ->
                cardinalOffsets.mapNotNull { offset ->
                    val pos = coord + offset
                    if (plots.getOrNull(pos.y)?.getOrNull(pos.x) != plots[coord.y][coord.x]) {
                        coord to pos
                    } else {
                        null
                    }
                }
            }.toMutableSet()
            var sides = 0L
            while (fences.isNotEmpty()) {
                val fence = fences.first()
                fences.remove(fence)

                val inner = fence.first
                val outer = fence.second
                if (inner.x == outer.x) {
                    var leftCurr = 0
                    while (
                        fences.remove(
                            inner + IntOffset(leftCurr - 1, 0) to outer + IntOffset(leftCurr - 1, 0)
                        )
                    ) {
                        leftCurr--
                    }
                    var rightCurr = 0
                    while (
                        fences.remove(
                            inner + IntOffset(rightCurr + 1, 0) to outer + IntOffset(rightCurr + 1, 0)
                        )
                    ){
                        rightCurr++
                    }
                } else {
                    assert(inner.y == outer.y)
                    var upCurr = 0
                    while (
                        fences.remove(
                            inner + IntOffset(0, upCurr - 1) to outer + IntOffset(0, upCurr - 1)
                        )
                    ) {
                        upCurr--
                    }
                    var bottomCurr = 0
                    while (
                        fences.remove(
                            inner + IntOffset(0, bottomCurr + 1) to outer + IntOffset(0, bottomCurr + 1)
                        )
                    ){
                        bottomCurr++
                    }
                }

                sides++
            }
            area * sides
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 140L)
    check(part2(testInput) == 80L)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
