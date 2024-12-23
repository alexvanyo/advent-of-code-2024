import androidx.compose.ui.unit.IntOffset
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.properties.Delegates

fun main() {
    data class State(
        val position: IntOffset,
        val length: Int,
    )

    fun search(
        start: IntOffset,
        end: IntOffset,
        walls: Set<IntOffset>,
    ): Map<IntOffset, Int> {
        val searched = mutableMapOf<IntOffset, Int>()
        val queue = PriorityQueue<State>(
            { a, b ->
                (a.length).compareTo(b.length)
            },
        )
        queue.add(State(start, 1))

        while (queue.isNotEmpty()) {
            val next = queue.remove()
            if (next.position in searched) continue
            searched[next.position] = next.length
            if (next.position == end) {
                break
            }

            val offsets = listOf(Direction.Left, Direction.Up, Direction.Right, Direction.Down).map {
                when (it) {
                    Direction.Left -> IntOffset(-1, 0)
                    Direction.Up -> IntOffset(0, -1)
                    Direction.Right -> IntOffset(1, 0)
                    Direction.Down -> IntOffset(0, 1)
                }
            }

            offsets.forEach { offset ->
                val pos = next.position + offset
                if (pos !in walls) {
                    queue.add(
                        State(
                            position = pos,
                            length = next.length + 1,
                        )
                    )
                }
            }
        }

        return searched
    }

    fun part1(input: List<String>, timeToSave: Int): Int {
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

        val searched = search(start, end, walls)



        return searched.entries.sumOf { (pos, length) ->
            listOf(
                IntOffset(-1, -1),
                IntOffset(1, -1),
                IntOffset(-1, 1),
                IntOffset(1, 1),
                IntOffset(-2, 0),
                IntOffset(2, 0),
                IntOffset(0, -2),
                IntOffset(0, 2),
            ).count {
                searched.getOrDefault(pos + it, 0) - length >= (timeToSave + 2)
            }
        }
    }

    fun part2(input: List<String>, timeToSave: Int): Int {
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

        val searched = search(start, end, walls)
        val validOffsets = (-20..20).flatMap { x ->
            (-20..20).map { y ->
                IntOffset(x, y)
            }
        }.filter { it.manhattanDistance <= 20 }

        return searched.entries.sumOf { (pos, length) ->
            validOffsets.count {
                searched.getOrDefault(pos + it, 0) - length >= (timeToSave + it.manhattanDistance)
            }
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day20_test")
    //check(part1(testInput) == 1)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day20")
    part1(input, 100).println()
    part2(input, 100).println()
}
