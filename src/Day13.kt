import androidx.compose.ui.unit.IntOffset
import java.math.BigInteger
import kotlin.math.min

fun main() {
    data class Case(
        val a: Pair<BigInteger, BigInteger>,
        val b: Pair<BigInteger, BigInteger>,
        val prize: Pair<BigInteger, BigInteger>,
    )

    fun part1(input: List<String>): BigInteger {
        val cases = input.chunked(4).map {
            val result = Regex("""Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""")
                .matchAt(it.joinToString("\n"), 0)!!

            Case(
                a = result.groupValues[1].toBigInteger() to result.groupValues[2].toBigInteger(),
                b = result.groupValues[3].toBigInteger() to result.groupValues[4].toBigInteger(),
                prize = result.groupValues[5].toBigInteger() to result.groupValues[6].toBigInteger(),
            )
        }

        return cases.withIndex().sumOf { (index, case) ->
            val aTop = case.b.second * case.prize.first - case.b.first * case.prize.second
            val bTop = case.a.first * case.prize.second - case.a.second * case.prize.first
            val bottom = case.b.second * case.a.first - case.b.first * case.a.second

            if (bottom == BigInteger.ZERO) {
                println("bottom was 0! $index")
                0L.toBigInteger()
            } else if (aTop.rem(bottom) == BigInteger.ZERO && bTop.rem(bottom) == BigInteger.ZERO) {
                val aCount = aTop / bottom
                val bCount = bTop / bottom

                3L.toBigInteger() * aCount + bCount
            } else {
                0L.toBigInteger()
            }
        }
    }

    fun part2(input: List<String>): BigInteger {
        val cases = input.chunked(4).map {
            val result = Regex("""Button A: X\+(\d+), Y\+(\d+)\nButton B: X\+(\d+), Y\+(\d+)\nPrize: X=(\d+), Y=(\d+)""")
                .matchAt(it.joinToString("\n"), 0)!!

            Case(
                a = result.groupValues[1].toBigInteger() to result.groupValues[2].toBigInteger(),
                b = result.groupValues[3].toBigInteger() to result.groupValues[4].toBigInteger(),
                prize = (result.groupValues[5].toBigInteger() + BigInteger("10000000000000")) to (result.groupValues[6].toBigInteger() + BigInteger("10000000000000")),
            )
        }

        return cases.withIndex().sumOf { (index, case) ->
            val aTop = case.b.second * case.prize.first - case.b.first * case.prize.second
            val bTop = case.a.first * case.prize.second - case.a.second * case.prize.first
            val bottom = case.b.second * case.a.first - case.b.first * case.a.second

            if (bottom == BigInteger.ZERO) {
                println("bottom was 0! $index")
                0L.toBigInteger()
            } else if (aTop.rem(bottom) == BigInteger.ZERO && bTop.rem(bottom) == BigInteger.ZERO) {
                val aCount = aTop / bottom
                val bCount = bTop / bottom

                3L.toBigInteger() * aCount + bCount
            } else {
                0L.toBigInteger()
            }
        }
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day13_test")
    //check(part1(testInput) == 480L)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
