import androidx.compose.ui.unit.IntOffset
import java.util.PriorityQueue
import kotlin.properties.Delegates

fun main() {
    data class State(
        val position: IntOffset,
        val direction: Direction,
        val points: Int,
        val tiles: Set<IntOffset>,
    )

    fun search(input: List<String>): State {
        val walls = mutableSetOf<IntOffset>()
        var start: IntOffset by Delegates.notNull()
        var end: IntOffset by Delegates.notNull()
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                val pos = IntOffset(col, row)
                when (char) {
                    '#' -> walls.add(pos)
                    'E' -> end = pos
                    'S' -> start = pos
                }
            }
        }

        val searched = mutableSetOf<Pair<IntOffset, Direction>>()
        val queue = PriorityQueue<State>(
            { a, b ->
                a.points.compareTo(b.points)
            },
        )
        queue.add(
            State(
                position = start,
                direction = Direction.Right,
                points = 0,
                tiles = setOf(start),
            )
        )

        while (queue.isNotEmpty()) {
            val nextGroup = mutableListOf<State>()
            val currentPoints = queue.peek()?.points
            while (queue.peek()?.points == currentPoints) {
                nextGroup.add(queue.remove())
            }
            val filteredNextGroup = nextGroup
                .groupingBy { (Triple(it.position, it.direction, it.points) ) }
                .fold(emptySet<IntOffset>()) { acc, state -> acc + state.tiles }
                .map { (key, tiles) ->
                    State(
                        position = key.first,
                        direction = key.second,
                        points = key.third,
                        tiles = tiles,
                    )
                }

            for (next in filteredNextGroup) {
                val searchKey = next.position to next.direction
                if (searchKey in searched) {
                    continue
                }
                searched.add(searchKey)

                if (next.position == end) {
                    return next
                }

                val offset = when (next.direction) {
                    Direction.Left -> IntOffset(-1, 0)
                    Direction.Up -> IntOffset(0, -1)
                    Direction.Right -> IntOffset(1, 0)
                    Direction.Down -> IntOffset(0, 1)
                }

                val forward = next.position + offset

                if (forward !in walls) {
                    queue.add(
                        State(
                            position = forward,
                            direction = next.direction,
                            points = next.points + 1,
                            tiles = next.tiles + forward,
                        )
                    )
                }
                queue.add(
                    State(
                        position = next.position,
                        direction = when (next.direction) {
                            Direction.Left -> Direction.Up
                            Direction.Up -> Direction.Right
                            Direction.Right -> Direction.Down
                            Direction.Down -> Direction.Left
                        },
                        points = next.points + 1000,
                        tiles = next.tiles,
                    )
                )
                queue.add(
                    State(
                        position = next.position,
                        direction = when (next.direction) {
                            Direction.Left -> Direction.Down
                            Direction.Up -> Direction.Left
                            Direction.Right -> Direction.Up
                            Direction.Down -> Direction.Right
                        },
                        points = next.points + 1000,
                        tiles = next.tiles,
                    )
                )
            }
        }
        error("Couldn't find end")
    }

    fun part1(input: List<String>): Int =
        search(input).points

    fun part2(input: List<String>): Int =
        search(input).tiles.size

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput).also(::println) == 45)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
