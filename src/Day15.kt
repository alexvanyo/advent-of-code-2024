import androidx.compose.ui.unit.IntOffset
import kotlin.properties.Delegates

fun main() {
    fun part1(input: List<String>): Int {
        val emptyLine = input.indexOfFirst { it == "" }

        val walls = mutableSetOf<IntOffset>()
        val boxes = mutableSetOf<IntOffset>()
        var robotPos: IntOffset by Delegates.notNull()

        input.subList(0, emptyLine).forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                val pos = IntOffset(col, row)
                when (char) {
                    '.' -> Unit
                    '#' -> walls.add(pos)
                    'O' -> boxes.add(pos)
                    '@' -> {
                        robotPos = pos
                    }
                }
            }
        }

        fun printGrid() {
            input.subList(0, emptyLine).forEachIndexed { row, line ->
                line.mapIndexed { col, _ ->
                    val pos = IntOffset(col, row)
                    if (pos == robotPos) {
                        '@'
                    } else if (pos in walls) {
                        '#'
                    } else if (pos in boxes) {
                        'O'
                    } else {
                        '.'
                    }
                }.println()
            }
        }

        val moveSequence = input.subList(emptyLine + 1, input.size).map {
            it.toCharArray().map {
                when (it) {
                    '>' -> Direction.Right
                    '<' -> Direction.Left
                    '^' -> Direction.Up
                    'v' -> Direction.Down
                    else -> error("Unexpected direction: $it")
                }
            }
        }.flatten()

        //printGrid()

        moveSequence.forEach { direction ->
            //println(direction)
            val directionOffset = when (direction) {
                Direction.Left -> IntOffset(-1, 0)
                Direction.Up -> IntOffset(0, -1)
                Direction.Right -> IntOffset(1, 0)
                Direction.Down -> IntOffset(0, 1)
            }

            var searchPos = robotPos + directionOffset

            while (searchPos !in walls && searchPos in boxes) {
                if (searchPos in boxes) {
                    searchPos += directionOffset
                }
            }
            val wasBlocked = searchPos in walls
            //println("wasBlocked: $wasBlocked")
            if (searchPos !in walls) {
                while (searchPos - directionOffset in boxes) {
                    boxes.add(searchPos)
                    boxes.remove(searchPos - directionOffset)
                    searchPos -= directionOffset
                }
                assert(searchPos == robotPos)
                robotPos += directionOffset
            }
            //printGrid()
        }

        return boxes.sumOf { boxPos ->
            100 * boxPos.y + boxPos.x
        }
    }

    fun part2(input: List<String>): Int {
        val emptyLine = input.indexOfFirst { it == "" }

        val walls = mutableSetOf<IntOffset>()
        val boxes = mutableSetOf<IntOffset>()
        var robotPos: IntOffset by Delegates.notNull()

        input.subList(0, emptyLine).forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                val pos = IntOffset(col * 2, row)
                when (char) {
                    '.' -> Unit
                    '#' -> {
                        walls.add(pos)
                        walls.add(pos + IntOffset(1, 0))
                    }
                    'O' -> boxes.add(pos)
                    '@' -> {
                        robotPos = pos
                    }
                }
            }
        }

        fun printGrid() {
            input.subList(0, emptyLine).forEachIndexed { row, line ->
                (0 until line.length * 2).map { col ->
                    val pos = IntOffset(col, row)
                    if (pos == robotPos) {
                        '@'
                    } else if (pos in walls || pos + IntOffset(-1, 0) in walls) {
                        '#'
                    } else if (pos in boxes) {
                        '['
                    } else if (pos + IntOffset(-1, 0) in boxes) {
                        ']'
                    } else {
                        '.'
                    }
                }.println()
            }
        }

        val moveSequence = input.subList(emptyLine + 1, input.size).map {
            it.toCharArray().map {
                when (it) {
                    '>' -> Direction.Right
                    '<' -> Direction.Left
                    '^' -> Direction.Up
                    'v' -> Direction.Down
                    else -> error("Unexpected direction: $it")
                }
            }
        }.flatten()

        //printGrid()

        moveSequence.forEach { direction ->
            //println(direction)
            val directionOffset = when (direction) {
                Direction.Left -> IntOffset(-1, 0)
                Direction.Up -> IntOffset(0, -1)
                Direction.Right -> IntOffset(1, 0)
                Direction.Down -> IntOffset(0, 1)
            }

            val boxesToPush = mutableListOf<IntOffset>()
            var wasBlocked = false
            val offsetsToSearch = mutableSetOf(robotPos + directionOffset)

            while (offsetsToSearch.isNotEmpty()) {
                val nextOffsetToSearch = offsetsToSearch.first()
                offsetsToSearch.remove(nextOffsetToSearch)
                if (nextOffsetToSearch in walls) {
                    wasBlocked = true
                    break
                }
                if (nextOffsetToSearch in boxes) {
                    boxesToPush.add(nextOffsetToSearch)
                    when (direction) {
                        Direction.Down -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(0, 1))
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(1, 1))
                        }
                        Direction.Left -> {
                            error("Pushing from inside the box!")
                        }
                        Direction.Right -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(2, 0))
                        }
                        Direction.Up -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(0, -1))
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(1, -1))
                        }
                    }
                } else if (nextOffsetToSearch + IntOffset(-1, 0) in boxes) {
                    boxesToPush.add(nextOffsetToSearch + IntOffset(-1, 0))
                    when (direction) {
                        Direction.Down -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(0, 1))
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(-1, 1))
                        }
                        Direction.Left -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(-2, 0))
                        }
                        Direction.Right -> {
                            error("Pushing from inside the box!")
                        }
                        Direction.Up -> {
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(0, -1))
                            offsetsToSearch.add(nextOffsetToSearch + IntOffset(-1, -1))
                        }
                    }
                }
            }
            //println("wasBlocked: $wasBlocked")
            if (!wasBlocked) {
                boxesToPush.reversed().forEach {
                    boxes.remove(it)
                    boxes.add(it + directionOffset)
                }
                robotPos += directionOffset
            }
            //printGrid()
        }

        return boxes.sumOf { boxPos ->
            100 * boxPos.y + boxPos.x
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 2028)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
