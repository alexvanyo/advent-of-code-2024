import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import java.util.*

fun main() {

    data class State(
        val position: IntOffset,
        val length: Int,
    )

    fun search(
        bytes: Set<IntOffset>,
        size: IntSize,
    ): Int {
        val start = IntOffset.Zero
        val end = IntOffset(size.width - 1, size.height - 1)

        val searched = mutableSetOf<IntOffset>()
        val queue = PriorityQueue<State>(
            { a, b ->
                (a.length)
                    .compareTo(b.length)
            },
        )
        queue.add(State(start, 0))

        while (queue.isNotEmpty()) {
            val next = queue.remove()
            if (next.position in searched) continue
            searched.add(next.position)
            if (next.position == end) {
                return next.length
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
                if (pos !in bytes && pos.x in 0 until size.width && pos.y in 0 until size.height) {
                    queue.add(
                        State(
                            position = pos,
                            length = next.length + 1,
                        )
                    )
                }
            }
        }

        return Int.MAX_VALUE
    }

    fun part1(input: List<String>, size: IntSize, numBytes: Int): Int {
        val bytes = input.map {
            val (x, y) = it.split(",").map(String::toInt)
            IntOffset(x, y)
        }.take(numBytes).toSet()

        return search(bytes, size)
    }

    fun firstBlockingByteIndex(bytes: List<IntOffset>, size: IntSize): Int {
        var low = 1
        var high = bytes.size - 1

        while (low < high) {
            val mid = (low + high).ushr(1) // safe from overflows
            val midValue = search(bytes.take(mid).toSet(), size)

            if (midValue < Int.MAX_VALUE) {
                low = mid + 1
            } else {
                high = mid
            }
        }
        check(search(bytes.take(high - 1).toSet(), size) < Int.MAX_VALUE)
        check(search(bytes.take(high).toSet(), size) == Int.MAX_VALUE)
        return high - 1
    }

    fun part2(input: List<String>, size: IntSize): String {
        val bytes = input.map {
            val (x, y) = it.split(",").map(String::toInt)
            IntOffset(x, y)
        }.toList()

        val byteIndex = firstBlockingByteIndex(bytes, size)
        val byte = bytes[byteIndex]

        return "${byte.x},${byte.y}"
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day18_test")
    check(part1(testInput, IntSize(7, 7), numBytes = 12) == 22)
    check(part2(testInput, IntSize(7, 7)) == "6,1")

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day18")
    part1(input, IntSize(71, 71), numBytes = 1024).println()
    part2(input, IntSize(71, 71)).println()
}
