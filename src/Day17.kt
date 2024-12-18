fun main() {
    data class State(
        val a: Long,
        val b: Long,
        val c: Long,
        val pc: Int,
        val halt: Boolean,
    )

    fun tick(
        state: State,
        program: List<Long>,
    ): Pair<State, Long?> {
        check(!state.halt)

        val opcode = program.getOrNull(state.pc) ?:
            return State(state.a, state.b, state.c, state.pc, halt = true) to null
        val operand = program.getOrNull(state.pc + 1) ?:
            return State(state.a, state.b, state.c, state.pc, halt = true) to null

        val literal = operand.mod(8L)
        val combo = when (operand) {
            0L -> 0L
            1L -> 1L
            2L -> 2L
            3L -> 3L
            4L -> state.a
            5L -> state.b
            6L -> state.c
            else -> error("unexpected operand: $operand")
        }

        val newState = when (opcode) {
            // adv
            0L -> State(
                a = state.a / (1L shl combo.toInt()),
                b = state.b,
                c = state.c,
                pc = state.pc + 2,
                halt = false,
            ) to null
            // bxl
            1L -> State(
                a = state.a,
                b = state.b xor literal,
                c = state.c,
                pc = state.pc + 2,
                halt = false,
            ) to null
            // bst
            2L -> State(
                a = state.a,
                b = combo.mod(8L),
                c = state.c,
                pc = state.pc + 2,
                halt = false,
            ) to null
            // jnz
            3L -> {
                if (state.a == 0L) {
                    State(
                        a = state.a,
                        b = state.b,
                        c = state.c,
                        pc = state.pc + 2,
                        halt = false,
                    )
                } else {
                    State(
                        a = state.a,
                        b = state.b,
                        c = state.c,
                        pc = literal.toInt(),
                        halt = false,
                    )
                } to null
            }
            // bxc
            4L -> {
                State(
                    a = state.a,
                    b = state.b xor state.c,
                    c = state.c,
                    pc = state.pc + 2,
                    halt = false,
                ) to null
            }
            // out
            5L -> {
                State(
                    a = state.a,
                    b = state.b,
                    c = state.c,
                    pc = state.pc + 2,
                    halt = false,
                ) to combo.mod(8L)
            }
            // bdv
            6L -> State(
                a = state.a,
                b = state.a / (1L shl combo.toInt()),
                c = state.c,
                pc = state.pc + 2,
                halt = false,
            ) to null
            // cdv
            7L -> State(
                a = state.a,
                b = state.b,
                c = state.a / (1L shl combo.toInt()),
                pc = state.pc + 2,
                halt = false,
            ) to null
            else -> error("unexpected opcode: $opcode")
        }

        return newState
    }

    fun decompile(
        program: List<Long>
    ): List<String> = program.chunked(2) { (opcode, operand) ->
        val literal = operand.mod(8L)
        val combo = when (operand) {
            0L -> "0"
            1L -> "1"
            2L -> "2"
            3L -> "3"
            4L -> "A"
            5L -> "B"
            6L -> "C"
            else -> error("unexpected operand: $operand")
        }
        when (opcode) {
            0L -> "A = A / (1 << $combo)"
            1L -> "B = B xor $literal"
            2L -> "B = $combo mod 8"
            3L -> "if (A != 0) jump $literal"
            4L -> "B = B xor C"
            5L -> "out $combo mod 8"
            6L -> "B = A / (1 << $combo)"
            7L -> "C = A / (1 << $combo)"
            else -> error("unexpected opcode: $opcode")
        }
    }

    fun runProgram(
        initialState: State,
        program: List<Long>,
        expectedOutput: List<Long>? = null,
    ): List<Long> {
        val output = mutableListOf<Long>()

        var state = initialState
        do {
            val (newState, out) = tick(state, program)
            state = newState
            if (out != null) {
                output.add(out)
                if (expectedOutput != null && expectedOutput.getOrNull(output.lastIndex) != out) {
                    return output
                }
            }
        } while (!state.halt)

        return output
    }

    fun part1(input: List<String>): String {
        val a = input[0].split(" ").last().toLong()
        val b = input[1].split(" ").last().toLong()
        val c = input[2].split(" ").last().toLong()

        val initialState = State(
            a = a,
            b = b,
            c = c,
            pc = 0,
            halt = false,
        )
        val program = input[4].substringAfter(" ").split(",").map(String::toLong)

        decompile(program).forEach {
            println(it)
        }

        val output = runProgram(initialState, program)

        return output.joinToString(",")
    }

    fun createOutput(a: Long): List<Long> {
        var currA = a
        val output = mutableListOf<Long>()
        while (currA != 0L) {
            var b = currA.mod(8L)
            b = b xor 1L  // X
            val c = currA / (1 shl b.toInt())
            b = b xor 4L // Y
            b = b xor c // Z
            output.add(b.mod(8L))
            currA /= 8L
        }
        return output
    }

    fun computeA(program: List<Long>): Long {
        val memoizedMap: MutableMap<Pair<Map<Int, Boolean>, Int>, Long?> = mutableMapOf()

        fun part2Search(
            fixedABits: Map<Int, Boolean>,
            index: Int,
        ): Long? {
            val memoizationKey = fixedABits to index
            return if (memoizationKey in memoizedMap) {
                memoizedMap[memoizationKey]
            } else if (index == program.size) {
                fixedABits.toList().sumOf { (bit, value) ->
                    if (value) {
                        1L shl (bit - 3)
                    } else {
                        0L
                    }
                }
            } else {
                val value = program[program.lastIndex - index]

                val possibleC = 0L..7L
                possibleC.mapNotNull { c ->
                    val z = value xor c
                    val y = z xor 4L
                    val cOffset = y.toInt()
                    val x = y xor 1L

                    val requiredBits = listOf(
                        0 to ((x and 0b001L) != 0L),
                        1 to ((x and 0b010L) != 0L),
                        2 to ((x and 0b100L) != 0L),
                        cOffset to ((c and 0b001L) != 0L),
                        cOffset + 1 to ((c and 0b010L) != 0L),
                        cOffset + 2 to ((c and 0b100L) != 0L),
                    )

                    val isRequiredBitsValid =
                        requiredBits
                            .groupBy { it.first }
                            .values
                            .all {
                                it.map { it.second }.toSet().size == 1
                            }
                    if (isRequiredBitsValid) {
                        val requiredBitsMap = requiredBits.toMap()
                        if (requiredBitsMap.all { (i, b) -> i !in fixedABits || fixedABits[i] == b }) {
                            val newFixedABits = (fixedABits + requiredBitsMap).mapKeys { (key, _) -> key + 3 }
                            part2Search(newFixedABits, index + 1).also {
                                memoizedMap[memoizationKey] = it
                            }
                        } else {
                            null
                        }
                    } else {
                        null
                    }
                }.minOrNull()
            }
        }

        return part2Search(
            fixedABits = emptyMap(),
            index = 0,
        )!!
    }

    fun part2(input: List<String>): Long {
        val program = input[4].substringAfter(" ").split(",").map(String::toLong)
        return computeA(program)
    }

    // Or read a large test input from the `src/DayXX_test.txt` file:
    val testInput = readInput("Day17_test")
    //check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    //check(part2(testInput).also(::println) == 117440L)

    // Read the input from the `src/DayXX.txt` file.
    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
