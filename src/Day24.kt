import kotlin.random.Random

sealed interface Gate {
    val inA: String
    val inB: String
    val out: String

    fun copy(
        out: String,
    ): Gate

    data class And(
        override val inA: String,
        override val inB: String,
        override val out: String,
    ) : Gate {
        override fun copy(out: String): And =
            copy(inA = inA, inB = inB, out = out)
    }

    data class Xor(
        override val inA: String,
        override val inB: String,
        override val out: String,
    ) : Gate {
        override fun copy(out: String): Xor =
            copy(inA = inA, inB = inB, out = out)
    }

    data class Or(
        override val inA: String,
        override val inB: String,
        override val out: String,
    ) : Gate {
        override fun copy(out: String): Or =
            copy(inA = inA, inB = inB, out = out)
    }
}

fun main() {
    data class Input(
        val inputWires: Map<String, Boolean>,
        val gates: Map<String, Gate>,
    ) {
        val allWires = inputWires.keys + gates.flatMap { (_, gate) -> listOf(gate.inA, gate.inB, gate.out) }.toSet()
        val allZWires = gates.keys.filter { it.startsWith("z") }.toSet()
    }

    fun calculateInputWires(
        x: Long,
        y: Long,
    ): Map<String, Boolean> =
        (0..44).flatMap { bit ->
            listOf(
                "x%02d".format(bit) to ((x and (1L shl bit)) != 0L),
                "y%02d".format(bit) to ((y and (1L shl bit)) != 0L)
            )
        }.toMap()

    fun parseInput(input: List<String>): Input {
        val emptyLineIndex = input.indexOf("")
        val inputWires = input.subList(0, emptyLineIndex).map {
            val (name, value) = it.split(": ")
            name to (value.toInt() == 1)
        }.toMap()
        val gates = input.subList(emptyLineIndex + 1, input.size).map {
            val (inA, gate, inB, _, out) = it.split(" ")
            out to when (gate) {
                "AND" -> Gate.And(inA, inB, out)
                "XOR" -> Gate.Xor(inA, inB, out)
                "OR" -> Gate.Or(inA, inB, out)
                else -> error("invalid gate type")
            }
        }.toMap()
        return Input(
            inputWires = inputWires,
            gates = gates
        )
    }

    fun calculateDependencies(
        parsedInput: Input,
        wire: String,
        substituteInputWires: Boolean,
        depth: Int = Int.MAX_VALUE,
    ): String =
        if (wire in parsedInput.inputWires) {
            if (substituteInputWires) {
                parsedInput.inputWires.getValue(wire).toString()
            } else {
                wire
            }
        } else if (depth == 0) {
            wire
        } else {
            val gate = parsedInput.gates.getValue(wire)
            val gateName = when (gate) {
                is Gate.And -> "AND"
                is Gate.Or -> "OR"
                is Gate.Xor -> "XOR"
            }
            val color = Random(seed = wire.hashCode()).nextInt(16, 232)
            val startColor = "\u001b[38:5:${color}m"
            val endColor = "\u001b[0m"

            "$startColor($endColor" +
                    "$wire=" +
                    calculateDependencies(parsedInput, gate.inA, substituteInputWires, depth - 1) +
                    " $gateName " +
                    calculateDependencies(parsedInput, gate.inB, substituteInputWires, depth - 1) +
                    "$startColor)$endColor"
        }

    fun calculate(parsedInput: Input): Long {
        val wireStates = parsedInput.inputWires.toMutableMap()

        fun calculateState(wire: String): Boolean =
            if (wire in wireStates) {
                wireStates[wire]!!
            } else {
                val gate = parsedInput.gates.getValue(wire)
                check(gate.out == wire)
                val inA = calculateState(gate.inA)
                val inB = calculateState(gate.inB)

                val out = when (gate) {
                    is Gate.And -> inA && inB
                    is Gate.Or -> inA || inB
                    is Gate.Xor -> inA xor inB
                }
                wireStates[wire] = out
                out
            }

        return parsedInput.allZWires.sumOf { wire ->
            val bit = wire.removePrefix("z").toInt()
            val value = calculateState(wire)
            if (value) {
                1L shl bit
            } else {
                0L
            }
        }
    }

    fun part1(input: List<String>): Long =
        calculate(parseInput(input))

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day24_test")
    check(part1(testInput) == 4L)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day24")
    part1(input).println()

    val parsedInput = parseInput(input)

    val flippedGates = listOf<Pair<String, String>>(
        "z13" to "vcv",
        "vwp" to "z19",
        "mps" to "z25",
        "vjv" to "cqm",
    )
        .flatMap { (a, b) ->
            listOf(a to b, b to a)
        }
        .toMap()

    println(flippedGates.keys.sorted().joinToString(","))

    val modifiedParsedInput = parsedInput.copy(
        gates = parsedInput.gates.map { (wire, gate) ->
            if (wire in flippedGates) {
                val newOut = flippedGates.getValue(wire)
                newOut to gate.copy(out = newOut)
            } else {
                wire to gate
            }
        }
            .toMap()
    )

    (0..45).forEach { bit ->
        println(calculateDependencies(modifiedParsedInput, "z%02d".format(bit), false, 4))
    }

    val testInputs = listOf<Pair<Long, Long>>(
        0b0L to 0b0L,
    ) + (1..44).flatMap { bit ->
        listOf(
            ((1L shl bit) - 1L) to 1L,
            1L to ((1L shl bit) - 1L),
        )
    }
    testInputs.forEach { (a, b) ->
        val out = calculate(
            modifiedParsedInput.copy(
                inputWires = calculateInputWires(a, b)
            )
        )
        println("testInput: ${a.toString(2)}, ${b.toString(2)}: ${out.toString(2)}")
        check(a + b == out)
    }
}
