import kotlin.properties.Delegates

fun main() {
    fun part1(input: List<String>): Int {
        val state = input.map {
            it.toCharArray().toMutableList()
        }

        var guardX by Delegates.notNull<Int>()
        var guardY by Delegates.notNull<Int>()
        lateinit var guardDirection: Direction

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->
                if (state[row][col] == '>') {
                    guardX = col
                    guardY = row
                    guardDirection = Direction.Right
                    state[row][col] = '.'
                } else if (state[row][col] == '<') {
                    guardX = col
                    guardY = row
                    guardDirection = Direction.Left
                    state[row][col] = '.'
                } else if (state[row][col] == '^') {
                    guardX = col
                    guardY = row
                    guardDirection = Direction.Up
                    state[row][col] = '.'
                } else if (state[row][col] == 'v') {
                    guardX = col
                    guardY = row
                    guardDirection = Direction.Down
                    state[row][col] = '.'
                }
            }
        }

        val positionSet = mutableSetOf(guardX to guardY)

        while (true) {
            val frontOffset = when (guardDirection) {
                Direction.Down -> 0 to 1
                Direction.Left -> -1 to 0
                Direction.Right -> 1 to 0
                Direction.Up -> 0 to -1
            }

            val frontPositionX = guardX + frontOffset.first
            val frontPositionY = guardY + frontOffset.second

            when (state.getOrNull(frontPositionY)?.getOrNull(frontPositionX)) {
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
                    guardX = frontPositionX
                    guardY = frontPositionY
                    positionSet.add(guardX to guardY)
                }
            }
        }

        return positionSet.size
    }

    fun part2(input: List<String>): Int {
        val state = input.map {
            it.toCharArray().toMutableList()
        }

        var initialGuardX by Delegates.notNull<Int>()
        var initialGuardY by Delegates.notNull<Int>()
        lateinit var initialGuardDirection: Direction

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->
                if (state[row][col] == '>') {
                    initialGuardX = col
                    initialGuardY = row
                    initialGuardDirection = Direction.Right
                    state[row][col] = '.'
                } else if (state[row][col] == '<') {
                    initialGuardX = col
                    initialGuardY = row
                    initialGuardDirection = Direction.Left
                    state[row][col] = '.'
                } else if (state[row][col] == '^') {
                    initialGuardX = col
                    initialGuardY = row
                    initialGuardDirection = Direction.Up
                    state[row][col] = '.'
                } else if (state[row][col] == 'v') {
                    initialGuardX = col
                    initialGuardY = row
                    initialGuardDirection = Direction.Down
                    state[row][col] = '.'
                }
            }
        }

        var newObstacleCount = 0

        state.indices.forEach { row ->
            state[row].indices.forEach { col ->

                if ((row != initialGuardY || col != initialGuardX) && state[row][col] == '.') {
                    state[row][col] = 'O'

                    var guardX = initialGuardX
                    var guardY = initialGuardY
                    var guardDirection = initialGuardDirection

                    val positionSet = mutableSetOf(Triple(initialGuardX, guardY, guardDirection))

                    while (true) {
                        val frontOffset = when (guardDirection) {
                            Direction.Down -> 0 to 1
                            Direction.Left -> -1 to 0
                            Direction.Right -> 1 to 0
                            Direction.Up -> 0 to -1
                        }

                        val frontPositionX = guardX + frontOffset.first
                        val frontPositionY = guardY + frontOffset.second

                        when (state.getOrNull(frontPositionY)?.getOrNull(frontPositionX)) {
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
                                guardX = frontPositionX
                                guardY = frontPositionY
                                val newPosition = Triple(guardX, guardY, guardDirection)

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

sealed interface Direction {
    data object Up : Direction
    data object Down : Direction
    data object Left : Direction
    data object Right : Direction
}
