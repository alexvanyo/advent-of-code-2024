import androidx.compose.ui.unit.IntOffset
import kotlin.properties.Delegates

fun main() {
    fun part1(input: List<String>): Int {
        val state = input.map {
            it.toCharArray().toMutableList()
        }

        var guardPosition: IntOffset by Delegates.notNull()
        lateinit var guardDirection: Direction

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->
                val pos = IntOffset(col, row)

                if (state[row][col] == '>') {
                    guardPosition = pos
                    guardDirection = Direction.Right
                    state[row][col] = '.'
                } else if (state[row][col] == '<') {
                    guardPosition = pos
                    guardDirection = Direction.Left
                    state[row][col] = '.'
                } else if (state[row][col] == '^') {
                    guardPosition = pos
                    guardDirection = Direction.Up
                    state[row][col] = '.'
                } else if (state[row][col] == 'v') {
                    guardPosition = pos
                    guardDirection = Direction.Down
                    state[row][col] = '.'
                }
            }
        }

        val positionSet = mutableSetOf(guardPosition)

        while (true) {
            val frontOffset = when (guardDirection) {
                Direction.Down -> IntOffset(0, 1)
                Direction.Left -> IntOffset(-1, 0)
                Direction.Right -> IntOffset(1, 0)
                Direction.Up -> IntOffset(0, -1)
            }

            val frontPosition = guardPosition + frontOffset

            when (state.getOrNull(frontPosition.y)?.getOrNull(frontPosition.x)) {
                null -> break
                '#' -> {
                    guardDirection = when (guardDirection) {
                        Direction.Down -> Direction.Left
                        Direction.Left -> Direction.Up
                        Direction.Right -> Direction.Down
                        Direction.Up -> Direction.Right
                    }
                }
                '.' -> {
                    guardPosition = frontPosition
                    positionSet.add(guardPosition)
                }
            }
        }

        return positionSet.size
    }

    fun part2(input: List<String>): Int {
        val state = input.map {
            it.toCharArray().toMutableList()
        }

        var initialGuardPosition: IntOffset by Delegates.notNull()
        lateinit var initialGuardDirection: Direction

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->
                val pos = IntOffset(col, row)

                if (state[row][col] == '>') {
                    initialGuardPosition = pos
                    initialGuardDirection = Direction.Right
                    state[row][col] = '.'
                } else if (state[row][col] == '<') {
                    initialGuardPosition = pos
                    initialGuardDirection = Direction.Left
                    state[row][col] = '.'
                } else if (state[row][col] == '^') {
                    initialGuardPosition = pos
                    initialGuardDirection = Direction.Up
                    state[row][col] = '.'
                } else if (state[row][col] == 'v') {
                    initialGuardPosition = pos
                    initialGuardDirection = Direction.Down
                    state[row][col] = '.'
                }
            }
        }

        var newObstacleCount = 0

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->
                val pos = IntOffset(col, row)

                if (pos != initialGuardPosition && state[row][col] == '.') {
                    state[row][col] = 'O'
                    var guardPosition = initialGuardPosition
                    var guardDirection = initialGuardDirection

                    val positionSet = mutableSetOf(guardPosition to guardDirection)

                    while (true) {
                        val frontOffset = when (guardDirection) {
                            Direction.Down -> IntOffset(0, 1)
                            Direction.Left -> IntOffset(-1, 0)
                            Direction.Right -> IntOffset(1, 0)
                            Direction.Up -> IntOffset(0, -1)
                        }

                        val frontPosition = guardPosition + frontOffset

                        when (state.getOrNull(frontPosition.y)?.getOrNull(frontPosition.x)) {
                            null -> break
                            '#', 'O' -> {
                                guardDirection = when (guardDirection) {
                                    Direction.Down -> Direction.Left
                                    Direction.Left -> Direction.Up
                                    Direction.Right -> Direction.Down
                                    Direction.Up -> Direction.Right
                                }
                            }
                            '.' -> {
                                guardPosition = frontPosition
                                val newPosition = guardPosition to guardDirection

                                if (newPosition in positionSet) {
                                    newObstacleCount++
                                    break
                                }
                                positionSet.add(newPosition)
                            }
                        }
                    }

                    state[row][col] = '.'
                }
            }
        }

        return newObstacleCount
    }

    // Or read a large test input from the `src/Day06_test.txt` file:
    val testInput = readInput("Day06_test")
    check(part1(testInput).also { it.println() } == 41)
    //check(part2(testInput) == 1)

    // Read the input from the `src/Day06.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
