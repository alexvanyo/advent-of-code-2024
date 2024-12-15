import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.floor

fun main() {
    fun part1(
        input: List<String>,
        time: Int = 100,
        size: IntSize = IntSize(101, 103)
    ): Int {
        val robotsInitial = input.map {
            val match = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""").matchEntire(it)!!
            IntOffset(match.groupValues[1].toInt(), match.groupValues[2].toInt()) to
                IntOffset(match.groupValues[3].toInt(), match.groupValues[4].toInt())
        }

        val robotsFinalPositions = robotsInitial.map { (initialPos, velocity) ->
            val finalPos = initialPos + velocity * time.toFloat()
            IntOffset(
                x = finalPos.x.mod(size.width),
                y = finalPos.y.mod(size.height),
            )
        }

        var a = 0
        var b = 0
        var c = 0
        var d = 0

        robotsFinalPositions.forEach { pos ->
            if (pos.x < size.width / 2) {
                if (pos.y < size.height / 2) {
                    a++
                } else if (pos.y > size.height / 2) {
                    b++
                }
            } else if (pos.x > size.width / 2) {
                if (pos.y < size.height / 2) {
                    c++
                } else if (pos.y > size.height / 2) {
                    d++
                }
            }
        }

        return a * b * c * d
    }

    fun part2(
        input: List<String>,
        size: IntSize = IntSize(101, 103)
    ): Int {
        val robotsInitial = input.map {
            val match = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""").matchEntire(it)!!
            IntOffset(match.groupValues[1].toInt(), match.groupValues[2].toInt()) to
                IntOffset(match.groupValues[3].toInt(), match.groupValues[4].toInt())
        }

        repeat(10000) { time ->
            val robotsFinalPositions = robotsInitial.map { (initialPos, velocity) ->
                val finalPos = initialPos + velocity * time.toFloat()
                IntOffset(
                    x = finalPos.x.mod(size.width),
                    y = finalPos.y.mod(size.height),
                )
            }.toSet()

            var hasLineOf7 = false
            val lines = (0 until size.height).map { y ->
                (0 until size.width).map { x ->
                    if (IntOffset(x, y) in robotsFinalPositions) {
                        "X"
                    } else {
                        " "
                    }
                }.joinToString("").also {
                    hasLineOf7 = hasLineOf7 || it.contains("XXXXXXX")
                }
            }
            if (hasLineOf7) {
                println("time: $time")
                lines.forEach(::println)
                println()
            }
        }
        return 0
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day14_test")
    check(part1(testInput, size = IntSize(11, 7)).also(::println) == 12)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
